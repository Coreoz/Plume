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

### Add the monitoring API
See [monitoring usage example](#usage-example).

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
- `registerGrizzlyMetrics`: Register metrics for Grizzly HTTP threads pool, see [Jersey module](../plume-web-jersey) for usage documentation on `GrizzlyThreadPoolProbe` 
- `registerHikariMetrics`: Register metrics for HikariCP SQL connections pool
- `build`: create a metrics provider that provides the status of the metrics that are monitored.


Usage example
-------

**Web-service**

```java
@Path("/monitoring")
// Authentication is done directly by the web service without any annotation
@PublicApi
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class MonitoringWs {
    private final ApplicationInfo applicationInfo;
    private final Provider<HealthStatus> healthStatus;
    private final Provider<Map<String, Metric>> metrics;
    private final ObjectWriter metricsJsonWriter;

    private final BasicAuthenticator<String> basicAuthenticator;

    @Inject
    public MonitoringWs(
        ApplicationInfoProvider applicationInfoProvider,
        TransactionManager transactionManager,
        GrizzlyThreadPoolProbe grizzlyThreadPoolProbe,
        HikariDataSource hikariDataSource,
        InternalApiAuthenticator apiAuthenticator,
        JerseyMonitoringObjectMapperProvider metricsObjectMapperProvider
    ) {
        this.applicationInfo = applicationInfoProvider.get();
        // Registering health checks
        this.healthStatus = new HealthCheckBuilder()
            .registerDatabaseHealthCheck(transactionManager)
            .build();

        // Registering metrics to monitor
        this.metrics = new MetricsCheckBuilder()
            .registerJvmMetrics()
            .registerGrizzlyMetrics(grizzlyThreadPoolProbe)
            .registerHikariMetrics(hikariDataSource)
            .build();

        // Require authentication to access monitoring endpoints
        this.basicAuthenticator = apiAuthenticator.get();
        this.metricsJsonWriter = metricsObjectMapperProvider.get().writer();
    }

    @GET
    @Path("/info")
    @SneakyThrows
    public String info(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return metricsJsonWriter.writeValueAsString(this.applicationInfo);
    }

    @GET
    @Path("/health")
    @SneakyThrows
    public String health(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return metricsJsonWriter.writeValueAsString(this.healthStatus.get());
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public String metrics(@Context ContainerRequestContext requestContext) {
        basicAuthenticator.requireAuthentication(requestContext);
        return metricsJsonWriter.writeValueAsString(metrics.get());
    }
}
```
