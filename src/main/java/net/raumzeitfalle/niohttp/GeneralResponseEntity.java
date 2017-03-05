package net.raumzeitfalle.niohttp;

enum GeneralResponseEntity {

    SERVER("Server"), DATETIME("Date"), CONTENT_TYPE(
            "Content-Type"), CONTENT_LENGTH(
                    "Content-Length"), CONNECTION_STATUS("Connection");

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
	}
    }

}