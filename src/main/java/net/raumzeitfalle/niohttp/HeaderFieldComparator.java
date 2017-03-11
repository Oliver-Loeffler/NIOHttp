package net.raumzeitfalle.niohttp;

import java.util.Comparator;

class HeaderFieldComparator implements Comparator<HeaderField> {

    @Override
    public int compare(HeaderField a, HeaderField b) {
	return b.getFieldName().toLowerCase().compareTo(a.getFieldName().toLowerCase());
    }

}
