package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

class HttpResponse {
    private static final char SPACE = ' ';
    private static final String CRLF = "\r\n";

    private final String protocolVersion;
    private final String statusCode;
    private final String reasonPhrase;

    private final byte[] payload;

    private Map<GeneralResponseEntity, String> responseFields = new TreeMap<>();

    HttpResponse(String protocolVersion, String statusCode,
            String reasonPhrase, byte[] payload) {
	this.protocolVersion = protocolVersion;
	this.statusCode = statusCode;
	this.reasonPhrase = reasonPhrase;
	this.payload = payload;
    }

    @Override
    public String toString() {
	StringBuilder b = new StringBuilder(protocolVersion).append(SPACE)
	        .append(statusCode)
	        .append(SPACE).append(reasonPhrase).append(CRLF);

	for (Entry<GeneralResponseEntity, String> e : responseFields
	        .entrySet()) {
	    addResponseFieldContent(b, e.getKey());
	}
	b.append(CRLF);

	if (this.payload.length > 0) {
	    b.append(new String(payload));
	}

	return b.toString();
    }

    byte[] getBytes() {
	return toString().getBytes();
    }

    protected void addResponseFieldWithValue(GeneralResponseEntity field,
            String value) {
	this.responseFields.put(field, value);
    }

    private void addResponseFieldContent(StringBuilder b,
            GeneralResponseEntity responseField) {
	if (this.responseFields.containsKey(responseField)) {
	    b.append(responseField.getFieldName()).append(": ")
	            .append(this.responseFields.get(responseField))
	            .append(CRLF);
	}
    }

    /**
     * Factory method creating a {@link HttpResponse} object from a byte array.
     * 
     * @param bytes
     * @return HttpResponse object
     */
    public static HttpResponse fromBytes(byte[] bytes) {
	String[] responseLines = new String(bytes).split(CRLF);

	int firstSpace = responseLines[0].indexOf(SPACE);
	String protocol = responseLines[0].substring(0, firstSpace);
	String statusCodeAndReason = responseLines[0].substring(firstSpace + 1,
	        responseLines[0].length());
	int secondSpace = statusCodeAndReason.indexOf(SPACE);
	String statusCode = statusCodeAndReason.substring(0, secondSpace);
	String reasonPhrase = statusCodeAndReason.substring(secondSpace + 1,
	        statusCodeAndReason.length());

	HttpResponseBuilder responseBuilder = new HttpResponseBuilder(protocol)
	        .withStatus(statusCode).withReasonPhrase(reasonPhrase);

	int lineIndex = 1;
	String line = responseLines[lineIndex];
	while (!line.isEmpty()) {
	    GeneralResponseEntity g = GeneralResponseEntity.fromString(line);
	    int separation = line.indexOf(SPACE) + 1;
	    g.callHttpResponseBuilderWhenSupported(responseBuilder,
	            line.substring(separation, line.length()));
	    line = responseLines[lineIndex++];
	}

	StringBuilder payloadBuilder = new StringBuilder();
	while (lineIndex < responseLines.length) {
	    payloadBuilder.append(responseLines[lineIndex++]);
	    if (lineIndex < responseLines.length) {
		payloadBuilder.append(CRLF);
	    }
	}
	responseBuilder.withPayload(payloadBuilder.toString().getBytes());
	return responseBuilder.build();
    }
}
