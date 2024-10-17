Plume Web Jersey
================

This module provides multiple utilities to use Jersey, Grizzly and Servlets.

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

Explicit access control
-----------------------
In order to avoid leaking an API that should have been private, a Jersey feature enables to force developers to always specify the access control rule that must set for an API.

To use it, register this feature in Jersey: `resourceConfig.register(RequireExplicitAccessControlFeature.accessControlAnnotations(PublicApi.class, RestrictToAdmin.class));`
`PublicApi` and `RestrictTo` being the valid access control annotations.

Any custom annotation can be added (as long as the corresponding Jersey access control feature is configured...). In a doubt to configure the Jersey access control feature, see as an example the existing class `PermissionFeature` that checks the `RestrictTo` annotation access control.

Content size limit
------------------
In order to protect the backend against attack that would send huge content, it is possible to limit the size of the content that can be sent to the backend.

To do so, register the `ContentSizeLimitFeature` in Jersey: `resourceConfig.register(ContentSizeLimitFeature.class);`
By default the content size of body is limited to 500 KB. This limit can be overridden for the whole api by using the `ContentSizeLimitFeatureFactory` to specify your own limit.

Usage example:
```java
resourceConfig.register(new AbstractBinder() {
    @Override
    protected void configure() {
        bindFactory(new ContentSizeLimitFeatureFactory(1000 * 1024 /* 1MB */)).to(ContentSizeLimitFeature.class);
    }
});
```

You can also override only a specific endpoint by using the `@ContentSizeLimit` annotation:
```java
@POST
	@Path("/test")
	@Operation(description = "Example web-service")
    @ContentSizeLimit(1024 * 1000 * 5) // 5MB
	public void test(Test test) {
        logger.info("Test: {}", test.getName());
	}
```

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

Requests authorization
----------------------
Multiple authorization provider are implemented.
For all provider, the HTTP header `Authorization` is used to fetch the authorization value.

### Basic authorization
This feature is provided by the `BasicAuthenticator` class.
Sample usage:
```java
@Path("/example")
@Tag(name = "example", description = "Manage exemple web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// Since we are performing authorization in the resource, we can mark this API as public
@PublicApi
@Singleton
public class ExampleWs {
    private final BasicAuthenticator<String> basicAuthenticator;

	@Inject
	public ExampleWs() {
        this.basicAuthenticator = BasicAuthenticator.fromSingleCredentials(
            "my-username",
            "my-password",
            // Message displayed to the user trying to access the API with a browser
            "My protected API"
        );
	}

	@GET
	@Path("/test")
	@Operation(description = "Example web-service")
	public Test test(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);

		return new Test("Hello world");
	}
}
```

One sample valid request: `curl -u 'my-username:my-password' 'http://localhost:8080/api/example/test'`

### API Key authorization
This feature is provided by the `BearerAuthenticator` class.
Sample usage:
```java
@Path("/example")
@Tag(name = "example", description = "Manage exemple web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// Since we are performing authorization in the resource, we can mark this API as public
@PublicApi
@Singleton
public class ExampleWs {
    private final BearerAuthenticator bearerAuthenticator;

    @Inject
    public ExampleWs() {
        this.bearerAuthenticator = new BearerAuthenticator("my-bearer-token");
    }

    @GET
    @Path("/test")
    @Operation(description = "Example web-service")
    public Test test(@Context ContainerRequestContext requestContext) {
        bearerAuthenticator.verifyAuthentication(requestContext);

        return new Test("Hello world");
    }
}
```

One sample valid request: `curl -H 'Authorization: Bearer my-bearer-token' 'http://localhost:8080/api/example/test'`

### Resource authorization based on annotation
This feature is provided `AuthorizationSecurityFeature` and works with any authentication system.
For example using bearer auth:
- In the `JerseyConfigProvider` file, declare the feature with the annotation used for resource identification: `config.register(new BearerAuthenticator("my-bearer-token").toAuthorizationFeature(BearerRestricted.class));`
- In the `JerseyConfigProvider` file, register if needed the annotation used in the `RequireExplicitAccessControlFeature`: `config.register(RequireExplicitAccessControlFeature.accessControlAnnotations(PublicApi.class, BearerRestricted.class));`
- Use the annotation in a resource definition:
```java
@Path("/example")
@Tag(name = "example", description = "Manage exemple web-services")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// The new annotation that will ensure the authorization process before granting access
@BearerRestricted
@Singleton
public class ExampleWs {
    @GET
    @Path("/test")
    @Operation(description = "Example web-service")
    public Test test() {
        return new Test("Hello world");
    }
}
```

### Permissions based authorization
TODO to details
