package net.raumzeitfalle.niohttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Requires a web server near by, will issue a bad request. Then a HttpResponse
 * object is created and printed to System.out. The bad request works currently
 * only on my Nginx 1.10.2 running on a Raspberry Pi.
 * 
 * @author oliver
 *
 */
class Demo {

    public static void main(String args[]) throws IOException {

	String serverIp = "192.168.1.20";
	int httpPort = 80;

	try (SocketChannel socketChannel = SocketChannel.open()) {
	    socketChannel.connect(new InetSocketAddress(serverIp, httpPort));
	    Demo demo = new Demo(socketChannel);
	    HttpResponse response = demo.read();
	    sysout(demo.getResponseBytes(),
	            "Response (converted to String) received from Server");
	    sysout(response.getBytes(), "HttpResponse object content");
	}
    }

    private static void sysout(byte[] bytes, String title) {
	StringBuilder out = new StringBuilder(title)
	        .append(System.lineSeparator())
	        .append("----------------------------------------------------")
	        .append(System.lineSeparator())
	        .append(new String(bytes)).append(System.lineSeparator());
	System.out.println(out);
    }

    private SocketChannel socketChannel;

    private byte[] responseBytes;

    public Demo(SocketChannel socketChannel) {
	this.socketChannel = socketChannel;

	try {
	    simpleButWrongRequest();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private void simpleButWrongRequest() throws IOException {
	String request = "/test.html";
	ByteBuffer sending = ByteBuffer
	        .allocateDirect(request.getBytes().length);
	sending.put(request.getBytes());
	sending.flip();

	socketChannel.write(sending);
    }

    public HttpResponse read() throws IOException {
	ByteBuffer receive = ByteBuffer.allocateDirect(1024);
	receive.clear();

	int numBytesRead = socketChannel.read(receive);

	if (numBytesRead == -1) {
	    socketChannel.close();
	} else {
	    HttpResponseReader responseReader = new HttpResponseReader(receive);
	    HttpResponse response = responseReader.readFromBuffer();
	    System.out.println(response.toString());
	    this.responseBytes = responseReader.getBytesRead();
	    return response;
	}
	throw new IOException(
	        "Failed too convice server to respond properly, or server sent empty response.");
    }

    /**
     * @return bytes from server as received.
     */
    protected byte[] getResponseBytes() {
	return this.responseBytes;
    }
}
