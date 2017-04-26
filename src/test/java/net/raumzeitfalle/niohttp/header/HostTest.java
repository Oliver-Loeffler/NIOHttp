package net.raumzeitfalle.niohttp.header;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import net.raumzeitfalle.niohttp.Constants;

public class HostTest {

    private Host classUnderTest;

    @Test
    public void hostWithoutPort() throws MalformedURLException {
	classUnderTest = new Host(new URL("http://www.raumzeitfalle.de/"));
	assertEquals("Host: www.raumzeitfalle.de" + Constants.CRLF, classUnderTest.get());
    }

    @Test
    public void hostWithPort() throws MalformedURLException {
	classUnderTest = new Host(new URL("http://www.raumzeitfalle.de:8080/"));
	assertEquals("Host: www.raumzeitfalle.de:8080" + Constants.CRLF, classUnderTest.get());
    }

}
