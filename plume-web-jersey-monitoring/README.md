Plume Web Jersey info
================

This module provides utilities to expose backend monitoring API similarly to what is offered by Spring Actuator:
- `/health`
- `/info`
- `/metrics`

Setup
-----
### Maven dependency
Add the dependency to the `pom.xml` file
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-web-jersey-monitoring</artifactId>
</dependency>
```

### Install Guice modules
In your application module :
1. replace `GuiceJacksonModule`by the `GuiceJacksonWithMetricsModule`
2. install the `GuiceJerseyMonitoringModule`

~~`install(new GuiceJacksonModule());`~~
```java
install(new GuiceJacksonWithMetricsModule());
install(new GuiceJerseyMonitoringModule());
```

Features
-------

### HealthCheckBuilder
The `HealthCheckBuilder` provides a simple API to monitor the health status of your application.

- `registerHealthCheck`: Register your health checks
- `registerDatabaseHealthCheck`: Register the built-in database health check
- `build`: create a health status provider


### ApplicationInfo
The `ApplicationInfo` is a singleton object available through dependency injection.
It contains the basic application information retrieved from the `Pom.xml` file.

`ApplicationInfo` content: 
- name
- description
- version
- additional information retrieved from the configuration file

Additional information can be added using the configuration file as follows :
```
plm-web-jersey-info = {
    info = {
        my-custom-field = "custom aplication detail"
    }
}
```

### MetricsCheckBuilder
The `MetricsCheckBuilder` uses the [io.dropwizard.metrics](https://github.com/dropwizard/metrics) library
to provide some basic functionality for monitoring your application's metrics (CPU usage, memory usage, ...).

Exposed API :
- `registerMetric`: Register metrics to monitor
- `registerJvmMetrics`: Register the basic JVM metrics to monitor
- `build`: create a metrics provider that provides the status of the metrics that are monitored.


Usage example
-------

**Web-service**

```java
import com.coreoz.plume.jersey.security.permission.PublicApi;

@Path("/monitor")
// Authentication is done directly by the web service without any annotation
@PublicApi
@Singleton
public class MonitoringWs {
    private final ApplicationInfo applicationInfo;
    private final Provider<HealthStatus> healthStatusProvider;
    private final Provider<Map<String, Metric>> metricsStatusProvider;

    private final BasicAuthenticator<String> basicAuthenticator;

    @Inject
    public MonitoringWs(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
        // Registering health checks
        this.healthStatusProvider = new HealthCheckBuilder()
            .registerDatabaseHealthCheck()
            .build();

        // Registering metrics to monitor
        this.metricsStatusProvider = new MetricsCheckBuilder()
            .registerJvmMetrics()
            .build();

        // require authentication to access the supervision URL
        this.basicAuthenticator = BasicAuthenticator.fromSingleCredentials(
            "SINGLE_USERNAME",
            "PASSWORD",
            "MY_REALM"
        );
    }

    @GET
    @Path("/info")
    public ApplicationInfo get(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return this.infoService.getAppInfo();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public HealthStatus get(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return this.healthStatusProvider.get();
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return metricsStatusProvider.get();
    }
}
```
