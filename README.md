# NIOHttp

Building HTTP requests and responses using Java NIO/NIO2

### Goals:
* Create a library/toolkit to build and send HTTP requests vis NIO/NIO2 as well to receive HTTP requests via NIO/NIO2 and to parse them

### Boundary Conditions:
* zero external dependencies beside JDK 1.8
* only testing frameworks for (Junit, TestNG or others) and mocking frameworks (Mockito, PowerMock etc.) are permitted

# Idea

 * Connecting to a given URL using SocketChannels
 * Submit a HttpRequest by writing the request to a ByteBuffer connected to the SocketChannel
 * Read the servers response from corresponding receive ByteBuffer
  * Use HttpResponseReader to read the byte[] sequences
  * Use HttpResponse.fromBytes(byte[] bytes) to create a HttpResponse object wich allows access to all HTTP message fields and values and of course, to the HTTP response payload (content) 

# Basics

### HTTP 1.1 Protocol

* IETF RFC2616 https://tools.ietf.org/html/rfc2616
* HTTP Working Group: https://tools.ietf.org/wg/httpbis/
* HTTP Requests: https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
* HTTP Responses: https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6

### HTTP/2 Protocol

* IETF RFC7540 https://tools.ietf.org/html/rfc7540


