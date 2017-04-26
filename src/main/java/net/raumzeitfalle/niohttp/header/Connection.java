package net.raumzeitfalle.niohttp.header;

import net.raumzeitfalle.niohttp.Constants;

public class Connection implements GeneralHeaderField {

    private String option;

    public Connection(String option) {
	this.option = option;
    }
    
    public static Connection close() {
	return new Connection("close");
    }
    
    public static Connection keepAlive() {
	return new Connection("keep-alive");
    }

    @Override
    public String toString() {
	return new StringBuilder("Connection")
		.append(Constants.FIELD_VALUE_SEPARATOR)
		.append(Constants.SPACE)
		.append(this.option)
		.append(Constants.CRLF)
		.toString();
    }

    @Override
    public String get() {
	return this.toString();
    }

}
