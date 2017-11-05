Plume Web Jersey
================

This module provides multiple utilities to use Jersey, Grizzly, Servlets.

Setup
-----
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-web-jersey</artifactId>
</dependency>
```

Jackson
-------
The module `GuiceJacksonModule` provides an injectable Jackson `ObjectMapper` with common defaults, especially:
- a support for Java 8 Time objects,
- unknown attributes handling non-mandatory. 

Asynchronous web-services
-------------------------
Non blocking web-services enable to address this use case:
1. the application is using a remote API that is used in a non critical part of the application,
2. the remote API starts to answer slowly or not event at all
(the root cause can be multiple: out of memory, sql connection pool full, http connection pool full, etc.),
3. after some time the application will completely stop responding: the http connection pool will be full.

The main goal of non blocking web-services is to isolate a remote API so that if this API goes down,
the main application will not go down with it.
Plume web Jersey provides utilities functions to help setup non blocking web-services.
This utilities functions are available in `AsyncJersey` and `AsyncOkHttp`.
Do note that to use `AsyncOkHttp` the dependency to [OkHttp](https://github.com/square/okhttp) must be added manually.

Here is a usage example:
**In the API class:**
```java
public CompletableFuture<String> fetchDataAsync() {
  return AsyncOkHttp
    .executeAsync(
      // the okHttpClient should be built in the constructor
      this
        .okHttpClient
        .newCall(new Request.Builder().url("https://remote-api-host/operation").build())
    )
    // note that this apply operation will be executed by OkHttp thread pool
    // if slow instructions should be done after the HTTP response is received,
    // it should be done in a separate thread pool, with thenApplyAsync(function, executor)
    .thenApply(AsyncOkHttp.wrapUncheked(response -> response.body().string()));
}
```

**In the Jersey web-service class:**
```java
@GET
public void waitAsync(@Suspended final AsyncResponse asyncResponse) {
  remoteApi
    .fetchDataAsync()
    .whenComplete(AsyncJersey.toAsyncConsumer(asyncResponse));
}
```

