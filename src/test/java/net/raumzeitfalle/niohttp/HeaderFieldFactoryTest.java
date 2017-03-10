package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;


import org.junit.Test;

public class HeaderFieldFactoryTest {

    HeaderFieldFactory classUnderTest = HeaderFieldFactory.getInstance();

    @Test
    public void ensureInstanceIsNotNull() {
	assertNotNull(classUnderTest);
    }

    @Test
    public void createEntityFieldFromLine() {
	HeaderField entityField = classUnderTest.fromString(entityKeyValueExample());
	assertEquals(EntityFields.CONTENT_RANGE, entityField);
    }

    @Test
    public void createGeneralFieldFromLine() {
	HeaderField generalField = classUnderTest.fromString(generalKeyValueExample());
	assertEquals(GeneralFields.TRANSFER_ENCODING, generalField);
    }

    @Test
    public void createResponseFieldFromLine() {
	HeaderField responseField = classUnderTest.fromString(responseKeyValueExample());
	assertEquals(ResponseFields.WWW_AUTHENTICATE, responseField);
    }

    @Test
    public void creareExperimentalFieldFromLine() {
	HeaderField experimentalField = classUnderTest.fromString(experimentalKeyValueExample());
	assertEquals("X-Unsupported-Feature", experimentalField.getFieldName());
    }

    private String experimentalKeyValueExample() {
	return "X-Unsupported-Feature" + separatedFieldValue();
    }

    private String entityKeyValueExample() {
	return "Content-Range" + separatedFieldValue();
    }

    private String generalKeyValueExample() {
	return "Transfer-Encoding" + separatedFieldValue();
    }

    private String responseKeyValueExample() {
	return "WWW-Authenticate" + separatedFieldValue();
    }

    private String separatedFieldValue() {
	return String.valueOf(new char[] { Constants.SPACE, Constants.FIELD_VALUE_SEPARATOR, Constants.SPACE })
		+ "fromHereToInfinity" + Constants.CRLF;
    }

}
