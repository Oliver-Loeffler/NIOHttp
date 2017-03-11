package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * Message header fields of general applicability:
 * <ul>
 * <li>Cache-Control</li>
 * <li>Connection</li>
 * <li>Date</li>
 * <li>Pragma</li>
 * <li>Trailer</li>
 * <li>Transfer-Encoding</li>
 * <li>Upgrade</li>
 * <li>Via</li>
 * <li>Warning</li>
 * </ul>
 *
 */
public enum GeneralFields implements HeaderField {
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    DATETIME("Date"),
    PRAGMA("Pragma"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    UPGRADE("Upgrade"),
    VIA("Via"),
    WARNING("Warning");

    private final String fieldName;

    GeneralFields(final String fieldName) {
	this.fieldName = fieldName;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public static GeneralFields fromString(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (GeneralFields generalField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(generalField.fieldName.toLowerCase())) {
		return generalField;
	    }
	}
	throw new RuntimeException("Unknown HTTP header general field");
    }

    public static boolean isGeneralField(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	for (GeneralFields generalField : values()) {
	    if (lineFromBytes.trim().toLowerCase().startsWith(generalField.fieldName.toLowerCase())) {
		return true;
	    }
	}
	return false;
    }
}
