package net.raumzeitfalle.niohttp.header;

import java.net.URL;
import java.util.Objects;

import net.raumzeitfalle.niohttp.Constants;

public class Host implements RequestHeaderField {

    private URL url;
   
    public Host(URL url) {
	this.url = Objects.requireNonNull(url, "url must not be null");
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("Host")
		.append(Constants.FIELD_VALUE_SEPARATOR)
		.append(Constants.SPACE)
		.append(this.url.getHost());
	
        	if (this.url.getPort() > 0) {
        	   sb.append(Constants.FIELD_VALUE_SEPARATOR)
        	   .append(this.url.getPort());
        	}
	
	return sb.append(Constants.CRLF).toString();
    }

    @Override
    public String get() {
	return this.toString();
    }

}
