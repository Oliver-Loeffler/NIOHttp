package net.raumzeitfalle.niohttp;

import java.nio.charset.Charset;

/**
 * Created by hendrikebbers on 06.03.17.
 */
public interface Constants {
    char SPACE = ' ';
    char FIELD_VALUE_SEPARATOR = ':';

    String CRLF = "\r\n";
    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
}
