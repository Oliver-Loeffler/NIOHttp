package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntityFieldsTest {

    @Test
    public void isEntityField() {
	assertTrue(EntityFields.isEntityField("Content-Length:"));
    }

    @Test
    public void isNotEntityField() {
	assertFalse(EntityFields.isEntityField("Server:"));
    }

}
