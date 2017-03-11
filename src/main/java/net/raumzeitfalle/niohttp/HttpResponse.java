package net.raumzeitfalle.niohttp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;

class HttpResponse {

    private final String protocolVersion;
    private final String statusCode;
    private final String reasonPhrase;

    private final byte[] payload;

    private final Map<HeaderField, String> responseFields = new TreeMap<>(new HeaderFieldComparator());

    public HttpResponse(final String protocolVersion, final String statusCode, final String reasonPhrase,
	    final byte[] payload) {
	this.protocolVersion = Objects.requireNonNull(protocolVersion, "protocolVersion should not be null");
	this.statusCode = Objects.requireNonNull(statusCode, "statusCode should not be null");
	this.reasonPhrase = Objects.requireNonNull(reasonPhrase, "reasonPhrase should not be null");
	this.payload = Objects.requireNonNull(payload, "payload should not be null");

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
	StringBuilder b = new StringBuilder(protocolVersion).append(SPACE).append(statusCode).append(SPACE)
		.append(reasonPhrase).append(CRLF);

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

    /**
     * Factory method creating a {@link HttpResponse} a single HttpResponse
     * object from a byte array which is assumed to be complete (no missing
     * bytes).
     *
     * @param bytes
     * @return HttpResponse
     */
    public static Optional<HttpResponse> fromBytes(byte[] bytes) {
	String[] responseLines = new String(bytes).split(CRLF);

	int firstSpace = responseLines[0].indexOf(SPACE);

	/*
	 * TODO: This way of protocol detection (if message header is correct)
	 * is most likely fragile and error prone - implement better
	 * methodology.
	 */
	if (firstSpace < 0)
	    return Optional.empty();

	String protocol = responseLines[0].substring(0, firstSpace);
	String statusCodeAndReason = responseLines[0].substring(firstSpace + 1, responseLines[0].length());
	int secondSpace = statusCodeAndReason.indexOf(SPACE);
	String statusCode = statusCodeAndReason.substring(0, secondSpace);
	String reasonPhrase = statusCodeAndReason.substring(secondSpace + 1, statusCodeAndReason.length());

	HttpResponseBuilder responseBuilder = new HttpResponseBuilder(protocol).withStatus(statusCode)
		.withReasonPhrase(reasonPhrase);

	int lineIndex = 1;
	String line = responseLines[lineIndex];
	while (!line.isEmpty()) {
	    HeaderField messageField = HeaderFieldFactory.getInstance().fromString(line);
	    int separation = line.indexOf(SPACE) + 1;
	    responseBuilder.addMessageField(messageField, line.substring(separation, line.length()));
	    line = responseLines[lineIndex++];
	}

	int firstPayloadByte = findFirstPayloadByteIndex(bytes);

	if (firstPayloadByte < bytes.length) {
	    byte[] payloadBytes = new byte[bytes.length - firstPayloadByte];
	    System.arraycopy(bytes, firstPayloadByte, payloadBytes, 0, payloadBytes.length);
	    responseBuilder.withPayload(payloadBytes);
	}
	return Optional.of(responseBuilder.build());
    }

    private static int findFirstPayloadByteIndex(byte[] bytes) {
	int firstPayloadByte = 0;
	while (firstPayloadByte < bytes.length) {
	    if (firstPayloadByte > 3 && carriageReturnLineFeedTwoTimes(bytes, firstPayloadByte)) {
		firstPayloadByte++;
		break;
	    }
	    firstPayloadByte++;
	}
	return firstPayloadByte;
    }

    private static boolean carriageReturnLineFeedTwoTimes(byte[] bytes, int i) {
	byte CR = 13;
	byte LF = 10;
	return bytes[i - 3] == CR && bytes[i - 2] == LF && bytes[i - 1] == CR && bytes[i] == LF;
    }
}
