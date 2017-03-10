package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * HTTP 1.1 RFC2616 section 6 defines following fields to provide additional
 * details about the response:
 * <ul>
 * <li>Accept-Ranges</li>
 * <li>Age</li>
 * <li>ETag</li>
 * <li>Location</li>
 * <li>Proxy-Authenticate</li>
 * <li>Retry-After</li>
 * <li>Server</li>
 * <li>Vary</li>
 * <li>WWW-Authenticate</li>
 * </ul>
 *
 */
public enum ResponseFields implements MessageField {
    ACCEPT_RANGES("Accept-Ranges"),
    AGE("Age"),
    ETAG("ETag"),
    LOCATION("LOCATION"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    RETRY_AFTER("Retry-After"),
    SERVER("Server"),
    VARY("Vary"),
    WWW_AUTHENTICATE("WWW-Authenticate");

    private final String fieldName;

    ResponseFields(final String fieldName) {
	this.fieldName = fieldName;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public static ResponseFields fromString(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (ResponseFields responseField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(responseField.fieldName.toLowerCase())) {
		return responseField;
	    }
	}
	throw new RuntimeException("Unknown HTTP header response field");
    }

    public static boolean isResponseField(final String lineFromBytes) {
	for (ResponseFields responseField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(responseField.fieldName.toLowerCase())) {
		return true;
	    }
	}
	return false;
    }
}
