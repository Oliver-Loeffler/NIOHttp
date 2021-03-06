package net.raumzeitfalle.niohttp.playground;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

import net.raumzeitfalle.niohttp.HttpResponse;
import net.raumzeitfalle.niohttp.HttpResponseReader;

public class StreamDemo {
    public static void main(String[] args) throws IOException {
	String address = "http://www.raumzeitfalle.de/";
	StreamDemo demo = new StreamDemo(address);

	demo.run(r -> {
	    System.out.println(r.responseHeader());

	    int payloadSize = r.getPayload().length;
	    String unit = "Byte";
	    if (payloadSize > 1024) {
		unit = "KiB";
		payloadSize /= 1024;
	    }

	    System.out.println("Payload consists of: " + payloadSize + unit);
	});
    }

    private final SocketAddress address;

    private final URL url;

    private int port;

    private String hostname;

    public StreamDemo(final String address) throws MalformedURLException {
	this.url = new URL(address);
	this.hostname = this.url.getHost();
	this.port = this.url.getDefaultPort();
	this.address = new InetSocketAddress(this.hostname, this.port);
    }

    public void run(Consumer<HttpResponse> consumer) throws IOException {
	System.out.println("Expecting a response after GET from: " + this.url.toString() + "\n\n");
	try (SocketChannel socketChannel = SocketChannel.open(this.address)) {
	    socketChannel.configureBlocking(true);
	    writeGetRequestTo(socketChannel);
	    HttpResponseReader.fromChannel(socketChannel).findFirst().ifPresent(consumer);

	}
	System.out.println("Demo end");
    }

    private void writeGetRequestTo(ByteChannel channel) throws IOException {

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
