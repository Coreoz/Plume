Plume Web Jersey info
================

This module provides utilities to expose backend monitoring API similarly to what is offered by Spring Actuator:
- `/health`
- `/info`
- `/metrics`

Setup
-----
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-web-jersey-info</artifactId>
</dependency>
```

Services
-------

### HealthCheckService
The `HealthCheckService` provides a simple API to monitor the health status of your application.

- `registerHealthCheck`: Register your health checks
- `isHealthy`: run the registered health checks and return the health status.

Usage example:

**Web-service**
```java
@Path("/monitor/health")
@Singleton
public class HealthWs {
    private static final Logger logger = LoggerFactory.getLogger(HealthWs.class);

    private final HealthCheckService healthCheckService;
    private final ObjectMapper objectMapper = PlmWebJerseyInfoObjectMapperProvider.get();

    @Inject
    public HealthWs(TransactionManager transactionManager, HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
        healthCheckService.registerHealthCheck("database", new DatabaseHealthCheck(transactionManager));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HealthStatus get(@Context ContainerRequestContext requestContext) throws JsonProcessingException {
        return this.healthCheckService.isHealthy();
    }
}
```

### InfoCheckService
The `InfoCheckService` provides a unique getter that retrieves the basic application information from the `Pom.xml` :
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

Usage example:

**Web-service**
```java
@Path("/monitor/info")
@Singleton
public class InfoWs {
    private final InfoService infoService;

    @Inject
    public InfoWs(InfoService infoService) {
        this.infoService = infoService;
    }

    @GET
    public String get(@Context ContainerRequestContext requestContext) {
        return this.infoService.getAppInfo();
    }
}
```

### MetricsService
The `MetricsService` uses the [io.dropwizard.metrics](https://github.com/dropwizard/metrics) library
to provide some basic functionality for monitoring your application's metrics (CPU usage, memory usage, ...).

Exposed API :
- `registerMetric`: Register metrics to monitor
- `getMetrics`: Provides the metrics that are monitored.

Usage example:

**Web-service**
```java
@Path("/monitor/metrics")
@Singleton
public class MetricsWs {
    private final MetricsService metricsService;
    private final PlmWebJerseyInfoObjectMapper objectMapper = new PlmWebJerseyInfoObjectMapper();

    @Inject
    public MetricsWs(MetricsService metricsService) {
        this.metricsService = metricsService;

        metricsService.registerMetric("memory-usage", new MemoryUsageGaugeSet());
        metricsService.registerMetric("thread-states", new ThreadStatesGaugeSet());
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context ContainerRequestContext requestContext) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "metrics", metricsService.getMetrics()
        ));
    }
}
```


Built-in HealthChecks
-------

### DatabaseHealthCheck
Check the health of your connection to the database.

Usage example:

```java
@Inject
    public HealthWs(TransactionManager transactionManager, HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
        healthCheckService.registerHealthCheck("database", new DatabaseHealthCheck(transactionManager));
    }
```

### PlmWebJerseyInfoObjectMapperProvider
Provides an ObjectMapper to serialize the types provided by plume-web-jersey-info services.

Usage example:

```java
    private final PlmWebJerseyInfoObjectMapper objectMapper = new PlmWebJerseyInfoObjectMapper();
```
