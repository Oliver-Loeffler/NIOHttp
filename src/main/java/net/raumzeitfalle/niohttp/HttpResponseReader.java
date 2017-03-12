package net.raumzeitfalle.niohttp;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Reads from a channel and produces HttpResponses.
 */
public class HttpResponseReader {

    private final ReadableByteChannel channel;

    private byte[] bytesRead = new byte[0];

    /**
     * Creates a new reader for a given channel. Call the read() method to
     * obtain a {@link HttpResponse}.
     * 
     * @param channel
     *            ReadableByteChannel
     */
    public HttpResponseReader(final ReadableByteChannel channel) {
	this.channel = Objects.requireNonNull(channel, "channel must not be null");
    }

    /**
     * Creates a {@link FutureTask} (without return value) to read HttpResponses
     * from a given channel and supply a Consumer with them.
     * 
     * @param channel
     *            {@link ReadableByteChannel}
     * @param responseConsumer
     *            {@link Consumer} for {@link HttpResponse}
     * @return futureTask {@link FutureTask} w/o return value
     */
    public static FutureTask<Void> fromChannel(final ReadableByteChannel channel,
	    final Consumer<HttpResponse> responseConsumer) {

	return new FutureTask<>(() -> {
	    HttpResponseReader reader = new HttpResponseReader(channel);
	    reader.read(responseConsumer);
	    return null;
	});

    }

    public static Stream<HttpResponse> fromChannel(final ReadableByteChannel channel) {
	HttpResponseSupplier consumer = new HttpResponseSupplier();
	FutureTask<Void> futureTask = HttpResponseReader.fromChannel(channel, consumer);
	ExecutorService executor = Executors.newFixedThreadPool(1);
	executor.submit(futureTask);
	boolean incomplete = true;
	while (incomplete) {
	    if (futureTask.isDone()) {
		executor.shutdown();
		return Stream.generate(consumer);
	    }
	}
	executor.shutdown();
	return Stream.empty();

    }

    public void read(final Consumer<HttpResponse> responseConsumer) throws IOException {
	Objects.requireNonNull(responseConsumer, "responseConsumer must not be null");
	readBytesFromChannel();
	HttpResponse response = fromBytes(this.bytesRead);
	responseConsumer.accept(response);
    }

    public HttpResponse get() {
	return fromBytes(this.bytesRead);
    }

    /**
     * Factory method creating a {@link HttpResponse} object from a byte array.
     *
     * @param bytes
     *            byte array read from a channel
     * @return HttpResponse object
     */
    private HttpResponse fromBytes(byte[] bytes) {
	String[] responseLines = new String(bytes).split(CRLF);

	StatusLine statusLine = new StatusLine(responseLines[0]);
	HttpResponseBuilder responseBuilder = new HttpResponseBuilder(statusLine);

	// build message header here
	int lineIndex = 1;
	String line = responseLines[lineIndex];
	while (!line.isEmpty()) {
	    HeaderField messageField = HeaderFieldFactory.getInstance().fromString(line);
	    int separation = line.indexOf(SPACE) + 1;
	    responseBuilder.addMessageField(messageField, line.substring(separation, line.length()));
	    line = responseLines[lineIndex++];
	}

	// provide message header to response builder

	// use details from message header to control payload parsing

	if (responseBuilder.requiresPayload()) {
	    int firstPayloadByte = findFirstPayloadByteIndex(bytes);
	    if (firstPayloadByte < bytes.length) {
		byte[] payloadBytes = new byte[bytes.length - firstPayloadByte];
		System.arraycopy(bytes, firstPayloadByte, payloadBytes, 0, payloadBytes.length);
		responseBuilder.withPayload(payloadBytes);
	    }
	}
	// add payload to builder

	// return a new HttpResponse object

	// ... continue parsing --> how do

	return responseBuilder.build();
    }

    private void readBytesFromChannel() throws IOException {
	this.bytesRead = new byte[0];
	int numberOfBytesRead = 0;
	int byteBufferSize = 48;
	ByteBuffer inputBuffer = ByteBuffer.allocate(byteBufferSize);
	do {
	    inputBuffer.clear();
	    numberOfBytesRead = channel.read(inputBuffer);
	    if (numberOfBytesRead > 0) {
		this.bytesRead = mergeArrays(this.bytesRead, inputBuffer.array(), numberOfBytesRead);
	    }
	} while (numberOfBytesRead > 0);
    }

    private byte[] mergeArrays(byte[] oldBytes, byte[] newBytes, int numberOfNewBytesToConsider) {
	assert (numberOfNewBytesToConsider <= newBytes.length);
	byte[] result = new byte[oldBytes.length + numberOfNewBytesToConsider];
	System.arraycopy(oldBytes, 0, result, 0, oldBytes.length);
	System.arraycopy(newBytes, 0, result, oldBytes.length, numberOfNewBytesToConsider);
	return result;
    }

    private static int findFirstPayloadByteIndex(byte[] bytes) {
	int firstPayloadByte = 0;
	while (firstPayloadByte < bytes.length) {
	    if (firstPayloadByte > 3 && carriageReturnLineFeedTwoTimes(bytes, firstPayloadByte)) {
		firstPayloadByte++;
		break;
	    }
	    firstPayloadByte++;
	}
	return firstPayloadByte;
    }

    private static boolean carriageReturnLineFeedTwoTimes(byte[] bytes, int i) {
	byte CR = 13;
	byte LF = 10;
	return bytes[i - 3] == CR && bytes[i - 2] == LF && bytes[i - 1] == CR && bytes[i] == LF;
    }
}
