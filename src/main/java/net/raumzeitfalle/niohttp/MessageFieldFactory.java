package net.raumzeitfalle.niohttp;

import java.util.Objects;

class MessageFieldFactory {

    private static MessageFieldFactory instance = new MessageFieldFactory();

    private MessageFieldFactory() {
    }

    public static MessageFieldFactory getInstance() {
	return instance;
    }

    public MessageField fromString(String lineFromBytes) {
	Objects.requireNonNull(lineFromBytes, "lineFromBytes should not be null");
	if (GeneralFields.isGeneralField(lineFromBytes)) {
	    return GeneralFields.fromString(lineFromBytes);
	} else if (ResponseFields.isResponseField(lineFromBytes)) {
	    return ResponseFields.fromString(lineFromBytes);
	} else if (EntityFields.isEntityField(lineFromBytes)) {
	    return EntityFields.fromString(lineFromBytes);
	} else {
	    return ExperimentalFields.fromLine(lineFromBytes);
	}
    }
}
