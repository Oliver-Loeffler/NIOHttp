package net.raumzeitfalle.niohttp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.function.Consumer;

class HttpResponseReader {

    private final ReadableByteChannel channel;

    private byte[] bytesRead;

    public HttpResponseReader(final ReadableByteChannel channel) {
		this.channel = Objects.requireNonNull(channel, "channel should not be null");
    }

    public void readFromBuffer(final Consumer<HttpResponse> responseConsumer) throws IOException {
		Objects.requireNonNull(responseConsumer, "responseConsumer should not be null");

		int read = 0;
		ByteBuffer inputBuffer = ByteBuffer.allocate(48);
		while (read >= 0) {
			read = channel.read(inputBuffer);

			byte[] newBytesRead = inputBuffer.array();
			byte[] merge = new byte[bytesRead.length + newBytesRead.length];

			int index = 0;
			for(byte b : bytesRead) {
				merge[index] = b;
				index++;
			}
			for(byte b : newBytesRead) {
				merge[index] = b;
				index++;
			}
			bytesRead = merge;
			inputBuffer.clear();


			byte CRLF = 13;
			for (int i = 1; i < bytesRead.length; i++) {
				if (bytesRead[i - 1] == CRLF && bytesRead[i] == CRLF) {
					byte[] responseBytes = new byte[i];

					for(int j = 0; j <= i; j++) {
						responseBytes[j] = bytesRead[j];
					}
					HttpResponse response = HttpResponse.fromBytes(responseBytes);
					responseConsumer.accept(response);

					byte[] unusedBytes = new byte[bytesRead.length - responseBytes.length];
					for(int j = responseBytes.length; j < bytesRead.length; j++) {
						unusedBytes[j] = bytesRead[j];
					}
					bytesRead = unusedBytes;
				}
			}
		}
    }

    /**
     * @return bytes read from buffer
     */
    protected byte[] getBytesRead() {
		return this.bytesRead;
    }
}
