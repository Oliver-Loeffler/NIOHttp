package net.raumzeitfalle.niohttp.header;

import static org.junit.Assert.*;

import org.junit.Test;

import net.raumzeitfalle.niohttp.Constants;
import net.raumzeitfalle.niohttp.header.Connection;

public class ConnectionTest {

    private Connection classUnderTest;

    @Test
    public void close() {
	classUnderTest = Connection.close();
	assertEquals("Connection: close" + Constants.CRLF, classUnderTest.get());
    }

    @Test
    public void keepAlive() {
	classUnderTest = Connection.keepAlive();
	assertEquals("Connection: keep-alive" + Constants.CRLF, classUnderTest.get());
    }

}
