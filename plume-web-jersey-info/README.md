Plume Web Jersey info
================

This module provides multiple utilities to implement monitoring web-services

Setup
-----
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-web-jersey-info</artifactId>
</dependency>
```

Jackson
-------
The module `GuiceJacksonModule` provides an injectable Jackson `ObjectMapper` with common defaults, especially:
- a support for Java 8 Time objects,
- unknown attributes handling non-mandatory.

Explicit access control
-----------------------
In order to avoid leaking an API that should have been private, a Jersey feature enables to force developers to always specify the access control rule that must set for an API.

To use it, register this feature in Jersey: `resourceConfig.register(RequireExplicitAccessControlFeature.accessControlAnnotations(PublicApi.class, RestrictToAdmin.class));`
`PublicApi` and `RestrictTo` being the valid access control annotations.

Any custom annotation can be added (as long as the corresponding Jersey access control feature is configured...). In a doubt to configure the Jersey access control feature, see as an example the existing class `PermissionFeature` that checks the `RestrictTo` annotation access control.

Data validation
---------------
To validate web-service input data, an easy solution is to use `WsException`:
it is a `RuntimeException` that will be serialized into a nice error with a 400 HTTP response to the web-service consumer.
To use this feature, `WsResultExceptionMapper` must be registered in Jersey:
`resourceConfig.register(WsResultExceptionMapper.class);`

Usage example:

**Error enum definition:**
```java
public enum ProjectWsError implements WsError {
  WRONG_LOGIN_OR_PASSWORD,
}
```

**Web-service:**
```java
@POST
public void create(Use userToCreate) {
  Validators.checkRequired("Login", userToCreate.getLogin());
  Validators.checkRequired("Password", userToCreate.getPassword());
  if(!userToCreate.getPassword().equals(userToCreate.getPasswordConfirmation())) {
    throw new WsException(
      ProjectWsError.PASSWORDS_DO_NOT_MATCH,
      ImmutableList.of(
        "Password",
        "Password confirmation"
      )
    );
  }

  userService.create(userToCreate);
}
```

To have an HTTP 400 error (instead of a 500 error) when an input JSON is provided,
use `WsJacksonJsonProvider` instead of `JacksonJaxbJsonProvider` as Jersey JSON provider. 

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

