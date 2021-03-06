package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class GeneralFieldsParametrizedTest {

    private String messageFieldName;
    private GeneralFields expectation;

    public GeneralFieldsParametrizedTest(String fieldName, GeneralFields expected) {
	this.messageFieldName = fieldName;
	this.expectation = expected;
    }

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
	return Arrays.asList(new Object[][] { { "Cache-Control", GeneralFields.CACHE_CONTROL },
		{ "  CaChe-CONtrol  ", GeneralFields.CACHE_CONTROL }, { " Connection ", GeneralFields.CONNECTION },
		{ "Date", GeneralFields.DATETIME }, { "Pragma", GeneralFields.PRAGMA },
		{ "Trailer", GeneralFields.TRAILER }, { "Transfer-Encoding", GeneralFields.TRANSFER_ENCODING },
		{ "Upgrade", GeneralFields.UPGRADE }, { "Via", GeneralFields.VIA },
		{ "Warning", GeneralFields.WARNING } });
    }

    @Test
    public void fieldFromString() {
	assertEquals(this.expectation, GeneralFields.fromString(this.messageFieldName));
    }

}
