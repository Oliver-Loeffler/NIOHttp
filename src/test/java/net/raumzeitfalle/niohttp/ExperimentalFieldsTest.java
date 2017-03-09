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

    @Test
    public void comparingExperimentalFields() {
	ExperimentalFields otherXfield = ExperimentalFields.fromLine(otherButDifferentExperimentalFieldValueLine());
	classUnderTest = ExperimentalFields.fromLine(experimentalFieldValueLine());
	assertTrue(classUnderTest.compareTo(otherXfield) < 0);
	assertTrue(otherXfield.compareTo(classUnderTest) > 0);
	assertTrue(classUnderTest.compareTo(classUnderTest) == 0);
    }

    private String unsupportedHttpField() {
	return "FieldNotPartOfHTTP";
    }

    private String otherUnsupportedHttpField() {
	return "OtherFieldNotPartOfHTTP";
    }

    private String separatedValueOfField() {
	return String.valueOf(new char[] { Constants.SPACE, Constants.FIELD_VALUE_SEPARATOR, Constants.SPACE })
		+ "valueExperimentalField"
		+ Constants.CRLF;
    }

    private String experimentalFieldValueLine() {
	return unsupportedHttpField() + separatedValueOfField();
    }

    private String otherButDifferentExperimentalFieldValueLine() {
	return otherUnsupportedHttpField() + separatedValueOfField();
    }

    private String lineWithZeroLengthFieldName() {
	return "   " + separatedValueOfField();
    }


}
