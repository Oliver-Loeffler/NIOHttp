package net.raumzeitfalle.niohttp;

import java.nio.ByteBuffer;
import java.util.Objects;

class HttpResponseReader {

    private final ByteBuffer receive;

    private byte[] bytesRead;

    public HttpResponseReader(final ByteBuffer bufferToReadFrom) {
		this.receive = Objects.requireNonNull(bufferToReadFrom, "buffer should not be null");
    }

    public HttpResponse readFromBuffer() {
		receive.flip();
		byte CRLF = 13;
		byte[] bytes = new byte[receive.capacity()];
		bytes[0] = receive.get();
		for (int i = 1; i < bytes.length; i++) {
			bytes[i] = receive.get();
			if (!receive.hasRemaining())
			break;
			if (bytes[i - 1] == CRLF && bytes[i] == CRLF)
			break;
		}

		this.bytesRead = bytes;
		return HttpResponse.fromBytes(bytes);
    }

    /**
     * @return bytes read from buffer
     */
    protected byte[] getBytesRead() {
		return this.bytesRead;
    }
}
