package net.raumzeitfalle.niohttp;

import java.util.Comparator;

public class MessageFieldComparator implements Comparator<MessageField> {
    @Override
    public int compare(MessageField a, MessageField b) {
	return b.getFieldName().toLowerCase().compareTo(a.getFieldName().toLowerCase());
    }

}
