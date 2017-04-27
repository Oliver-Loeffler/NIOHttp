package net.raumzeitfalle.niohttp.header;

import java.net.URL;
import java.util.Objects;

public class Host implements RequestHeaderField {

    private static final String HOST = "Host";
    private URL url;

    public Host(URL url) {
	this.url = Objects.requireNonNull(url, "url must not be null");
    }

    @Override
    public String toString() {
	return get();
    }

    @Override
    public String name() {
	return HOST;
    }

    @Override
    public String value() {
	if (this.url.getPort() > 0) {
	    return this.url.getHost() + ":" + this.url.getPort();
	}
	return this.url.getHost();
    }

}
