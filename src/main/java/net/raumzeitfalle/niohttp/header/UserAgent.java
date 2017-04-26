package net.raumzeitfalle.niohttp.header;

import java.util.Objects;

import net.raumzeitfalle.niohttp.Constants;

public class UserAgent implements RequestHeaderField {
    private String agentDescription;

    public UserAgent(String userAgent) {
	Objects.requireNonNull(userAgent, "userAgent must not be null");
	this.agentDescription = userAgent;
    }
    
    @Override
    public String toString() {
	return new StringBuilder("User-Agent")
		.append(Constants.FIELD_VALUE_SEPARATOR)
		.append(Constants.SPACE)
		.append(this.agentDescription)
		.append(Constants.CRLF).toString();
    }

    @Override
    public String get() {
	return toString();
    }

}
