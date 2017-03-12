package net.raumzeitfalle.niohttp.playground;

import static net.raumzeitfalle.niohttp.Constants.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import net.raumzeitfalle.niohttp.HttpResponse;
import net.raumzeitfalle.niohttp.HttpResponseReader;

/**
 * Writes a HTTP request to www.raumzeitfalle.net/ using Java NIO SocketChannel.
 * When reachable, domain front page HTML content is delivered and partially
 * printed on System.out.<br>
 */
class Demo {

    public static void main(String args[]) throws IOException {

	String address = "http://www.raumzeitfalle.de/";
	Demo demo = new Demo(address);
	demo.run(r -> {
	    sysout(r.responseHeader().getBytes(), "Response Header");
	    sysout(r.getPayload(), "Response Payload");
	    writeToFile(r.responseHeader().getBytes(), "header.txt");
	    writeToFile(r.getPayload(), "payload.txt");
	});

	// Server actually forwards to HTTPS, the response has no message body,
	// no payload
	String addressWhichWillCauseException = "http://de.wikipedia.org/wiki/Byte_Order_Mark";
	demo = new Demo(addressWhichWillCauseException);
	demo.run(r -> {
	    sysout(r.responseHeader().getBytes(), "Response Header");

	});
    }

    private final SocketAddress address;

    private final URL url;

    private int port;

    public Demo(final String address) throws MalformedURLException {
	this.url = new URL(address);
	this.port = this.url.getDefaultPort();
	this.address = new InetSocketAddress(this.url.getHost(), this.port);
    }

    /**
     * Connects to given URL, writes a get request to URL and reads response.
     * 
     * @param responseConsumer
     *            Consumer for HttpResponses
     * @throws IOException
     *             in case of any kind of connection error, an IOException is
     *             thrown
     */
    public void run(Consumer<HttpResponse> responseConsumer) throws IOException {
	try (SocketChannel socketChannel = SocketChannel.open(this.address)) {
	    socketChannel.configureBlocking(true);
	    writeGetRequestToChannel(socketChannel);
	    readResponseFromChannel(socketChannel, responseConsumer);
	}
    }

    private void readResponseFromChannel(SocketChannel channel, Consumer<HttpResponse> responseConsumer)
	    throws IOException {

	HttpResponseReader reader = new HttpResponseReader(channel);
	reader.read(responseConsumer);
    }

    private static void sysout(byte[] bytes, String title) {
	StringBuilder out = new StringBuilder(title).append(System.lineSeparator());
	for (int i = 0; i < title.toCharArray().length; i++) {
	    out.append('-');
	}

	out.append(System.lineSeparator()).append(new String(bytes)).append(System.lineSeparator())
		.append(System.lineSeparator()).append(System.lineSeparator());
	System.out.println(out.toString());
    }

    private static void writeToFile(byte[] payload, String filename) {
	Path path = Paths.get(filename);
	try {
	    Files.write(path, payload);
	} catch (IOException e) {
	    System.err.println("Could not write payload to file: " + path.toAbsolutePath().toString());
	}
    }

    private void writeGetRequestToChannel(ByteChannel channel) throws IOException {

	// @formatter:off
        String request = new StringBuilder("GET").append(SPACE)
                .append(this.url.getPath()).append(SPACE).append("HTTP/1.1")
                .append(CRLF)
		.append("User-Agent: NIOHttp/0.1 Java")
                .append(CRLF)
                .append("Host:").append(SPACE).append(url.getHost())
                .append(CRLF)
                .append("Accept-Language: en-us").append(CRLF)
                .append("Connection: close").append(CRLF).append(CRLF)
                .toString();
        // @formatter:on

	ByteBuffer sending = ByteBuffer.allocate(request.getBytes().length);
	sending.put(request.getBytes());
	sending.flip();
	channel.write(sending);
    }
}
