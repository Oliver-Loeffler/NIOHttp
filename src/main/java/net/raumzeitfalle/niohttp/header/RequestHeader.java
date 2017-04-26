package net.raumzeitfalle.niohttp.header;

import java.net.URL;
import java.util.function.Supplier;

import net.raumzeitfalle.niohttp.Constants;
import net.raumzeitfalle.niohttp.Protocol;
import net.raumzeitfalle.niohttp.RequestType;

public class RequestHeader implements Supplier<String> {

    private RequestType type;

    private String resource;

    private Protocol protocol;

    public RequestHeader(RequestType type, URL url) {
	this.type = type;
	this.resource = getResourcePath(url);
	this.protocol = Protocol.fromUrl(url);
    }

    private String getResourcePath(URL url) {
	if (url.getPath().isEmpty()) {
	    return "/";
	}
	return url.getPath();
    }

    @Override
    public String toString() {
	return new StringBuilder(type.name()).append(Constants.SPACE).append(this.resource).append(Constants.SPACE)
		.append(this.protocol.getProtocolAndVersion()).append(Constants.CRLF).toString();
    }

    @Override
    public String get() {
	return this.toString();
    }
}
