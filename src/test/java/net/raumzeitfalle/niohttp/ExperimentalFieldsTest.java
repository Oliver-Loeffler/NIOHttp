package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExperimentalFieldsTest {

    ExperimentalFields classUnderTest;

    @Test
    public void createExperimentalFieldFromSingleValuedLine() {
	classUnderTest = ExperimentalFields.fromLine(experimentalFieldValueLine());
	assertEquals("FieldNotPartOfHTTP", classUnderTest.getFieldName());
    }

    @Test(expected = RuntimeException.class)
    public void throwExceptionWhenFieldNameHasZeroChars() {
	classUnderTest = ExperimentalFields.fromLine(lineWithZeroLengthFieldName());
	assertEquals("FieldNotPartOfHTTP", classUnderTest.getFieldName());
    }

    private String unsupportedHttpField() {
	return "FieldNotPartOfHTTP";
    }

    private String separatedValueOfField() {
	return String.valueOf(new char[] { Constants.SPACE, Constants.FIELD_VALUE_SEPARATOR, Constants.SPACE })
		+ "valueExperimentalField"
		+ Constants.CRLF;
    }

    private String experimentalFieldValueLine() {
	return unsupportedHttpField() + separatedValueOfField();
    }

    private String lineWithZeroLengthFieldName() {
	return "   " + separatedValueOfField();
    }


}
