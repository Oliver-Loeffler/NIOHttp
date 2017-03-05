package net.raumzeitfalle.niohttp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;

import org.junit.Test;

/*
 * TODO: refine to enable testing w/o need for a socket channel.
 */
public class HttpResponseReaderTest {

    HttpResponseReader classUnderTest;

    @Test
    public void readFromByteBuffer() {
	classUnderTest = new HttpResponseReader(examplaryByteBuffer());

	HttpResponse test = classUnderTest.readFromBuffer();

	assertNotNull(test);
	assertEquals(exectedStringResponse(), test.toString());
	assertArrayEquals(expectedByteArray(), test.getBytes());

    }

    private ByteBuffer examplaryByteBuffer() {
	ByteBuffer buffer = ByteBuffer.allocate(expectedByteArray().length);
	buffer.put(expectedByteArray());
	return buffer;
    }

    private byte[] expectedByteArray() {
	return exectedStringResponse().getBytes();
    }

    private String exectedStringResponse() {
	String CR = "\r\n";
	return new StringBuilder("HTTP/1.1")
	        .append(" 400 Bad Request").append(CR)
	        .append("Server: nginx/1.10.2").append(CR)
	        .append("Date: Sat, 04 Mar 2017 22:02:46 GMT").append(CR)
	        .append("Content-Type: text/html").append(CR)
	        .append("Content-Length: 173").append(CR)
	        .append("Connection: close").append(CR).append(CR)
	        .append("<html>").append(CR)
	        .append("<head><title>400 Bad Request</title></head>")
	        .append(CR)
	        .append("<body bgcolor=\"white\">").append(CR)
	        .append("<center><h1>400 Bad Request</h1></center>").append(CR)
	        .append("<hr><center>nginx/1.10.2</center>").append(CR)
	        .append("</body>").append(CR)
	        .append("</html>").toString();
    }

}
