package net.raumzeitfalle.niohttp;

import java.net.URL;

public enum Protocol {
    HTTP("1.1");

    private String protocolVersion;

    private Protocol(String version) {
	this.protocolVersion = version;
    }

    public static Protocol fromUrl(URL url) {
	String protocol = url.getProtocol();
	if (protocol.equals("http")) {
	    return Protocol.HTTP;
	}
	throw new IllegalArgumentException("Unsupported protocol : " + protocol);
    }

    public String getProtocolAndVersion() {
	return this.name() + "/" + this.protocolVersion;
    }
}
