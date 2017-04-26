package net.raumzeitfalle.niohttp.requests;

import java.util.function.Supplier;

import net.raumzeitfalle.niohttp.Constants;

public interface Request extends Supplier<String> {

    default byte[] getBytes() {
	return get().getBytes(Constants.DEFAULT_CHARSET);
    }
}
