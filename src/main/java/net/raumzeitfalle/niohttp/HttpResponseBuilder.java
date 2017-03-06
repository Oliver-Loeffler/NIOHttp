package net.raumzeitfalle.niohttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

class HttpResponseBuilder {

    private final Map<ResponseStatus, String> responseStatus = new HashMap<>(3);

    private final Map<GeneralResponseEntity, String> responseFields = new TreeMap<>();

	private byte[] payload = new byte[0];

    /**
     * With only protocol version given, a bad request response with status code
     * 400 is created. No additional {@link GeneralResponseEntity} properties
     * are set.
     * 
     * @param protocolVersion
     */
    public HttpResponseBuilder(String protocolVersion) {
	this.responseStatus.put(ResponseStatus.PROTOCOL_VERSION,
	        protocolVersion);
	this.responseStatus.put(ResponseStatus.STATUS_CODE, "400");
	this.responseStatus.put(ResponseStatus.REASON_PHRASE, "Bad Request");
    }

    public HttpResponseBuilder withStatus(String statusCode) {
	this.responseStatus.put(ResponseStatus.STATUS_CODE, statusCode);
	return this;
    }

    public HttpResponseBuilder withReasonPhrase(String reasonPhrase) {
	this.responseStatus.put(ResponseStatus.REASON_PHRASE, reasonPhrase);
	return this;
    }

    public HttpResponseBuilder servedBy(String server) {
	this.responseFields.put(GeneralResponseEntity.SERVER, server);
	return this;
    }

    public HttpResponseBuilder deliveredAt(String date) {
	this.responseFields.put(GeneralResponseEntity.DATETIME, date);
	return this;
    }

    public HttpResponseBuilder withContentOfType(String contentType) {
	this.responseFields.put(GeneralResponseEntity.CONTENT_TYPE,
	        contentType);
	return this;
    }

    public HttpResponseBuilder contentLength(long contentLength) {
	this.responseFields.put(GeneralResponseEntity.CONTENT_LENGTH,
	        String.valueOf(contentLength));
	return this;
    }

    public HttpResponseBuilder withConnectionStatus(String connectionStatus) {
	this.responseFields.put(GeneralResponseEntity.CONNECTION_STATUS,
	        connectionStatus);
	return this;
    }

    public HttpResponseBuilder lastModified(String lastModified) {
	this.responseFields.put(GeneralResponseEntity.LAST_MODIFIED,
	        lastModified);
	return this;
    }

    public HttpResponseBuilder withEtag(String eTag) {
	this.responseFields.put(GeneralResponseEntity.ETAG,
	        eTag);
	return this;
    }

    public HttpResponseBuilder acceptRanges(String acceptRanges) {
	this.responseFields.put(GeneralResponseEntity.ACCEPT_RANGES,
	        acceptRanges);
	return this;
    }

    public HttpResponseBuilder vary(String vary) {
	this.responseFields.put(GeneralResponseEntity.VARY,
	        vary);
	return this;
    }

    public HttpResponseBuilder xPoweredBy(String xPoweredBy) {
	this.responseFields.put(GeneralResponseEntity.X_POWERED_BY,
	        xPoweredBy);
	return this;
    }

    public HttpResponseBuilder keepAlive(String keepAlive) {
	this.responseFields.put(GeneralResponseEntity.KEEP_ALIVE,
	        keepAlive);
	return this;
    }

    public HttpResponseBuilder withPayload(byte[] payload) {
	this.payload = payload;
	return this;
    }

    public HttpResponse build() {
	HttpResponse response = new HttpResponse(protocolVersionText(),
	        statusCode(), reasonPhrase(), payload);

	for (Entry<GeneralResponseEntity, String> e : responseFields
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
