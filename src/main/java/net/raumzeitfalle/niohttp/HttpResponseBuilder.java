package net.raumzeitfalle.niohttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.TreeMap;

class HttpResponseBuilder {

    private final Map<ResponseStatus, String> responseStatus = new HashMap<>(3);

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    private byte[] payload = new byte[0];

    /**
     * With only protocol version given, a bad request response with status code
     * 400 is created. No additional {@link GeneralResponseEntity} properties
     * are set.
     * 
     * @param protocolVersion
     */
    public HttpResponseBuilder(String protocolVersion) {
	addStatusParameter(ResponseStatus.PROTOCOL_VERSION, protocolVersion);
	addStatusParameter(ResponseStatus.STATUS_CODE, "400");
	addStatusParameter(ResponseStatus.REASON_PHRASE, "Bad Request");
    }

    public HttpResponseBuilder withStatus(String statusCode) {
	addStatusParameter(ResponseStatus.STATUS_CODE, statusCode);
	return this;
    }

    public HttpResponseBuilder withReasonPhrase(String reasonPhrase) {
	addStatusParameter(ResponseStatus.REASON_PHRASE, reasonPhrase);
	return this;
    }

    public HttpResponseBuilder servedBy(String server) {
	addFieldWithValue(ResponseFields.SERVER, server);
	return this;
    }

    public HttpResponseBuilder deliveredAt(String date) {
	addFieldWithValue(GeneralFields.DATETIME, date);
	return this;
    }

    public HttpResponseBuilder withContentOfType(String contentType) {
	addFieldWithValue(EntityFields.CONTENT_TYPE, contentType);
	return this;
    }

    public HttpResponseBuilder contentLength(long contentLength) {
	addFieldWithValue(EntityFields.CONTENT_LENGTH, String.valueOf(contentLength));
	return this;
    }

    public HttpResponseBuilder withConnectionStatus(String connection) {
	addFieldWithValue(GeneralFields.CONNECTION, connection);
	return this;
    }

    public HttpResponseBuilder lastModified(String lastModified) {
	addFieldWithValue(EntityFields.LAST_MODIFIED, lastModified);
	return this;
    }

    public HttpResponseBuilder withEtag(String entityTag) {
	addFieldWithValue(ResponseFields.ETAG, entityTag);
	return this;
    }

    public HttpResponseBuilder acceptRanges(String acceptRanges) {
	addFieldWithValue(ResponseFields.ACCEPT_RANGES, acceptRanges);
	return this;
    }

    public HttpResponseBuilder vary(String vary) {
	addFieldWithValue(ResponseFields.VARY, vary);
	return this;
    }


    public HttpResponseBuilder withPayload(byte[] payload) {
	this.payload = payload;
	return this;
    }

    public HttpResponseBuilder addMessageField(HeaderField field, String value) {
	addFieldWithValue(field, value);
	return this;
    }

    private void addFieldWithValue(final HeaderField field, final String value) {
	this.responseFields.put(field, Objects.requireNonNull(value, "value assigned to field should not be null"));
    }

    private void addStatusParameter(final ResponseStatus parameter, final String value) {
	this.responseStatus.put(parameter, Objects.requireNonNull(value, "value assigned to field should not be null"));
    }

    public HttpResponse build() {
	HttpResponse response = new HttpResponse(protocolVersionText(),
	        statusCode(), reasonPhrase(), payload);

	for (Entry<HeaderField, String> e : responseFields
	        .entrySet()) {
	    response.addResponseFieldWithValue(e.getKey(), e.getValue());
	}
	return response;
    }

    private String reasonPhrase() {
	return this.responseStatus.get(ResponseStatus.REASON_PHRASE);
    }

    private String statusCode() {
	return this.responseStatus.get(ResponseStatus.STATUS_CODE);
    }

    private String protocolVersionText() {
	return this.responseStatus.get(ResponseStatus.PROTOCOL_VERSION);
    }

}
