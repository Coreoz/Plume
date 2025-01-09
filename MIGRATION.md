This release is the biggest Plume release, by features and by breaking changes. We strived to make the upgrade as fast as possible and as documented as possible. On most projects, the migration should not take more than 1 hour of work. However, what can be time-consuming is to upgrade other dependencies that have not yet migrated to Jakarta EE (mostly dependency injection or Jersey web-service). If there is an upgrade issue, please [reach out](https://github.com/Coreoz/Plume/discussions).

About the beta release: no big change are expected before the final release version. This beta version has been tested, but since the changes of this release are bigger than usual, we prefer to release first a non-final version that is more likely to contain small bugs that we will fix in the final version. Please share all the issues you might encounter with this beta version.

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

Breaking changes
----------------
### Java 17
The minimum required Java version is now 17.

### 2️⃣ Migration from Java EE to Jakarta EE
This migration can mainly be done automatically with:
- Either Intellij using the main menu (on the top bar): Refactor > Migrate Packages and Classes... > Java EE to Jakarta EE
- Either by running the [openrewrite migration plugin](https://docs.openrewrite.org/recipes/java/migrate/jakarta/javaxmigrationtojakarta): `mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta -Drewrite.exportDatatables=true` ⚠️ This command line must be executed before upgrading plume version in the `pom.xml` file

Note that dependency `javax.servlet-api` should now be deleted in the `pom.xml` file since it should not be used anymore.

`swagger-jaxrs2` artifact must also be changed in the `pom.xml` file to use the Jakarta version `swagger-jaxrs2-jakarta`:
```xml
<dependency>
  <groupId>io.swagger.core.v3</groupId>
  <artifactId>swagger-jaxrs2-jakarta</artifactId>
</dependency>
```

### Swagger upgrade
Swagger has been upgraded and it is not possible anymore to use the query parameter `url`: https://github.com/swagger-api/swagger-ui/issues/7702

To overcome this, Swagger UI is now loaded directly from the `index.html` file of the project using webjars.
This file was generally containing this:
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

### 3️⃣ JUnit 5 migration
Plume now uses only JUnit 5.
This migration has been covered in many guides, for example https://blog.jetbrains.com/idea/2020/08/migrating-from-junit-4-to-junit-5/ or https://docs.openrewrite.org/running-recipes/popular-recipe-guides/migrate-from-junit-4-to-junit-5

Intellij also provide a feature that automates most of this: `Refactor > Migrate Packages and Classes... > Unit (4.x -> 5.0)`.  Note that this feature seems a bit buggy and it often needs to be launched multiple times. Moreover, `@Test(expected = SomeException.class)` is not migrated automatically, for that, [the stackoverflow Q/A](https://stackoverflow.com/a/40268447) can be followed.

Note that the new package [Guice JUnit](https://github.com/Coreoz/Guice-Junit) has been included in Plume to run easily JUnit 5 tests that requires Guice dependency members. This replaces the [non maintained JUnit 4 Guice integration](https://github.com/caarlos0-graveyard/guice-junit-test-runner) referenced by Plume < v5.0.0. The migration to use Guice JUnit is straightforward:
- Remove the line `@RunWith(GuiceTestRunner.class)` from tests
- Replace the line `@GuiceModules(MyModule.class)` by `@GuiceTest(MyModule.class)`

### TimeProvider deprecated to use Clock instead
The `TimeProvider` class has been deprecated: it was a custom solution that wasn't easy to use in non Plume project and that is actually resolved by the Java based standard `Clock` object.

To facilitate the use of the `Clock` object, the [Plume test module](https://github.com/Coreoz/Plume/tree/master/plume-test) can be used. It contains especially the `MockedClock` class.

When using [Plume Admin](https://github.com/Coreoz/Plume-admin), here are the files that have switched to using `Clock` instead of `TimeProvider`:
- `AdminUserService`
- `JwtSessionSigner`
- `JwtSessionSignerProvider`
- `LogApiService`
- `SessionWs`

### Improved security: request max content size verification
A new Jersey feature has been added to limit the size of body requests. This feature mitigates risk of denial of service.

See [Plume Jersey documentation](https://github.com/Coreoz/Plume/tree/master/plume-web-jersey#content-size-limit) to enable it.

Enabling this feature will improve the robustness of the application, but it can also lead to regressions: some API might require to support large body requests. So after setting this up:
- A review should be made to try to identify API that require large body requests to configure correctly the max content size
- A full testing of the application should be performed

### Plume file update
If [Plume file](https://github.com/Coreoz/Plume-file) is used, its updated version number is now configured in [Plume dependencies](https://github.com/Coreoz/Plume-dependencies).

This means that referenced Plume file version should be removed in the `pom.xml` file. If this changes is not applied, Plume file version must be updated to work correctly with the Java EE to Jakarta EE update.

For Plume file v1 usage, please [upgrade to Plume file latest version](https://github.com/Coreoz/Plume-file/releases).

### JJwt update
If JJwt is manipulated directly, some changes are required: https://github.com/jwtk/jjwt/blob/0.12.0/CHANGELOG.md

A migration sample can be found in the [Plume Admin JJwt migration](https://github.com/Coreoz/Plume-admin/commit/2b64ca61ddb30f49159aa2a3c3bfed9ebf736a9c).

### Transaction manager
`TransactionManager` and `TransactionManagerQuerydsl` are not creating by HikariCP pool by themselves anymore. Instead, they rely on a `DataSource` object. This `DataSource` can be created using the `HikariDataSources.fromConfig()` method, e.g. : `HikariDataSources.fromConfig(config, "db.hikari")`.

### Monitoring
HikariCP threads pool and Grizzly threads pool can now be easily monitored.
To do that:
- In `GrizzlySetup`, in the `start()` method:
    - add the following dependency in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe`
    - declare the prob in the http server configuration: `httpServer.getServerConfiguration().getMonitoringConfig().getThreadPoolConfig().addProbes(grizzlyThreadPoolProbe);`
- In `MonitoringWs`, in the constructor:
    - add the following dependencies in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe` and `HikariDataSource hikariDataSource`
    - Use the metrics: `this.metricsStatusProvider = new MetricsCheckBuilder().registerJvmMetrics().registerGrizzlyMetrics(grizzlyThreadPoolProbe).registerHikariMetrics(hikariDataSource).build()`

Upgrade instructions from 4.x to 5.x
-------------------------------------
These instructions are meant to be followed one step after the other.

If Plume file v1 is used, the guide will need to be followed after finishing migrating [upgrade to Plume file latest version](https://github.com/Coreoz/Plume-file/releases).

### Jakarta EE
- Either Intellij using the main menu (on the top bar): Refactor > Migrate Packages and Classes... > Java EE to Jakarta EE
- Either by running the [openrewrite migration plugin](https://docs.openrewrite.org/recipes/java/migrate/jakarta/javaxmigrationtojakarta): `mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE -Drewrite.activeRecipes=org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta -Drewrite.exportDatatables=true`

Note that after the automatic upgrade, the project will not find Jakarta dependencies, that's normal, it will be added in a later step.

### JUnit 5 automatic upgrade
If using Intellij, use: `Refactor > Migrate Packages and Classes... > Unit (4.x -> 5.0)`.  Note that this feature seems a bit buggy and it often needs to be launched multiple times. Moreover, `@Test(expected = SomeException.class)` is not migrated automatically, for that, [the stackoverflow Q/A](https://stackoverflow.com/a/40268447) can be followed.

Note that this automatic upgrade just does the bare minimum upgrade, mostly packages update only. It's better than nothing, but keep in mind that some manuel update will need to be done later.

### pom.xml file update
There are multiple updates to do in the `pom.xml` file.

#### Java version
Use at least Java 17 if it is not already the case. Java 21 or Java 23 can also be used.

#### Plume version
Reference the new Plume version: `5.0.0-beta1`.

#### Swagger dependency
Replace the Swagger artifact name `swagger-jaxrs2` by `swagger-jaxrs2-jakarta` (the group id `io.swagger.core.v3` remains the same).

#### Plume file
Plume file latest version is now included in Plume dependencies (with the Jakarta upgrade), so Plume file version should be deleted in the properties and in the dependencies.

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

### Swagger upgrade
Swagger has been upgraded and it is not possible anymore to use the query parameter `url`: https://github.com/swagger-api/swagger-ui/issues/7702

To overcome this, Swagger UI is now loaded directly from the `index.html` file of the project using webjars.
This file was generally containing this:
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
Replace the use of `TimeProvider` by the `Clock` object:
- `timeProvider.currentTime()` => `clock.millis()`
- `timeProvider.currentInstant()` => `clock.instant()`
- `timeProvider.currentLocalDate()` => `LocalDate.now(clock)`
- `timeProvider.currentDateTime()` => `LocalDateTime.now(clock)`

To facilitate testing the `Clock` usage, the [Plume test module](https://github.com/Coreoz/Plume/tree/master/plume-test) can be used. It contains especially the `MockedClock` class.

### Improved security: request max content size verification
Enable this security feature following [Plume Jersey documentation](https://github.com/Coreoz/Plume/tree/master/plume-web-jersey#content-size-limit).

### JJwt update
If JJwt is manipulated directly, some changes are required: https://github.com/jwtk/jjwt/blob/0.12.0/CHANGELOG.md

A migration sample can be found in the [Plume Admin JJwt migration](https://github.com/Coreoz/Plume-admin/commit/2b64ca61ddb30f49159aa2a3c3bfed9ebf736a9c).

### Transaction manager
In `QueryDSLGenerator`, the data source must be provided in the Guice injector creation: `Injector injector = Guice.createInjector(new GuiceConfModule(), new DataSourceModule());`

Moreover, if `TransactionManager` or `TransactionManagerQuerydsl` were created manually (for example if multiple databases are used), their creation must be updated using the `HikariDataSources.fromConfig()` method, e.g. : `HikariDataSources.fromConfig(config, "db.hikari")` to create the `DataSource` argument.

### Monitoring
HikariCP threads pool and Grizzly threads pool can now be easily monitored.
To do that:
- In `GrizzlySetup`, in the `start()` method:
    - add the following dependency in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe`
    - declare the prob in the http server configuration: `httpServer.getServerConfiguration().getMonitoringConfig().getThreadPoolConfig().addProbes(grizzlyThreadPoolProbe);`
- In `MonitoringWs`, in the constructor:
    - add the following dependencies in the method signature: `GrizzlyThreadPoolProbe grizzlyThreadPoolProbe` and `HikariDataSource hikariDataSource`
    - Use the metrics: `this.metricsStatusProvider = new MetricsCheckBuilder().registerJvmMetrics().registerGrizzlyMetrics(grizzlyThreadPoolProbe).registerHikariMetrics(hikariDataSource).build()`
