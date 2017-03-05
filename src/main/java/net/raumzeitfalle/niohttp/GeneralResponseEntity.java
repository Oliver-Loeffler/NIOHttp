package net.raumzeitfalle.niohttp;

/*
 * TODO: Replace enum with individual types so that no switch is required for invoking the proper builder method.
 */
enum GeneralResponseEntity {

    SERVER("Server"), DATETIME("Date"), CONTENT_TYPE(
            "Content-Type"), CONTENT_LENGTH(
                    "Content-Length"), CONNECTION_STATUS(
                            "Connection"), LAST_MODIFIED("Last-Modified"), ETAG(
                                    "ETag"), ACCEPT_RANGES(
                                            "Accept-Ranges"), VARY(
                                                    "Vary"), X_POWERED_BY(
                                                            "X-Powered-By"), KEEP_ALIVE(
                                                                    "Keep-Alive");

    private String fieldName;

    private GeneralResponseEntity(String fieldName) {
	this.fieldName = fieldName;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public static GeneralResponseEntity fromString(String lineFromBytes) {
	for (GeneralResponseEntity g : values()) {
	    if (lineFromBytes.startsWith(g.fieldName)) {
		return g;
	    }
	}
	throw new RuntimeException("Unknown HTTP header response field");
    }

    public void callHttpResponseBuilderWhenSupported(
            HttpResponseBuilder builder, String value) {
	switch (this) {
	    case SERVER: {
		builder.servedBy(value);
		break;
	    }
	    case CONTENT_TYPE: {
		builder.withContentOfType(value);
		break;
	    }
	    case DATETIME: {
		builder.deliveredAt(value);
		break;
	    }
	    case CONNECTION_STATUS: {
		builder.withConnectionStatus(value);
		break;
	    }
	    case CONTENT_LENGTH: {
		builder.contentLength(Long.valueOf(value.trim()).longValue());
		break;
	    }
	    case KEEP_ALIVE: {
		builder.keepAlive(value);
		break;
	    }
	    case X_POWERED_BY: {
		builder.xPoweredBy(value);
		break;
	    }
	    case VARY: {
		builder.vary(value);
		break;
	    }
	    case ACCEPT_RANGES: {
		builder.acceptRanges(value);
		break;
	    }
	    case ETAG: {
		builder.withEtag(value);
	    }
	    case LAST_MODIFIED:
		builder.lastModified(value);
		break;

	}
    }

}