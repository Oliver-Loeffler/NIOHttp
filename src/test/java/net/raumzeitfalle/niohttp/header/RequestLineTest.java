package net.raumzeitfalle.niohttp.header;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import net.raumzeitfalle.niohttp.Constants;
import net.raumzeitfalle.niohttp.RequestType;

public class RequestLineTest {

    RequestLine classUnderTest;

    @Test
    public void httpGetAtRoot() throws MalformedURLException {
	classUnderTest = new RequestLine(RequestType.GET, new URL("http://www.raumzeitfalle.de"));
	assertEquals(exampleHeaderWithRootResource(), classUnderTest.get());
    }

    @Test
    public void httpGetWithResource() throws MalformedURLException {
	classUnderTest = new RequestLine(RequestType.GET, new URL("http://www.raumzeitfalle.de/page.html"));
	assertEquals(exampleHeaderWithResource(), classUnderTest.get());
    }

    private String exampleHeaderWithResource() {
	return "GET /page.html HTTP/1.1" + Constants.CRLF;
    }

    private String exampleHeaderWithRootResource() {
	return "GET / HTTP/1.1" + Constants.CRLF;
    }

}
