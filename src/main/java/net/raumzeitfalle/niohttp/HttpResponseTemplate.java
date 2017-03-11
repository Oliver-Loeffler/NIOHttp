package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Map.Entry;

interface HttpResponseTemplate {

    HttpResponseTemplate addMessageField(HeaderField field, String value);

    HttpResponseTemplate withPayload(byte[] payload);

    StatusLine getStatusLine();

    Map<HeaderField, String> getResponseFields();

    byte[] getPayload();

    boolean requiresPayload();

    default HttpResponse build() {
	return buildFromTemplate(this);
    }

    static HttpResponse buildFromTemplate(HttpResponseTemplate template) {
	StatusLine statusLine = template.getStatusLine();
	byte[] payload = new byte[0];

	if (template.requiresPayload()) {
	    payload = template.getPayload();
	}

	HttpResponse response = new HttpResponse(statusLine, payload);
	for (Entry<HeaderField, String> e : template.getResponseFields().entrySet()) {
	    response.addResponseFieldWithValue(e.getKey(), e.getValue());
	}
	return response;
    }

}
