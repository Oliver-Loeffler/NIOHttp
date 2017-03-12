# NIOHttp

[![Build Status](https://travis-ci.org/Oliver-Loeffler/NIOHttp.svg?branch=master)](https://travis-ci.org/Oliver-Loeffler/NIOHttp)  

Reading and writing HTTP request and response objects from and to  ``java.nio.channels`` .

:warning: *This project has exploratory character and is in a very early stage of development.* 

## Goals

 * Create a library/toolkit to write and read HTTP requests to and from ``java.nio.channels`` (Java's non-blocking IO)
 * Explore ``java.util.concurrentjava.util.concurrent`` API
 * Do not use external frameworks, integrate HTTP protocol and  ``java.nio.channels``  using JDK 1.8+.

### Boundary Conditions

 * zero external dependencies beside standard JDK except for unit & integration testing and build automation


## Idea

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

[Example] (NIOHttp/blob/master/src/main/java/net/raumzeitfalle/niohttp/playground/FutureDemo.java)

```java

 FutureTask<Void> futureTask = HttpResponseReader
 				.fromChannel(socketChannel,r -> System.out.println(r.responseHeader());
 		
 ExecutorService executor = Executors.newFixedThreadPool(1);
 executor.submit(futureTask);

```
Furthermore **HttpResponseReader.fromChannel(...)** returns a **Stream** of HttpResponse objects. 

[Example] (NIOHttp/blob/master/src/main/java/net/raumzeitfalle/niohttp/playground/StreamDemo.java)

```java

Stream<HttpResponse> responseStream = HttpResponseReader.fromChannel(socketChannel); 
responseStream.findFirst().ifPresent(consumer);

```
This would enable (given appropriate HttpResponse capabilities), interesting ways of working with HttpResponses. 

```java

HttpResponseReader.fromChannel(socketChannel)
	    .filter( r -> r.isNotBadRequest() )
	    .findFirst().ifPresent(consumer);
	    
```

The current implementation is not yet fully functional here, as the **HttpResponseReader** only reads exactly one message from a channel. The response reading process has to be extended for continuous reading so that **HttpResponse** streaming will work.

## Resources

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

### Java non-blocking IO (NIO and NIO2) and Networking

* Java API
 * https://docs.oracle.com/javase/tutorial/
 * Java NIO channels: http://docs.oracle.com/javase/8/docs/api/java/nio/channels/package-summary.html
 
* Guides & Tutorials, Inspiration
 * NIO2: http://www.ibm.com/developerworks/java/library/j-nio2-1/index.html
 * NIO2 Async FileChannel: http://www.baeldung.com/java-nio2-async-file-channel
 * Jenkov: http://tutorials.jenkov.com/java-nio/index.html
 * NIO Selectors: http://www.baeldung.com/java-nio-selector
 * Apache HttpClient: http://www.baeldung.com/httpasyncclient-tutorial
 * Javaworld on NIO/NIO2: http://www.javaworld.com/article/2078654/core-java/java-se-five-ways-to-maximize-java-nio-and-nio-2.html
 * Java2s on non-blocking async IO: http://www.java2s.com/Tutorials/Java/Java_Network/0080__Java_Network_Asynchronous_Socket_Channels.htm
 
### Other helpful references

 * As RFC7230 uses the term ABNF, here the explanation: https://en.wikipedia.org/wiki/Augmented_Backus–Naur_form
