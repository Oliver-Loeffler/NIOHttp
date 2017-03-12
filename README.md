# NIOHttp

[![Build Status](https://travis-ci.org/Oliver-Loeffler/NIOHttp.svg?branch=master)](https://travis-ci.org/Oliver-Loeffler/NIOHttp)  

Reading and writing HTTP request and response objects from and to  ``java.nio.channels`` .

*This project has exploratory character and is in a very early stage of development.* 

## Goals:

 * Create a library/toolkit to write and read HTTP requests to and from ``java.nio.channels`` (Java's non-blocking IO)
 * Explore ``java.util.concurrentjava.util.concurrent`` API
 * Do not use external frameworks, integrate HTTP protocol and  ``java.nio.channels``  using JDK 1.8+.

### Boundary Conditions:

 * zero external dependencies beside standard JDK except for unit & integration testing and build automation

### Steps to implementation

 1. Implement first draft of HttpResponse class and corresponding reader class considering RFC7230 and RFC7231
 2. The first version of HttpResponse will only carry HTTP fields and payload
 3. Implement simple HttpRequest class and corresponding HttpRequestBuilder
 
 
## Idea

 * Connecting to a given URL using SocketChannels
 * Submit a HttpRequest by writing the request to a ByteBuffer connected to the SocketChannel
 * Read the servers response from corresponding receive ByteBuffer
  * Use HttpResponseReader to read the byte[] sequences
  * Use HttpResponse.fromBytes(byte[] bytes) to create a HttpResponse object which allows access to all HTTP message fields and values and of course, to the HTTP response payload (content) 

## Possible Concepts

First one need to connect a **SocketStream** to a URL such as http://www.raumzeitfalle.de/.

```java 
 
 URL url = new URL("http://www.raumzeitfalle.de/");
 InetSocketAddress address = new InetSocketAddress(url.getHost(), url.getDefaultPort());
 SocketChannel socketChannel = SocketChannel.open(address);

```
The server won't respond unless a request is sent (for example a GET request).
```java

  writeGetRequestTo(socketChannel);
  
```
Even with bad requests server will respond, however, this may take time and response time is different.
Here the static **HttpResponseReader.fromChannel(...)** method provides a **FutureTask< Void >** which can be executed by an **ExecutorService**. To collect the result, a **Consumer< HttpResponse >** must be provided.

```java

 FutureTask<Void> futureTask = HttpResponseReader.fromChannel(socketChannel, r -> System.out.println(r.responseHeader());
 ExecutorService executor = Executors.newFixedThreadPool(1);
 executor.submit(futureTask);

```


## HTTP Protocol Resources

### General

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
