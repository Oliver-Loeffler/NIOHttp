# NIOHttp

[![Build Status](https://travis-ci.org/Oliver-Loeffler/NIOHttp.svg?branch=master)](https://travis-ci.org/Oliver-Loeffler/NIOHttp)  

Building HTTP request and response objects which can be written and read from java.nio.channels.

## Goals:

 * Create a library/toolkit to build and send HTTP requests vis NIO/NIO2 as well to receive HTTP requests via NIO/NIO2 and to parse them
 * Implement HTTP/1.1 first.

### Boundary Conditions:

 * zero external dependencies beside JDK 1.8
 * only testing frameworks for (Junit, TestNG or others) and mocking frameworks (Mockito, PowerMock etc.) are permitted

### Steps to implementation

 1. Implement first draft of HttpResponse class and corresponding reader class considering RFC7230 and RFC7231
 2. The first version of HttpResponse will only carry HTTP fields and payload
 3. Implement simple HttpRequest class and corresponding HttpRequestBuilder
 
 
# Idea

 * Connecting to a given URL using SocketChannels
 * Submit a HttpRequest by writing the request to a ByteBuffer connected to the SocketChannel
 * Read the servers response from corresponding receive ByteBuffer
  * Use HttpResponseReader to read the byte[] sequences
  * Use HttpResponse.fromBytes(byte[] bytes) to create a HttpResponse object which allows access to all HTTP message fields and values and of course, to the HTTP response payload (content) 

# Basics
## The HTTP Working Group

* HTTP Working Group: http://httpwg.org


### The HTTP/1.1 Protocol 

* Introduction for RFC7230: http://httpwg.org/specs/rfc7230.html#introduction
* Uniform Resource Identifier (URI) in HTTP: http://httpwg.org/specs/rfc7230.html#uri
* Message format: http://httpwg.org/specs/rfc7230.html#http.message
* Semantics and Content: http://httpwg.org/specs/rfc7231.html#top
 * Request Methods http://httpwg.org/specs/rfc7231.html#methods
 * Request Header fields: http://httpwg.org/specs/rfc7231.html#request.header.fields
 * Response Status Codes: http://httpwg.org/specs/rfc7231.html#status.codes
 * Response Header Fields: http://httpwg.org/specs/rfc7231.html#response.header.fields
* IANA Status code assignments: http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml

### HTTP/2 Protocol

* Starting a HTTP/2 connection: http://httpwg.org/specs/rfc7540.html#starting
* The protocol: http://httpwg.org/specs/rfc7540.html
* HTTP/2 Message Exchanging: http://httpwg.org/specs/rfc7540.html#HTTPLayer

### HTTP over TLS (transport layer security)

* https://tools.ietf.org/html/rfc2818#section-2

### Other helpful references

 * As RFC7230 uses the term ABNF, here the explanation: https://en.wikipedia.org/wiki/Augmented_Backus–Naur_form
