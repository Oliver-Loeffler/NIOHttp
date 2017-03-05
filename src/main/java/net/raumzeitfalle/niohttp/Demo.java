package net.raumzeitfalle.niohttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Requires a web server near by, will issue a bad request. Then a HttpResponse
 * object is created and printed to System.out.
 * 
 * @author oliver
 *
 */
class Demo {

    public static void main(String args[]) throws IOException {
	try (SocketChannel socketChannel = SocketChannel.open()) {
	    socketChannel.connect(new InetSocketAddress("192.168.1.20", 80));

	    Demo demo = new Demo(socketChannel);
	    HttpResponse response = demo.read();
	    System.out.println(
	            "Response (converted to String) received from Server");
	    System.out.println(
	            "----------------------------------------------------");
	    System.out.println(new String(demo.responseBytes));

	    System.out.println(
	            "\ntoString() method of HttpResponse object called");
	    System.out.println(
	            "----------------------------------------------------");
	    System.out.println(response.toString());
	}
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
	String request = "/";
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
	throw new IOException("Failed too convice server to respond");
    }

    /**
     * @return bytes from server as received.
     */
    protected byte[] getResponseBytes() {
	return this.responseBytes;
    }
}
