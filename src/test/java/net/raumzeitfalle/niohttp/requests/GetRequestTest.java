package net.raumzeitfalle.niohttp.requests;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import net.raumzeitfalle.niohttp.header.Connection;
import net.raumzeitfalle.niohttp.header.UserAgent;

public class GetRequestTest {
    
    private GetRequest classUnderTest;

    @Test
    public void test() throws MalformedURLException {

	URL url = new URL("http", "www.raumzeitfalle.de", "/");
	UserAgent userAgent = new UserAgent("NIOHttp/0.1 Java");
	
	classUnderTest = new GetRequest(url, userAgent, Connection.close());
	
	assertEquals( simpleGetRequestString(url) , classUnderTest.get());
    }

    private String simpleGetRequestString(URL url) {
	return new StringBuilder("GET").append(SPACE)
                .append("/").append(SPACE).append("HTTP/1.1")
                .append(CRLF)
		.append("User-Agent: NIOHttp/0.1 Java")
                .append(CRLF)
                .append("Host:").append(SPACE).append(url.getHost())
                .append(CRLF)
                .append("Connection: close")
                .append(CRLF).append(CRLF)
                .toString();
    }

}
