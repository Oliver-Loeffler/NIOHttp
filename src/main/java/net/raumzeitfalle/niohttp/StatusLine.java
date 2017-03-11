package net.raumzeitfalle.niohttp;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static net.raumzeitfalle.niohttp.Constants.SPACE;

import java.util.Objects;

/**
 * Represents a response message status line according to RFC7230
 * 
 * @see <a href=
 *      "http://httpwg.org/specs/rfc7230.html#status.line">http://httpwg.org/specs/rfc7230.html#status.line</a>
 * 
 */
class StatusLine {

    private static final String COULD_NOT_PARSE_MESSAGE = "could not parse protocol and protocol version from message";

    private final String protocolVersion;

    private final int statusCode;

    private final String reasonPhrase;

    /**
     * Creates a Status Line object using given parameters.
     * 
     * @param protocolVersion
     *            (e.g. "HTTP/1.1")
     * @param statusCode
     *            (e.g. "400")
     * @param reasonPhrase
     *            (e.g. "Bad Request")
     * @throws HttpMessageParsingException
     *             in case of invalid input parameters
     */
    public StatusLine(final String protocolVersion, final String statusCode, final String reasonPhrase) {
	this.protocolVersion = assertNoLeadingAndTrailingBWS(protocolVersion);
	this.statusCode = converStatusCodeAndEnsureDigits(statusCode);
	this.reasonPhrase = ensureReasonPhraseHasNoBadChars(reasonPhrase);
    }

    /**
     * Parses a message and creates a corresponding StatusLine instance.
     * 
     * @param message
     */
    public StatusLine(final String message) {
	Objects.requireNonNull(message, "message must not be null");
	if (message.isEmpty()) {
	    throw new HttpMessageParsingException("could not parse HTTP status line");
	}
	this.protocolVersion = parseProtocolVersion(message);
	this.statusCode = parseStatusCode(message);
	this.reasonPhrase = parseReasonPhrase(message);
    }

    public String protocolVersion() {
	return this.protocolVersion;
    }

    /**
     * @return HTTP status codes such as 400, 404 or 501.
     */
    public int statusCode() {
	return this.statusCode;
    }

    private String parseProtocolVersion(final String line) {
	int firstSpace = indexOfFirstSpace(line);
	String protocolVersion = line.substring(0, firstSpace);
	return assertNoLeadingAndTrailingBWS(protocolVersion);
    }

    private String assertNoLeadingAndTrailingBWS(String protocolVersion) {
	if (protocolVersion.length() > protocolVersion.trim().length()) {
	    throw new HttpMessageParsingException(COULD_NOT_PARSE_MESSAGE);
	}
	return protocolVersion;
    }

    private int indexOfFirstSpace(final String line) {
	int firstSpace = line.indexOf(Constants.SPACE);
	if (firstSpace < 1) {
	    throw new HttpMessageParsingException(COULD_NOT_PARSE_MESSAGE);
	}
	return firstSpace;
    }

    private int indexAfterFirstSpace(final String line) {
	return indexOfFirstSpace(line) + 1;
    }

    private int parseStatusCode(final String line) {
	int digits = 3;
	int index = indexAfterFirstSpace(line);
	String statusCode = line.substring(index, index + digits);
	return converStatusCodeAndEnsureDigits(statusCode);
    }

    private int ensureStatusCodeDigits(int code) {
	if (code < 100 || code > 999) {
	    throw new HttpMessageParsingException("status code must have exactly 3 digits");
	}
	return code;
    }

    private int converStatusCodeAndEnsureDigits(String code) {
	try {
	    return ensureStatusCodeDigits(Integer.parseInt(code));
	} catch (NumberFormatException e) {
	    throw new HttpMessageParsingException("could not parse status code from HTTP status line", e);
	}
    }

    private String ensureReasonPhraseHasNoBadChars(final String reasonPhrase) {
	return trimAndEnsureNotEmpty(extractTextUntilFirstCRLF(reasonPhrase));
    }

    private String parseReasonPhrase(final String line) {
	int index = indexAfterFirstSpace(line);
	String candidate = line.substring(index);
	index = indexAfterFirstSpace(candidate);
	return ensureReasonPhraseHasNoBadChars(candidate.substring(index));
    }

    private String trimAndEnsureNotEmpty(final String line) {
	String phrase = extractTextUntilFirstCRLF(line);
	if (phrase.isEmpty()) {
	    throw new HttpMessageParsingException("could not parse reason phrase from HTTP status line");
	}
	return phrase;
    }

    private String extractTextUntilFirstCRLF(final String line) {
	int firstLineBreak = line.indexOf(Constants.CRLF);
	if (firstLineBreak == -1) {
	    return line;
	}
	return line.substring(0, firstLineBreak);
    }

    public String reasonPhrase() {
	return this.reasonPhrase;
    }

    @Override
    public String toString() {
	return new StringBuilder(protocolVersion).append(SPACE).append(statusCode).append(SPACE).append(reasonPhrase)
		.append(CRLF).toString();
    }

}
