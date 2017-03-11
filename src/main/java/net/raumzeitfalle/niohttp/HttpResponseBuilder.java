package net.raumzeitfalle.niohttp;

import java.util.Objects;

/**
 * Creates HttpResponses based on templates which are selected depending on
 * status code. Currently there are 2 templates where payload is either required
 * or not.
 * 
 * @author oliver
 *
 */
public class HttpResponseBuilder {

    private final StatusLine statusLine;

    private final HttpResponseTemplate template;

    public HttpResponseBuilder(String protocol, int code, String responsePhrase) {
	this(new StatusLine(protocol, String.valueOf(code), responsePhrase));
    }

    public HttpResponseBuilder(String messageLine) {
	this(new StatusLine(messageLine));
    }

    public HttpResponseBuilder(StatusLine statusLine) {
	Objects.requireNonNull(statusLine, "message line to be read must not be null");
	this.statusLine = statusLine;
	this.template = selectAppropriateTemplate();
    }

    public HttpResponseBuilder addMessageField(HeaderField field, String value) {
	addFieldWithValue(field, value);
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

    public HttpResponseBuilder withPayload(byte[] bytes) {
	this.template.withPayload(bytes);
	return this;
    }

    public HttpResponse build() {
	return this.template.build();
    }

    public boolean requiresPayload() {
	return this.template.requiresPayload();
    }

    private HttpResponseTemplate selectAppropriateTemplate() {
	int code = statusLine.statusCode();
	if ((code >= 100 && code < 200) || code == 204 || code == 304) {
	    return new ResponseWithoutPayload(statusLine);
	}
	return new ResponseWithPayload(statusLine);
    }

    private void addFieldWithValue(final HeaderField field, final String value) {
	this.template.getResponseFields().put(field,
		Objects.requireNonNull(value, "value assigned to field should not be null"));
    }
}
