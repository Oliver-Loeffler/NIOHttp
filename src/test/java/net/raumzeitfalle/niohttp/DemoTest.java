package net.raumzeitfalle.niohttp;

/**
 * Requires a web server near by, will issue a bad request. Then a HttpResponse
 * object is created and printed to System.out. Then the HttpResponse is turned
 * into a byte[] and compared to the actual response byte[].
 * 
 * @author oliver
 *
 */
public class DemoTest {

    /*
    Tests runs against an internal IP adress. This can not work by for example a Travis build
    @Test
    public void demo() throws IOException {

	HttpResponse response = null;
	byte[] bytesFromServer = null;

	try (SocketChannel socketChannel = SocketChannel.open()) {
	    socketChannel.connect(new InetSocketAddress("192.168.1.20", 80));

	    Demo demo = new Demo(socketChannel);
	    response = demo.read();
	    bytesFromServer = demo.getResponseBytes();
	}

	assertNotNull(response);
	assertEquals(new String(bytesFromServer), response.toString());

    }*/

}
