This release is the biggest Plume release, by features and by breaking changes. We strived to make the upgrade as fast as possible and as documented as possible. On most projects, the migration should not take more than 1 hour of work. However, what can be time-consuming is to upgrade other dependencies that haven't yet migrated to Jakarta EE (mostly dependency injection or Jersey web-service). If there is an upgrade issue, please [reach out](https://github.com/Coreoz/Plume/discussions).

About the beta release: no big change is expected before the final release version. This beta version has been tested, but since the changes of this release are bigger than usual, we prefer to release first a non-final version that is more likely to contain small bugs that we will fix in the final version. Please share all the issues you might encounter with this beta version.

Changelog
-------------
- Java EE -> Jakarta EE
- JUnit 4 -> JUnit 5
- Dependencies upgrade
- Java 20+ support
- Enables HikariCP and Grizzly threads pool monitoring
- Add [Jersey request max content size verification](https://github.com/Coreoz/Plume/tree/master/plume-web-jersey#content-size-limit) to improve security and avoid denial-of-service attacks
- Fix timing attack on the basic authentication service
- [Pagination for plume-db-querydsl](https://github.com/Coreoz/Plume/tree/master/plume-db-querydsl#pagination)
- [Use by standard `Clock` instead of the custom `TimeProvider`](https://github.com/Coreoz/Plume/issues/39)
- Add plume-test module
- #26 Add nullable annotations for better Kotlin integration
- Review how Swagger UI is used to enable to use the latest version of the UI

Upgrade instructions from 4.x to 5.x
-------------------------------------
These instructions are meant to be followed one step after the other.

If Plume file v1 is used, the guide will need to be followed after finishing migrating [upgrade to Plume file latest version](https://github.com/Coreoz/Plume-file/releases).

### Jakarta EE
- Either Intellij using the main menu (on the top bar): Refactor > Migrate Packages and Classes... > Java EE to Jakarta EE
- Either by running the [openrewrite migration plugin](https://docs.openrewrite.org/recipes/java/migrate/jakarta/javaxmigrationtojakarta): `mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta -Drewrite.exportDatatables=true`

Note that after the automatic upgrade, the project will not find Jakarta dependencies, that's normal, it will be added in a later step.

### JUnit 5 automatic upgrade with Intellij
If using Intellij, use: `Refactor > Migrate Packages and Classes... > Unit (4.x -> 5.0)`.
Note that this feature seems a bit buggy, and it often needs to be launched multiple times.

This automatic upgrade just does the bare minimum upgrade, mostly packages update only.
It's better than nothing, but keep in mind that some manuel update will need to be done later.

### pom.xml file update
There are multiple updates to do in the `pom.xml` file.

#### Java version
Use at least Java 17 if it is not already the case. Java 21 or Java 23 can also be used.

#### Plume version
Reference the new Plume version: `5.0.0-beta1`.

#### Swagger dependency
Replace the Swagger artifact name `swagger-jaxrs2` by `swagger-jaxrs2-jakarta` (the group id `io.swagger.core.v3` remains the same).

#### Plume file
Plume file latest version is now included in Plume dependencies (with the Jakarta upgrade), so the Plume file version should be deleted in the properties and in the dependencies.

#### Guice Bridge
The Guice exclusion from the `guice-bridge` dependency can be removed.

#### Remove javax.servlet-api
The dependency `javax.servlet-api` can be removed.

#### Test dependencies
Tests dependencies junit, assertj, mockito and `guice-junit-test-runner<` can be replaced by:
plume-db-test

#### Plugin versions
Maven plugins versions can be updated, especially if there is an incompatibility with the Java version used.

For example:
- maven-compiler-plugin
- maven-resources-plugin
- maven-jar-plugin
- maven-javadoc-plugin

#### Flyway specific module
Flyway uses a modular system since v10, if used:
- The module `flyway-core` must be included
- An additional module may be required depending on the database used, for example:
  - `flyway-mysql` for MySQL
  - `flyway-database-postgresql` for PostgreSQL

So the correct module depending on the database used must be referenced. See [the list of available module](https://github.com/flyway/flyway/issues/3780) to choose the module to add. 

The versions of Flyway modules for MySQL, PostgreSQL, Oracle and SQL Server are referenced in Plume dependency POM. So for example, with MySQL it can be used like this:
```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-mysql</artifactId>
</dependency>
```

### Swagger upgrade
Swagger has been upgraded, and it is not possible anymore to use the query parameter `url`: https://github.com/swagger-api/swagger-ui/issues/7702

To overcome this, Swagger UI is now loaded directly from the `index.html` file of the project using webjars.
This file generally contained this:
```html
API Swagger documentation:
        <a href="webjars/swagger-ui/4.1.2/index.html?url=/api/swagger">
            webjars/swagger-ui/4.1.2/index.html?url=/api/swagger
        </a>
```
And now the file should be updated with:
```html
<head>
  <!-- current head tag content should remain unchanged -->

  <!-- head tag content to be added: new links to updated swagger-ui -->
  <script src="webjars/swagger-ui/5.17.14/swagger-ui-bundle.js" charset="UTF-8"></script>
  <script src="webjars/swagger-ui/5.17.14/swagger-ui-standalone-preset.js" charset="UTF-8"></script>
  <link rel="stylesheet" href="webjars/swagger-ui/5.17.14/swagger-ui.css">
</head>
<body>
  <div id="swagger-ui"></div>
  <script>
    SwaggerUIBundle({
      url: "/api/swagger",
      dom_id: '#swagger-ui',
      presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIStandalonePreset
      ],
      layout: "StandaloneLayout"
    });
  </script>
</body>
```

After the update, Swagger UI will be accessible on the URL http://localhost:8080/ instead of http://localhost:8080/webjars/swagger-ui/4.1.2/index.html?url=/api/swagger

### TimeProvider deprecated to use Clock instead
The `TimeProvider` class has been deprecated: it was a custom solution that wasn't easy to use in non-Plume projects and that is actually resolved by the Java based standard `Clock` object.

Replace the use of `TimeProvider` by the `Clock` object:
- `timeProvider.currentTime()` => `clock.millis()`
- `timeProvider.currentInstant()` => `clock.instant()`
- `timeProvider.currentLocalDate()` => `LocalDate.now(clock)`
- `timeProvider.currentDateTime()` => `LocalDateTime.now(clock)`

To facilitate testing the `Clock` usage, the [Plume test module](https://github.com/Coreoz/Plume/tree/master/plume-test) can be used.
It contains especially the `MockedClock` class that provides methods to change the time in the injected `Clock` object.

### Finish JUnit 5 upgrade
#### Basic JUnit 5 upgrade
If the automatic upgrade has been performed, this step will be easier.

Follow the [official JUnit migration guide](https://junit.org/junit5/docs/current/user-guide/#migrating-from-junit4-tips).

One migration case that is often encountered is the test exception handling:
- JUnit 4: `@Test(expected = SomeException.class)`
- JUnit 5 (see the [stackoverflow Q/A](https://stackoverflow.com/a/40268447) for details):
```java
MyException thrown = assertThrows(
    SomeException.class,
    () -> myObject.doThing(),
    "Expected doThing() to throw, but it didn't"
);

assertTrue(thrown.getMessage().contains("Stuff"));
```

#### Guice runner upgrade
The new package [Guice JUnit](https://github.com/Coreoz/Guice-Junit) has been included in Plume to run easily JUnit 5 tests that require Guice dependency members. This replaces the [non maintained JUnit 4 Guice integration](https://github.com/caarlos0-graveyard/guice-junit-test-runner) referenced by Plume < v5.0.0. The migration to use Guice JUnit is straightforward:
- Remove the line `@RunWith(GuiceTestRunner.class)` from tests
- Replace the line `@GuiceModules(MyModule.class)` by `@GuiceTest(MyModule.class)`

### Improved security: request max content size verification
A new Jersey feature has been added to limit the size of body requests. This feature mitigates the risk of denial of service.

See [Plume Jersey documentation](https://github.com/Coreoz/Plume/tree/master/plume-web-jersey#content-size-limit) to enable it.

Enabling this feature will improve the robustness of the application, but it can also lead to regressions: some API might require supporting large body requests. So after setting this up:
- A review should be made to try to identify APIs that require large body requests to correctly configure the max content size
- A full testing of the application should be performed

Enabling this feature has a frontend impact: the new I18N error key `CONTENT_SIZE_LIMIT_EXCEEDED` will be returned when the request content size exceeds the limit.

### JJwt update
If JJwt is manipulated directly, some changes are required: https://github.com/jwtk/jjwt/blob/0.12.0/CHANGELOG.md

A migration sample can be found in the [Plume Admin JJwt migration](https://github.com/Coreoz/Plume-admin/commit/2b64ca61ddb30f49159aa2a3c3bfed9ebf736a9c).

### Transaction manager
No migration is required except if `TransactionManager` or `TransactionManagerQuerydsl` are created manually.

In `QueryDSLGenerator`, the data source must be provided in the Guice injector creation: `Injector injector = Guice.createInjector(new GuiceConfModule(), new DataSourceModule());`

Moreover, if `TransactionManager` or `TransactionManagerQuerydsl` were created manually (for example, if multiple databases are used), their creation must be updated using the `HikariDataSources.fromConfig()` method, e.g. : `HikariDataSources.fromConfig(config, "db.hikari")` to create the `DataSource` argument.

### QuerydslGenerator
The `DataSource` module is now separated from the `TransactionManager`, so:
- The line: `Injector injector = Guice.createInjector(new GuiceConfModule());`
- Must be replaced by: `Injector injector = Guice.createInjector(new GuiceConfModule(), new DataSourceModule());`

### Monitoring
HikariCP threads pool and Grizzly threads pool can now be easily monitored.
To do that:
- In `GrizzlySetup`, in the `start()` method:
    - add the following dependency in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe`
    - declare the prob in the http server configuration: `httpServer.getServerConfiguration().getMonitoringConfig().getThreadPoolConfig().addProbes(grizzlyThreadPoolProbe);`
- In `MonitoringWs`, in the constructor:
    - add the following dependencies in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe` and `HikariDataSource hikariDataSource`
    - Use the metrics: `this.metricsStatusProvider = new MetricsCheckBuilder().registerJvmMetrics().registerGrizzlyMetrics(grizzlyThreadPoolProbe).registerHikariMetrics(hikariDataSource).build()`
