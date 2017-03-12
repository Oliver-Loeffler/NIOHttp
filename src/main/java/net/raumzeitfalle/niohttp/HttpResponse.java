package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import static net.raumzeitfalle.niohttp.Constants.CRLF;

public class HttpResponse {

    private final StatusLine statusLine;

    private final byte[] payload;

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    protected HttpResponse(final StatusLine statusLine, final byte[] payload) {
	this.statusLine = Objects.requireNonNull(statusLine, "statusLine must nut be null");
	this.payload = Objects.requireNonNull(payload, "payload should not be null");
    }

    protected HttpResponse(final String protocolVersion, final String statusCode, final String reasonPhrase,
	    final byte[] payload) {

	this.statusLine = new StatusLine(Objects.requireNonNull(protocolVersion, "protocolVersion should not be null"),
		Objects.requireNonNull(statusCode, "statusCode should not be null"),
		Objects.requireNonNull(reasonPhrase, "reasonPhrase should not be null"));

	this.payload = Objects.requireNonNull(payload, "payload should not be null");
    }

    public byte[] getPayload() {
	return this.payload;
    }

    @Override
    public String toString() {
	StringBuilder b = new StringBuilder(responseHeader());

	if (this.payload.length > 0) {
	    b.append(new String(payload));
	}

	return b.toString();
    }

    /**
     * @return String representation of response header
     */
    public String responseHeader() {
	StringBuilder b = new StringBuilder(this.statusLine.toString());
	for (Entry<HeaderField, String> e : responseFields.entrySet()) {
	    addResponseFieldContent(b, e.getKey());
	}
	b.append(CRLF);
	return b.toString();
    }

    public byte[] getBytes() {
	return toString().getBytes();
    }

    protected void addResponseFieldWithValue(final HeaderField field, final String value) {
	this.responseFields.put(field, Objects.requireNonNull(value, "value assigned to a field must not be null"));
    }

    private void addResponseFieldContent(StringBuilder b, HeaderField responseField) {
	Objects.requireNonNull(b, "String builder should not be null");
	Objects.requireNonNull(responseField, "responseField builder should not be null");
	if (this.responseFields.containsKey(responseField)) {
	    b.append(responseField.getFieldName()).append(": ").append(this.responseFields.get(responseField))
		    .append(CRLF);
	}
    }

}
