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
public class AuthenticationFieldsParametrizedTest {

    private String messageFieldName;
    private AuthenticationFields expectation;

    public AuthenticationFieldsParametrizedTest(String fieldName, AuthenticationFields expected) {
	this.messageFieldName = fieldName;
	this.expectation = expected;
    }

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
	return Arrays.asList(new Object[][] { { "Proxy-Authenticate", AuthenticationFields.PROXY_AUTHENTICATE },
		{ "WWW-Authenticate", AuthenticationFields.WWW_AUTHENTICATE } });
    }

    @Test
    public void fieldFromString() {
	assertThat(AuthenticationFields.fromString(this.messageFieldName), is(this.expectation));
    }

}
