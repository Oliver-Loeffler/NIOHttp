package net.raumzeitfalle.niohttp;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ResponseFieldsParametrizedTest {

    private String messageFieldName;
    private ResponseFields expectation;

    public ResponseFieldsParametrizedTest(String fieldName, ResponseFields expected) {
	this.messageFieldName = fieldName;
	this.expectation = expected;
    }

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
	return Arrays.asList(new Object[][] { { "Accept-Ranges", ResponseFields.ACCEPT_RANGES },
		{ "  aGE  ", ResponseFields.AGE }, { " eTaG ", ResponseFields.ETAG },
		{ "Location", ResponseFields.LOCATION }, { "Proxy-Authenticate", ResponseFields.PROXY_AUTHENTICATE },
		{ "Retry-After", ResponseFields.RETRY_AFTER }, { "Server", ResponseFields.SERVER },
		{ "Vary", ResponseFields.VARY }, { "WWW-Authenticate", ResponseFields.WWW_AUTHENTICATE } });
    }

    @Test
    public void fieldFromString() {
	assertThat(ResponseFields.fromString(this.messageFieldName), is(this.expectation));
    }

}
