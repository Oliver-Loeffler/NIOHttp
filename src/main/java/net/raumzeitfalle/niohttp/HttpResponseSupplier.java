package net.raumzeitfalle.niohttp;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class HttpResponseSupplier implements Consumer<HttpResponse>, Supplier<HttpResponse> {

    private HttpResponse response;

    @Override
    public HttpResponse get() {
	return this.response;
    }

    @Override
    public void accept(HttpResponse t) {
	this.response = t;
    }

}
