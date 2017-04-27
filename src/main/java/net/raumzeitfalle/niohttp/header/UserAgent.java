package net.raumzeitfalle.niohttp.header;

import java.util.Objects;

public class UserAgent implements RequestHeaderField {
    private static final String USER_AGENT = "User-Agent";
    private String agentDescription;

    public UserAgent(String userAgent) {
	Objects.requireNonNull(userAgent, "userAgent must not be null");
	this.agentDescription = userAgent;
    }

    @Override
    public String toString() {
	return get();
    }

    @Override
    public String name() {
	return USER_AGENT;
    }

    @Override
    public String value() {
	return this.agentDescription.trim();
    }

}
