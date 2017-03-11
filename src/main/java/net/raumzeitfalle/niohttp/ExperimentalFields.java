package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * Experimental header fields (or up to now: all header fields which are not
 * known as a type).
 *
 */
class ExperimentalFields implements HeaderField {

    private final String name;

    /**
     * @param lineFromBytes
     *            with field name and field values (e.g.
     *            x-non-standard-field-name : value)
     * @return object for header field which is not supported by HTTP by default
     * @throws RuntimeException
     *             when field name length is 0
     */
    public static ExperimentalFields fromLine(final String lineFromBytes) {
	return new ExperimentalFields(
		parseFieldNameFrom(Objects.requireNonNull(lineFromBytes, "line from HTTP message must not be null")));
    }

    /**
     * @param name
     *            of the new experimental field to be created
     * @throws RuntimeException
     *             when field name length is 0
     */
    public ExperimentalFields(final String name) {
	this.name = requireTrimmedNonZeroLength(Objects.requireNonNull(name));
    }

    public String getFieldName() {
	return this.name;
    }

    private static String parseFieldNameFrom(final String line) {
	int separation = line.indexOf(Constants.FIELD_VALUE_SEPARATOR);
	return line.substring(0, separation);
    }

    private String requireTrimmedNonZeroLength(final String fieldName) {
	if (fieldName.trim().length() > 0) {
	    return fieldName.trim();
	}
	throw new RuntimeException("HTTP message contained a line with a zero length field name.");
    }

}
