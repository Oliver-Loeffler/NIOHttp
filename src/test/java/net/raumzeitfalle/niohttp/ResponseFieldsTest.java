package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseFieldsTest {

    @Test
    public void isResponseField() {
	assertTrue(ResponseFields.isResponseField("Server:"));
    }

    @Test
    public void isNotResponseField() {
	assertFalse(ResponseFields.isResponseField("X-Field:"));
    }

}
