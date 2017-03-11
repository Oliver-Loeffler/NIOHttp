package net.raumzeitfalle.niohttp;

class HttpMessageParsingException extends RuntimeException {

    private static final long serialVersionUID = -3767246146751224818L;

    public HttpMessageParsingException(String message) {
	super(message);
    }

    public HttpMessageParsingException(String message, Throwable cause) {
	super(message, cause);
    }

}
