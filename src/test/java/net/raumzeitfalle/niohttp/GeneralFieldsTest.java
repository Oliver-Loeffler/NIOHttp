package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneralFieldsTest {

    @Test
    public void isGeneralField() {
	assertTrue(GeneralFields.isGeneralField("Cache-Control:"));
    }

    @Test
    public void isNotGeneralField() {
	assertFalse(GeneralFields.isGeneralField("Server:"));
    }

}
