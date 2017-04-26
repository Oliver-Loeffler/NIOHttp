package net.raumzeitfalle.niohttp.header;

import static org.junit.Assert.*;

import org.junit.Test;

import net.raumzeitfalle.niohttp.Constants;
import net.raumzeitfalle.niohttp.header.UserAgent;

public class UserAgentTest {

    private UserAgent classUnderTest;

    @Test
    public void get() {
	classUnderTest = new UserAgent("NIOHttp");
	assertEquals("User-Agent: NIOHttp" + Constants.CRLF, classUnderTest.get());
    }

    @Test(expected = NullPointerException.class)
    public void nullArgument() {
	classUnderTest = new UserAgent(null);
    }

}
