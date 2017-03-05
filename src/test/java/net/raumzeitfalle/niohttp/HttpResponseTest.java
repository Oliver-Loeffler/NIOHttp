package net.raumzeitfalle.niohttp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpResponseTest {

    private HttpResponse classUnderTest = new HttpResponseBuilder("HTTP/1.1")
            .withStatus("400")
            .withReasonPhrase("Bad Request")
            .servedBy("nginx/1.10.2")
            .deliveredAt("Sat, 04 Mar 2017 22:02:46 GMT")
            .withContentOfType("text/html")
            .contentLength(173)
            .withConnectionStatus("close")
            .withPayload(payload().getBytes())
            .build();

    @Test
    public void comparingStringRepresentations() {
	assertEquals(exectedStringResponse(), classUnderTest.toString());
    }

    @Test
    public void comparingByteArrayRepresentations() {
	assertArrayEquals(getExpectedByteSequence(),
	        classUnderTest.getBytes());
    }

    @Test
    public void fromBytes() {
	HttpResponse testCase = HttpResponse
	        .fromBytes(getExpectedByteSequence());

	assertEquals(exectedStringResponse(), testCase.toString());

    }

    private byte[] getExpectedByteSequence() {
	return exectedStringResponse().getBytes();
    }

    private String exectedStringResponse() {
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
	        .append("</html>")
	        .toString();
    }

    private String payload() {
	return new StringBuilder("<html>").append(CR)
	        .append("<head><title>400 Bad Request</title></head>")
	        .append(CR)
	        .append("<body bgcolor=\"white\">").append(CR)
	        .append("<center><h1>400 Bad Request</h1></center>").append(CR)
	        .append("<hr><center>nginx/1.10.2</center>").append(CR)
	        .append("</body>").append(CR)
	        .append("</html>").toString();
    }

    private static final String CR = "\r\n";
}
