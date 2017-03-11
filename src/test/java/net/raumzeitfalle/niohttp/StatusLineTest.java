package net.raumzeitfalle.niohttp;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StatusLineTest {

    StatusLine classUnderTest;

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWithEmptyMessage() {
	assertNotNull(new StatusLine(""));
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWithMissingProtocol() {
	assertNotNull(new StatusLine("400 Bad Request"));
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWithMissingStatusCode() {
	assertNotNull(new StatusLine("HTTP/1.1 Bad Request"));
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWithInvalidStatusCode() {
	assertNotNull(new StatusLine("HTTP/1.1 42 Bad Request"));
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionLeadingBadWhiteSpace() {
	assertNotNull(new StatusLine("\t" + badRequestStatus()));
    }

    @Test
    public void constructorWithSeparatedFields() {
	classUnderTest = new StatusLine("HTTP/1.1", "400", "Bad Request\r\nTest");
	assertEquals("HTTP/1.1", classUnderTest.protocolVersion());
	assertEquals(400, classUnderTest.statusCode());
	assertEquals("Bad Request", classUnderTest.reasonPhrase());
    }

    @Test(expected = HttpMessageParsingException.class)
    public void constructorWithSeparatedFields_statusCodeWithTooManyDigits() {
	new StatusLine("HTTP/1.1", "4000", "Bad Request");
    }

    @Test(expected = HttpMessageParsingException.class)
    public void constructorWithSeparatedFields_statusCodeWithTooFewDigits() {
	new StatusLine("HTTP/1.1", "40", "Bad Request");
    }

    @Test(expected = HttpMessageParsingException.class)
    public void constructorWithSeparatedFields_invalidReasonPhrase() {
	new StatusLine("HTTP/1.1", "400", "");
    }

    @Test(expected = HttpMessageParsingException.class)
    public void constructorWithSeparatedFields_invalidProtocol() {
	new StatusLine("\t\tHTTP/1.1", "400", "Bad Request");
    }

    @Test
    public void httpVersion() {
	classUnderTest = new StatusLine(badRequestStatus());
	assertEquals("HTTP/1.1", classUnderTest.protocolVersion());
    }

    @Test
    public void statusCode() {
	classUnderTest = new StatusLine(badRequestStatus());
	assertEquals(400, classUnderTest.statusCode());
    }

    @Test
    public void reasonPhrase() {
	classUnderTest = new StatusLine(badRequestStatus());
	assertEquals("Bad Request", classUnderTest.reasonPhrase());
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWithEmptyReasonPhrase() {
	new StatusLine(badRequestStatus_withEmptyReason());
    }

    @Test(expected = HttpMessageParsingException.class)
    public void exceptionWhenNoSpacesFound() {
	new StatusLine(badRequestStatus_withIncorrectSeparator());
    }

    @Test
    public void stringRepresentation() {
	classUnderTest = new StatusLine("HTTP/1.1", "400", "Bad Request");
	assertEquals(statusLine("HTTP/1.1", "400", "Bad Request"), classUnderTest.toString());
    }

    private String badRequestStatus() {
	return messageWithStatusLine("HTTP/1.1", "400", "Bad Request");
    }

    private String badRequestStatus_withEmptyReason() {
	return messageWithStatusLine("HTTP/1.1", "400", "");
    }

    private String badRequestStatus_withIncorrectSeparator() {
	return new StringBuilder("HTTP/1.1").append("\t").append("400").append("\t").append("BadRequest").append("\t")
		.append("headerFieldName:headerFieldValue").toString();
    }

    private String messageWithStatusLine(String protocol, String statusCode, String reasonPhrase) {
	return new StringBuilder("HTTP/1.1").append(Constants.SPACE).append(statusCode).append(Constants.SPACE)
		.append(reasonPhrase).append(CRLF).append("headerFieldName: headerFieldValue").toString();
    }

    private String statusLine(String protocol, String statusCode, String reasonPhrase) {
	return new StringBuilder("HTTP/1.1").append(Constants.SPACE).append(statusCode).append(Constants.SPACE)
		.append(reasonPhrase).append(CRLF).toString();
    }
}
