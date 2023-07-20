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

Features
-------

### HealthCheckBuilder
The `HealthCheckBuilder` provides a simple API to monitor the health status of your application.

- `registerHealthCheck`: Register your health checks
- `registerDatabaseHealthCheck`: Register the built-in database health check
- `build`: create a health status provider

Usage example:

**Web-service**
```java
@Path("/monitor/health")
@Singleton
public class HealthWs {
    private static final Logger logger = LoggerFactory.getLogger(HealthWs.class);

    private final Provider<HealthStatus> healthStatusProvider;

    @Inject
    public HealthWs(TransactionManager transactionManager, HealthCheckService healthCheckService) {
        this.healthStatusProvider = new HealthCheckBuilder()
            .registerDatabaseHealthCheck()
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HealthStatus get(@Context ContainerRequestContext requestContext) {
        return this.healthStatusProvider.get();
    }
}
```

### InfoService
The `InfoService` provides a unique getter that retrieves the basic application information from the `Pom.xml` :
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

### MetricsCheckBuilder
The `MetricsCheckBuilder` uses the [io.dropwizard.metrics](https://github.com/dropwizard/metrics) library
to provide some basic functionality for monitoring your application's metrics (CPU usage, memory usage, ...).

Exposed API :
- `registerMetric`: Register metrics to monitor
- `registerJvmMetrics`: Register the basic JVM metrics to monitor
- `build`: create a metrics provider that provides the status of the metrics that are monitored.

Usage example:

**Web-service**
```java
@Path("/monitor/metrics")
@Singleton
public class MetricsWs {
    private final JerseyInfoObjectMapper objectMapper = JerseyInfoObjectMapper.get();
    private final Provider<Map<String, Metric>> metricsStatusProvider;

    @Inject
    public MetricsWs(MetricsService metricsService) {
        this.metricsStatusProvider = new MetricsCheckBuilder()
            .registerJvmMetrics()
            .build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context ContainerRequestContext requestContext) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "metrics", metricsStatusProvider.get()
        ));
    }
}
```

### JerseyInfoObjectMapper
Provides an ObjectMapper to serialize the types provided by plume-web-jersey-info services.

Usage example:

```java
    private final ObjectMapper objectMapper = JerseyInfoObjectMapper.get();
```
