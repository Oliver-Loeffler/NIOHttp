package net.raumzeitfalle.niohttp.header;

import java.util.function.Supplier;

import net.raumzeitfalle.niohttp.Constants;

/**
 * Applicable specification: RFC7230
 */
public interface HeaderField extends Supplier<String> {

    String name();

    String value();

    default String fieldIdentifier() {
	return name().trim() + Constants.FIELD_VALUE_SEPARATOR;
    }

    default String fieldValue() {
	return Constants.SPACE + value();
    }

    default String get() {
	return new StringBuilder(fieldIdentifier())
		.append(fieldValue())
		.append(Constants.CRLF).toString();
    }

}
