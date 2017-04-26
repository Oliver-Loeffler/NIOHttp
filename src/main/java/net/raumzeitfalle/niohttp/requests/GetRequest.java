package net.raumzeitfalle.niohttp.requests;

import java.net.URL;

import net.raumzeitfalle.niohttp.Constants;
import net.raumzeitfalle.niohttp.RequestType;
import net.raumzeitfalle.niohttp.header.Connection;
import net.raumzeitfalle.niohttp.header.GeneralHeaderField;
import net.raumzeitfalle.niohttp.header.Host;
import net.raumzeitfalle.niohttp.header.RequestHeader;
import net.raumzeitfalle.niohttp.header.RequestHeaderField;
import net.raumzeitfalle.niohttp.header.UserAgent;

class GetRequest implements Request {

    private RequestHeader header;

    private RequestHeaderField userAgent;

    private RequestHeaderField host;
    
    private GeneralHeaderField connection;

    public GetRequest(URL url, UserAgent userAgent, Connection connection) {
	this.header = new RequestHeader(RequestType.GET, url);
	this.userAgent = userAgent;
	this.host = new Host(url);
	this.connection = connection;
    }

    @Override
    public String toString() {
	return new StringBuilder(this.header.get())
		.append(this.userAgent.get())
		.append(this.host.get())
		.append(this.connection.get())
		.append(Constants.CRLF)
		.toString();
    }

    @Override
    public String get() {
	return this.toString();
    }
}
