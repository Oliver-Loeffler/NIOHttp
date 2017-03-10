package net.raumzeitfalle.niohttp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.Consumer;

class HttpResponseReader {

    private final ReadableByteChannel channel;

    private byte[] bytesRead = new byte[0];

    public HttpResponseReader(final ReadableByteChannel channel) {
	this.channel = Objects.requireNonNull(channel, "channel should not be null");
    }

    public void read(final Consumer<HttpResponse> responseConsumer) throws IOException {
	Objects.requireNonNull(responseConsumer, "responseConsumer should not be null");
	while (this.channel.isOpen()) {
	    readBytesFromChannel();
	    HttpResponse response = HttpResponse.fromBytes(this.bytesRead);
	    responseConsumer.accept(response);
	}
    }

    private void readBytesFromChannel() throws IOException {
	this.bytesRead = new byte[0];
	int numberOfBytesRead = 0;
	int byteBufferSize = 48;
	ByteBuffer inputBuffer = ByteBuffer.allocate(byteBufferSize);
	do {
	    inputBuffer.clear();
	    numberOfBytesRead = channel.read(inputBuffer);
	    if (numberOfBytesRead > -1) {
		this.bytesRead = mergeArrays(this.bytesRead, inputBuffer.array(), numberOfBytesRead);
	    }
	} while (numberOfBytesRead <= byteBufferSize && numberOfBytesRead > -1);
    }

    /**
     * @return bytes read from buffer
     */
    protected byte[] getBytesRead() {
	return this.bytesRead;
    }

    private byte[] mergeArrays(byte[] oldBytes, byte[] newBytes, int numberOfNewBytesToConsider) {
	assert (numberOfNewBytesToConsider <= newBytes.length);
	byte[] result = new byte[oldBytes.length + numberOfNewBytesToConsider];
	System.arraycopy(oldBytes, 0, result, 0, oldBytes.length);
	System.arraycopy(newBytes, 0, result, oldBytes.length, numberOfNewBytesToConsider);
	return result;
    }
}
