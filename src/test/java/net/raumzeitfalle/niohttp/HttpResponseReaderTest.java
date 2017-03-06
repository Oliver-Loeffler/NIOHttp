package net.raumzeitfalle.niohttp;

import org.junit.Test;

import java.nio.ByteBuffer;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
 * TODO: refine to enable testing w/o need for a socket channel.
 */
public class HttpResponseReaderTest {

    private HttpResponseReader classUnderTest;

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
        return new StringBuilder("HTTP/1.1")
                .append(" 400 Bad Request").append(CRLF)
                .append("Server: nginx/1.10.2").append(CRLF)
                .append("Date: Sat, 04 Mar 2017 22:02:46 GMT").append(CRLF)
                .append("Content-Type: text/html").append(CRLF)
                .append("Content-Length: 173").append(CRLF)
                .append("Connection: close").append(CRLF).append(CRLF)
                .append("<html>").append(CRLF)
                .append("<head><title>400 Bad Request</title></head>")
                .append(CRLF)
                .append("<body bgcolor=\"white\">").append(CRLF)
                .append("<center><h1>400 Bad Request</h1></center>").append(CRLF)
                .append("<hr><center>nginx/1.10.2</center>").append(CRLF)
                .append("</body>").append(CRLF)
                .append("</html>").toString();
    }

}
