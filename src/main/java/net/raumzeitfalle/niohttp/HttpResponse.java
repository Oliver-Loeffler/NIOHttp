package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;

class HttpResponse {

    private final String protocolVersion;
    private final String statusCode;
    private final String reasonPhrase;

    private final byte[] payload;

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    public HttpResponse(final String protocolVersion, final String statusCode,
                 final String reasonPhrase, final byte[] payload) {
        this.protocolVersion = Objects.requireNonNull(protocolVersion, "protocolVersion should not be null");
        this.statusCode = Objects.requireNonNull(statusCode, "statusCode should not be null");
        this.reasonPhrase = Objects.requireNonNull(reasonPhrase, "reasonPhrase should not be null");
        this.payload = Objects.requireNonNull(payload, "payload should not be null");
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(protocolVersion).append(SPACE)
                .append(statusCode)
                .append(SPACE).append(reasonPhrase).append(CRLF);

	for (Entry<HeaderField, String> e : responseFields
                .entrySet()) {
            addResponseFieldContent(b, e.getKey());
        }
        b.append(CRLF);

        if (this.payload.length > 0) {
            b.append(new String(payload));
        }

        return b.toString();
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    protected void addResponseFieldWithValue(final HeaderField field, final String value) {
	this.responseFields.put(field, Objects.requireNonNull(value, "value assigned to a field must not be null"));
    }

    private void addResponseFieldContent(StringBuilder b,
	    HeaderField responseField) {
        Objects.requireNonNull(b, "String builder should not be null");
        Objects.requireNonNull(responseField, "responseField builder should not be null");
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
	    HeaderField messageField = HeaderFieldFactory.getInstance().fromString(line);
            int separation = line.indexOf(SPACE) + 1;
	    responseBuilder.addMessageField(messageField, line.substring(separation, line.length()));
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
