Plume Scheduler
===============

Integrate [Wisp Scheduler](https://github.com/Coreoz/Wisp)
is a Java job scheduler that execute recurring Java tasks.

This module provides a `Scheduler` object through dependency injection.

The full documentation about Wisp Scheduler is available at:
<https://github.com/Coreoz/Wisp>.

Getting started
---------------
Include Plume Scheduler in your project:
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-scheduler</artifactId>
</dependency>
```

Create a `ScheduledJobs` class:
```java
@Singleton
public class ScheduledJobs {

    private final MyService service1;
    private final MyOtherService service2;

    @Inject
    public ScheduledJobs(Scheduler scheduler, MyService service1, MyOtherService service2) {
        this.service1 = service1;
        this.service2 = service2;
    }
    
    public void scheduleJobs() {
        scheduler.schedule(
            "My service job",
            service1::processToBeExecuted,
            Schedules.executeAt("03:30")
        );
        scheduler.schedule(
            "My other service job",
            service2::otherProcessToBeExecuted,
            Schedules.fixedDelaySchedule(Duration.ofSeconds(40))
        );
        scheduler.schedule(
            "Long running job monitor",
            new LongRunningJobMonitor(scheduler),
            Schedules.fixedDelaySchedule(Duration.ofMinutes(1))
        );
    }

}
```

Install the Scheduler module and load the jobs in your application:

```java
install(new GuiceSchedulerModule());
```

In your application main method `WebApplication.main()` start the jobs:

```java
injector.getInstance(ScheduledJobs.class).scheduleJobs();
```

