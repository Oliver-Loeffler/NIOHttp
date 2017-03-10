package net.raumzeitfalle.niohttp;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.util.function.Consumer;

import static net.raumzeitfalle.niohttp.Constants.CRLF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
 * TODO: refine to enable testing w/o need for a socket channel.
 */
public class HttpResponseReaderTest {

    private HttpResponseReader classUnderTest;

    private TestConsumer consumer = new TestConsumer();

    @Test
    public void read() throws IOException, InterruptedException {
	classUnderTest = new HttpResponseReader(examplaryByteChannel());
	consumer = new TestConsumer();

	classUnderTest.read(consumer);

	assertNotNull(consumer.response);
	assertEquals(expectedStringResponse(), consumer.toString());
	assertArrayEquals(expectedByteArray(), consumer.getBytes());

    }

    private ReadableByteChannel examplaryByteChannel() {
	InputStream inputStream = new ByteArrayInputStream(expectedByteArray());
	return java.nio.channels.Channels.newChannel(inputStream);
    }

    private byte[] expectedByteArray() {
	return expectedStringResponse().getBytes();
    }

    private String expectedStringResponse() {
	// @formatter:off
	return new StringBuilder("HTTP/1.1").append(" 400 Bad Request").append(CRLF)
		.append("Server: nginx/1.10.2").append(CRLF)
		.append("Date: Sat, 04 Mar 2017 22:02:46 GMT").append(CRLF)
		.append("Content-Type: text/html").append(CRLF)
		.append("Content-Length: 173").append(CRLF)
		.append("Connection: close").append(CRLF)
		.append(CRLF)
		.append("<html>").append(CRLF)
		.append("<head><title>400 Bad Request</title></head>").append(CRLF)
		.append("<body bgcolor=\"white\">").append(CRLF)
		.append("<center><h1>400 Bad Request</h1></center>").append(CRLF)
		.append("<hr><center>nginx/1.10.2</center>").append(CRLF)
		.append("</body>").append(CRLF)
		.append("</html>").toString();
	// @formatter:on
    }

    class TestConsumer implements Consumer<HttpResponse> {
	HttpResponse response;

	public byte[] getBytes() {
	    return this.response.getBytes();
	}

	public String toString() {
	    return this.response.toString();
	}

	@Override
	public void accept(HttpResponse t) {
	    this.response = t;
	}

    }

}
