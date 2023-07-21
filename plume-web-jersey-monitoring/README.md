Plume Web Jersey monitoring
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
In your application module replace `GuiceJacksonModule`by the `GuiceJacksonWithMetricsModule`

~~`install(new GuiceJacksonModule());`~~
```java
install(new GuiceJacksonWithMetricsModule());
```

Features
-------

### HealthCheckBuilder
The `HealthCheckBuilder` provides a simple API to monitor the health status of your application.

- `registerHealthCheck`: Register your health checks
- `registerDatabaseHealthCheck`: Register the built-in database health check
- `build`: create a health status provider


### ApplicationInfoProvider
The `ApplicationInfoProvider` provides an instance of `ApplicationInfo`.
It contains the basic application information retrieved from the `pom.xml` file.

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
@Path("/monitor")
// Authentication is done directly by the web service without any annotation
@PublicApi
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MonitoringWs {
    private final ApplicationInfo applicationInfo;
    private final Provider<HealthStatus> healthStatusProvider;
    private final Provider<Map<String, Metric>> metricsStatusProvider;

    private final BasicAuthenticator<String> basicAuthenticator;

    @Inject
    public MonitoringWs(ApplicationInfoProvider applicationInfoProvider, TransactionManager transactionManager) {
        this.applicationInfo = applicationInfoProvider.get();
        // Registering health checks
        this.healthStatusProvider = new HealthCheckBuilder()
            .registerDatabaseHealthCheck(transactionManager)
            .build();

        // Registering metrics to monitor
        this.metricsStatusProvider = new MetricsCheckBuilder()
            .registerJvmMetrics()
            .build();

        // require authentication to access the supervision URL
        this.basicAuthenticator = BasicAuthenticator.fromSingleCredentials(
            "plume",
            "rocks",
            "Plume showcase"
        );
    }

    @GET
    @Path("/info")
    public ApplicationInfo info(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return this.applicationInfo;
    }

    @GET
    @Path("/health")
    public HealthStatus health(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return this.healthStatusProvider.get();
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Metric> metrics(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return metricsStatusProvider.get();
    }
}
```
