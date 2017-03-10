# NIOHttp

[![Build Status](https://travis-ci.org/Oliver-Loeffler/NIOHttp.svg?branch=master)](https://travis-ci.org/Oliver-Loeffler/NIOHttp) [![codecov](https://codecov.io/gh/Oliver-Loeffler/NIOHttp/branch/master/graph/badge.svg)](https://codecov.io/gh/Oliver-Loeffler/NIOHttp)  

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
  * Use HttpResponse.fromBytes(byte[] bytes) to create a HttpResponse object which allows access to all HTTP message fields and values and of course, to the HTTP response payload (content) 

# Basics
## The HTTP Working Group
* HTTP Working Group: http://httpwg.org

### The HTTP 1.1 Protocol 

* Introduction for RFC7230: http://httpwg.org/specs/rfc7230.html#introduction
* Uniform Resource Identifier (URI) in HTTP: http://httpwg.org/specs/rfc7230.html#uri
* Message format: http://httpwg.org/specs/rfc7230.html#http.message

### HTTP/2 Protocol

* IETF RFC7540 https://tools.ietf.org/html/rfc7540


