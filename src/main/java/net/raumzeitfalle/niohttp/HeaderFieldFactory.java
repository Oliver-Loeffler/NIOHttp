package net.raumzeitfalle.niohttp;

import java.util.Objects;

class HeaderFieldFactory {

    private static final HeaderFieldFactory instance = new HeaderFieldFactory();

    private HeaderFieldFactory() {
    }

    public static HeaderFieldFactory getInstance() {
	return instance;
    }

    public HeaderField fromString(final String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	if (GeneralFields.isGeneralField(lineFromBytes)) {
	    return GeneralFields.fromString(lineFromBytes);
	} else if (ResponseFields.isResponseField(lineFromBytes)) {
	    return ResponseFields.fromString(lineFromBytes);
	} else if (AuthenticationFields.isResponseField(lineFromBytes)) {
	    return AuthenticationFields.fromString(lineFromBytes);
	} else if (EntityFields.isEntityField(lineFromBytes)) {
	    return EntityFields.fromString(lineFromBytes);
	} else {
	    return ExperimentalFields.fromLine(lineFromBytes);
	}
    }
}
