package net.raumzeitfalle.niohttp;

import static net.raumzeitfalle.niohttp.Constants.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

/**
 * Writes a HTTP request to www.raumzeitfalle.net/ using Java NIO SocketChannel.
 * When reachable, domain front page HTML content is delivered and partially
 * printed on System.out.<br>
 */
class Demo {

    public static void main(String args[]) throws IOException {

	String address = "http://www.raumzeitfalle.net/";
	Demo demo = new Demo(address);
	demo.run(r -> {
	    sysout(r.responseHeader().getBytes(), "Response Header");
	    sysout(r.getPayload(), "Response Payload");
	});

    }

    private final SocketAddress address;

    private final URL url;

    private int port;

    public Demo(final String address) throws MalformedURLException {
	this.url = checkAndFixURL(address);
	this.port = this.url.getDefaultPort();
	this.address = new InetSocketAddress(this.url.getHost(), this.port);
    }

    private URL checkAndFixURL(final String address) throws MalformedURLException {
	URL url = new URL(address);
	if (url.getPath().isEmpty()) {
	    return new URL(address + "/");
	}
	return url;
    }

    /**
     * Connects to given URL, writes a get request to URL and reads response.
     * 
     * @param responseConsumer
     * @throws IOException
     * @throws InterruptedException
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
                .append("Connection: Keep-Alive").append(CRLF).append(CRLF)
                .toString();
        // @formatter:on

	ByteBuffer sending = ByteBuffer.allocate(request.getBytes().length);
	sending.put(request.getBytes());
	sending.flip();
	channel.write(sending);
    }
}
