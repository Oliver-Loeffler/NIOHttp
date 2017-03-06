package net.raumzeitfalle.niohttp;

import org.junit.Test;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
                .append("</html>")
                .toString();
    }

    private String payload() {
        return new StringBuilder("<html>").append(CRLF)
                .append("<head><title>400 Bad Request</title></head>")
                .append(CRLF)
                .append("<body bgcolor=\"white\">").append(CRLF)
                .append("<center><h1>400 Bad Request</h1></center>").append(CRLF)
                .append("<hr><center>nginx/1.10.2</center>").append(CRLF)
                .append("</body>").append(CRLF)
                .append("</html>").toString();
    }

}
