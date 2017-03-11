package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * Authentication specific message header fields.
 * 
 * @see <a href=
 *      "http://httpwg.org/specs/rfc7231.html#response.auth">http://httpwg.org/specs/rfc7231.html#response.auth</a>
 *      <ul>
 *      <li>Proxy-Authenticate</li>
 *      <li>WWW-Authenticate</li>
 *      </ul>
 *
 */
public enum AuthenticationFields implements HeaderField {
    PROXY_AUTHENTICATE("Proxy-Authenticate"), WWW_AUTHENTICATE("WWW-Authenticate");

    private final String fieldName;

    AuthenticationFields(final String fieldName) {
	this.fieldName = fieldName;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public static AuthenticationFields fromString(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (AuthenticationFields responseField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(responseField.fieldName.toLowerCase())) {
		return responseField;
	    }
	}
	throw new RuntimeException("Unknown HTTP header response field");
    }

    public static boolean isResponseField(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (AuthenticationFields responseField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(responseField.fieldName.toLowerCase())) {
		return true;
	    }
	}
	return false;
    }
}
