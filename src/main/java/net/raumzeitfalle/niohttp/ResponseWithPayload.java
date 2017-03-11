package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

class ResponseWithPayload implements HttpResponseTemplate {

    private final StatusLine statusLine;

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    private byte[] payload = new byte[0];

    public ResponseWithPayload(StatusLine statusLine) {
	this.statusLine = statusLine;
    }

    public ResponseWithPayload withPayload(byte[] payload) {
	this.payload = payload;
	return this;
    }

    @Override
    public boolean requiresPayload() {
	return true;
    }

    @Override
    public HttpResponseTemplate addMessageField(HeaderField field, String value) {
	this.responseFields.put(field, Objects.requireNonNull(value, "value assigned to field should not be null"));
	return this;
    }

    @Override
    public StatusLine getStatusLine() {
	return this.statusLine;
    }

    @Override
    public Map<HeaderField, String> getResponseFields() {
	return this.responseFields;
    }

    @Override
    public byte[] getPayload() {
	return this.payload;
    }

}
