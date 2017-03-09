package net.raumzeitfalle.niohttp;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class EntityFieldsParametrizedTest {

    private String messageFieldName;
    private EntityFields expectation;

    public EntityFieldsParametrizedTest(String fieldName, EntityFields expected) {
	this.messageFieldName = fieldName;
	this.expectation = expected;
    }

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> data() {
	return Arrays.asList(new Object[][] { { "Allow", EntityFields.ALLOW },
		{ "   Content-Encoding ", EntityFields.CONTENT_ENCODING },
		{ " cONtEnT-ENCODing ", EntityFields.CONTENT_ENCODING },
		{ "Content-Encoding", EntityFields.CONTENT_ENCODING }, { "Content-Language", EntityFields.CONTENT_LANGUAGE },
		{ "Content-Length", EntityFields.CONTENT_LENGTH }, { "Content-Location", EntityFields.CONTENT_LOCATION },
		{ "Content-MD5", EntityFields.CONTENT_MD5 }, { "Content-Range", EntityFields.CONTENT_RANGE },
		{ "Content-Type", EntityFields.CONTENT_TYPE }, { "Expires", EntityFields.EXPIRES },
		{ "Last-Modified", EntityFields.LAST_MODIFIED }, { "extension-header", EntityFields.EXTENSION_HEADER } });
    }

    @Test
    public void test_addTwoNumbes() {
	assertThat(EntityFields.fromString(this.messageFieldName), is(this.expectation));
    }

}
