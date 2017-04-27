package net.raumzeitfalle.niohttp.header;

public class Connection implements GeneralHeaderField {

    private static final String CONNECTION = "Connection";
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
	return get();
    }

    @Override
    public String name() {
	return CONNECTION;
    }

    @Override
    public String value() {
	return this.option.trim();
    }

}
