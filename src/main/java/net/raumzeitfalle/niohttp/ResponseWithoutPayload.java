package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

class ResponseWithoutPayload implements HttpResponseTemplate {

    private final StatusLine statusLine;

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    public ResponseWithoutPayload(StatusLine statusLine) {
	this.statusLine = statusLine;
    }

    @Override
    public boolean requiresPayload() {
	return false;
    }

    @Override
    public HttpResponseTemplate addMessageField(HeaderField field, String value) {
	this.responseFields.put(field, Objects.requireNonNull(value, "value assigned to field should not be null"));
	return this;
    }

    /**
     * As this template requires no payload, the payload parameter is ignore.
     * 
     * @param payload
     *            byte array from channel
     */
    @Override
    public HttpResponseTemplate withPayload(byte[] payload) {
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
	return new byte[0];
    }

}
