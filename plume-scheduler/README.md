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
</dependency
```

Create a `ScheduledJobs` class:
```java
@Singleton
public class ScheduledJobs {

    @Inject
    public ScheduledJobs(Scheduler scheduler, MyService service1, MyOtherService service2) {
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
    }

}
```

Install the Scheduler module and load the jobs in your application:

```java
install(new GuiceSchedulerModule());
bind(ScheduledJobs.class).asEagerSingleton();
```

