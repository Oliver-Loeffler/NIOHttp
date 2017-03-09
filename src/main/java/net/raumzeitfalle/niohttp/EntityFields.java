package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * HTTP 1.1 RFC2616 section 7.1 defines following fields for general
 * applicability:
 * <ul>
 * <li>Allow</li>
 * <li>Content-Encoding</li>
 * <li>Content-Language</li>
 * <li>Content-Length</li>
 * <li>Content-Location</li>
 * <li>Content-MD5</li>
 * <li>Content-Range</li>
 * <li>Content-Type</li>
 * <li>Expires</li>
 * <li>Last-Modified</li>
 * <li>extension-header</li>
 * </ul>
 *
 */
public enum EntityFields implements MessageField {
    ALLOW("Allow"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_MD5("Content-MD5"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_TYPE("Content-Type"),
    EXPIRES("Expires"),
    LAST_MODIFIED("Last-Modified"),
    EXTENSION_HEADER("extension-header");
    
    private final String fieldName;

    EntityFields(final String fieldName) {
	this.fieldName = fieldName;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public static EntityFields fromString(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (EntityFields entityField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(entityField.fieldName.toLowerCase())) {
		return entityField;
	    }
	}
	throw new RuntimeException("Unknown HTTP header entity field");
    }
}
