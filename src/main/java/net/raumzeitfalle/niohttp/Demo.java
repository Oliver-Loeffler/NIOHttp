package net.raumzeitfalle.niohttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Requires a web server near by, will issue a bad request. Then a HttpResponse
 * object is created and printed to System.out. The bad request works currently
 * only on my Nginx 1.10.2 running on a Raspberry Pi.
 *
 * @author oliver
 */
class Demo {

    public static void main(String args[]) throws IOException {

        String address = "http://www.raumzeitfalle.net/";
        URL url = new URL(address);
        int port = url.getDefaultPort();

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel
                    .connect(new InetSocketAddress(url.getHost(),
                            port));
            Demo demo = new Demo(socketChannel, url);
            HttpResponse response = demo.read();
            sysout(demo.getResponseBytes(),
                    "Response (converted to String) received from Server");
            sysout(response.getBytes(), "HttpResponse object content");
        }
    }

    private static void sysout(byte[] bytes, String title) {
        StringBuilder out = new StringBuilder(title)
                .append(System.lineSeparator())
                .append("----------------------------------------------------")
                .append(System.lineSeparator())
                .append(new String(bytes)).append(System.lineSeparator());
        System.out.println(out);
    }

    private SocketChannel socketChannel;

    private byte[] responseBytes;

    private URL url;

    public Demo(SocketChannel socketChannel, URL url) {
        this.socketChannel = socketChannel;
        this.url = url;
    }

    public HttpResponse read() throws IOException {
        ByteBuffer receive = ByteBuffer.allocateDirect(1024);
        receive.clear();

        simpleButWrongRequest(this.socketChannel);

        int numBytesRead = socketChannel.read(receive);

        if (numBytesRead == -1) {
            socketChannel.close();
        } else {
            HttpResponseReader responseReader = new HttpResponseReader(receive);
            HttpResponse response = responseReader.readFromBuffer();
            System.out.println(response.toString());
            this.responseBytes = responseReader.getBytesRead();
            return response;
        }
        throw new IOException(
                "Failed too convice server to respond properly, or server sent empty response.");
    }

    /**
     * @return bytes from server as received.
     */
    protected byte[] getResponseBytes() {
        return this.responseBytes;
    }

    private void simpleButWrongRequest(SocketChannel socketChannel)
            throws IOException {
        String SPACE = " ";
        String CRLF = "\r\n";

        String request = new StringBuilder("GET").append(SPACE)
                .append(this.url.getPath()).append(SPACE).append("HTTP/1.1")
                .append(CRLF)
                .append("User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)")
                .append(CRLF)
                .append("Host:").append(SPACE).append(this.url.getHost())
                .append(CRLF)
                .append("Accept-Language: en-us").append(CRLF)
                // .append("Accept-Encoding: gzip, deflate").append(CRLF)
                .append("Connection: Keep-Alive").append(CRLF).append(CRLF)
                .toString();

        ByteBuffer sending = ByteBuffer
                .allocate(request.getBytes().length);
        sending.put(request.getBytes());
        sending.flip();

        socketChannel.write(sending);
    }
}
