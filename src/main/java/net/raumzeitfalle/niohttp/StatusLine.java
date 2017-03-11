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
	this.reasonPhrase = ensureCorrectReasonPhrase(reasonPhrase, 0);
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

    public int statusCode() {
	return this.statusCode;
    }

    private String parseProtocolVersion(final String line) {
	int firstSpace = findFirstSpace(line);
	String protocolVersion = line.substring(0, firstSpace);
	return assertNoLeadingAndTrailingBWS(protocolVersion);
    }

    private String assertNoLeadingAndTrailingBWS(String protocolVersion) {
	if (protocolVersion.length() > protocolVersion.trim().length()) {
	    throw new HttpMessageParsingException(COULD_NOT_PARSE_MESSAGE);
	}
	return protocolVersion;
    }

    private int findFirstSpace(final String line) {
	int firstSpace = line.indexOf(Constants.SPACE);
	if (firstSpace < 1) {
	    throw new HttpMessageParsingException(COULD_NOT_PARSE_MESSAGE);
	}
	return firstSpace;
    }

    private int parseStatusCode(final String line) {
	int firstSpace = findFirstSpace(line);
	return converStatusCodeAndEnsureDigits(line.substring(firstSpace + 1, firstSpace + 4));
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

    private String ensureCorrectReasonPhrase(final String reasonPhrase, int beginIndex) {
	return trimAndEnsureNotEmpty(extractTextUntilFirstCRLF(reasonPhrase, beginIndex), beginIndex);
    }

    private String parseReasonPhrase(final String line) {
	int fixedOffsetToReasonPhrase = 5;
	int firstReasonPhraseChar = findFirstSpace(line) + fixedOffsetToReasonPhrase;
	return ensureCorrectReasonPhrase(line, firstReasonPhraseChar);
    }

    private String trimAndEnsureNotEmpty(final String line, int firstReasonPhraseChar) {
	String phrase = extractTextUntilFirstCRLF(line, firstReasonPhraseChar);
	if (phrase.isEmpty()) {
	    throw new HttpMessageParsingException("could not parse reason phrase from HTTP status line");
	}
	return phrase;
    }

    private String extractTextUntilFirstCRLF(final String line, int firstReasonPhraseChar) {
	int firstLineBreak = line.indexOf(Constants.CRLF);
	if (firstLineBreak == -1) {
	    return line.substring(0, line.length());
	}
	return line.substring(firstReasonPhraseChar, firstLineBreak);
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
