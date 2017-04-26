package net.raumzeitfalle.niohttp.requests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import net.raumzeitfalle.niohttp.header.Connection;
import net.raumzeitfalle.niohttp.header.UserAgent;

public class HttpRequestBuilder {

    private URL url;

    private Optional<Connection> connection = Optional.empty();

    private Optional<UserAgent> userAgent = Optional.empty();

    public HttpRequestBuilder(String url) throws MalformedURLException {
	this(new URL(url));
    }

    public HttpRequestBuilder(URL url) {
	this.url = url;
    }

    public HttpRequestBuilder closeConnection() {
	this.connection = Optional.of(Connection.close());
	return this;
    }

    public HttpRequestBuilder keepAlive() {
	this.connection = Optional.of(Connection.keepAlive());
	return this;
    }

    public HttpRequestBuilder userAgent(String userAgent) {
	Objects.requireNonNull(userAgent, "userAgent must not be null");
	this.userAgent = Optional.of(new UserAgent(userAgent));
	return this;
    }

    public Request httpGet() {
	UserAgent agent = this.userAgent.orElse(new UserAgent("NIOHttp"));
	Connection con = this.connection.orElse(Connection.close());
	return new GetRequest(this.url, agent, con);
    }
}
