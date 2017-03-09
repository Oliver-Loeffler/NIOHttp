package net.raumzeitfalle.niohttp;

import java.util.Objects;

class ExperimentalFields implements MessageField, Comparable<ExperimentalFields> {

    private final String name;

    /**
     * @param line
     *            with field name and field values (e.g.
     *            x-non-standard-field-name : value)
     * @throws RuntimeException
     *             when field name length is 0
     */
    public static ExperimentalFields fromLine(String line) {
	return new ExperimentalFields(
		parseFieldNameFrom(Objects.requireNonNull(line, "line from HTTP message must not be null")));
    }
    
    /**
     * @param name
     *            of the new experimental field to be created
     * @throws RuntimeException
     *             when field name length is 0
     */
    public ExperimentalFields(String name) {
	this.name = requireTrimmedNonZeroLength(Objects.requireNonNull(name));
    }

    public String getFieldName() {
	return this.name;
    }

    private static String parseFieldNameFrom(String line) {
	int separation = line.indexOf(Constants.FIELD_VALUE_SEPARATOR);
	return line.substring(0, separation);
    }

    private String requireTrimmedNonZeroLength(String fieldName) {
	if (fieldName.trim().length() > 0) {
	    return fieldName.trim();
	}
	throw new RuntimeException("HTTP message contained a line with a zero length field name.");
    }
    
    @Override
    public int compareTo(ExperimentalFields o) {
	return this.toString().toLowerCase().compareTo(o.toString().toLowerCase());
    }
}
