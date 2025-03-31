Plume
=====
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.coreoz/plume-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.coreoz/plume-parent)

Plume is an encapsulation of the best available JVM libraries to get a project up and running quickly.

Key concepts:
- Plume has few main principles/dependencies: configuration via [Config](https://github.com/typesafehub/config) and dependency injection via [Guice](https://github.com/google/guice) or [Dagger](https://github.com/google/dagger).
- Plume is the glue made with Config and Guice/Dagger to make popular open source libraries work together: Jersey, QueryDsl, HikariCP, etc.
- Plume is lightweight and modular.

A concrete example of how Plume works is the [Plume Mail](plume-mail/) module:
- It provides the [Simple Java Mail](http://www.simplejavamail.org/) dependency
- This module exposes 50 lines of code via the [MailerProvider](plume-mail/src/main/java/com/coreoz/plume/mail/MailerProvider.java) class:
    - In the constructor, it populates the config object from Simple Mail Java using [Config](https://github.com/typesafehub/config)
    - The `MailerProvider` class implements the `jakarta.inject.Provider` interface to expose the Simple Mail Java `Mailer` object.

In this way, it is easy to replace most of the components suggested by Plume. The only "strong" requirement is to provide dependency injection objects using `jakarta.inject`.

Plume is maintained by [Coreoz](http://coreoz.com/)
and licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Maintenance
-----------
In developing Plume, we strive to keep the project and its dependencies as small and lightweight as possible. Making Plume lightweight helps reduce the complexity of the system and the risk of security issues. This means that Plume will help projects have the lowest maintenance and evolution costs.

In addition:
- Coreoz has been maintaining Plume since 2016, and Coreoz will continue to maintain Plume and provide migration guides for each release.
- Plume sources are published to Maven Central: so they will always be available one way or another.

Demo
----
Sample projects can be found here: <https://github.com/Coreoz/Plume-showcase>.

Getting started
---------------
The best way to get started is to use a
[Maven archetype for Plume](https://github.com/Coreoz/Plume-archetypes).

Plume requires at least Java 17.

Dependency injection
--------------------
If you are not familiar with the dependency injection concept, read the
[Guice Motivation Guice](https://github.com/google/guice/wiki/Motivation).

Dependency injection takes a central place in Plume.
Indeed, all modules provided by Plume contain injectable objects.
To use these injectables objects, the default choice is Guice: it is really easy to use.

If you are already familiar with Guice, you may want to have a look at
[Dagger](http://google.github.io/dagger/users-guide.html) 
which enables to detect dependency injection problems at compile time.
To use Dagger the annotation processor should be enabled in your IDE: <https://immutables.github.io/apt.html>.
However, note that Dagger may be more difficult to use than Guice.

In Plume documentation, examples use Guice modules.
Most of the time there is a corresponding Dagger module to each Guice module.
For example, the corresponding Dagger module for `GuiceConfModule` is `DaggerConfModule`.

Plume core modules
------------------
### General modules

#### [Plume Base Dependencies](plume-framework-dependencies/)

Reference all libraries versions used by Plume.
It will help you avoid dependency conflicts in your `pom.xml` file.

#### [Plume Conf](plume-conf/)

This module is based on the [Config](https://github.com/typesafehub/config) library
and handles the application configuration.

#### [Plume Jersey](plume-web-jersey/)

Enables to build REST web-services with [Jersey](https://jersey.java.net/)
and expose the documentation with [Swagger](http://swagger.io/).

This module can be used with [Plume Jersey monitoring](plume-web-jersey-monitoring/)
to quickly setup application monitoring exposure.

#### [Plume Services](plume-services/)

Common services that are often needed in projects or libraries.

#### [Plume Mail](plume-mail/)

Expose a `Mailer` object from [Simple Java Mail](http://www.simplejavamail.org/)
through a [Config](https://github.com/typesafehub/config) configuration.

#### [Plume Scheduler](plume-scheduler/)

Enables to easily execute recurring tasks/jobs through
[Wisp Scheduler](https://github.com/Coreoz/Wisp).

#### [Plume test](plume-test/)

Provide common Plume dependencies for tests and integration tests mocks.

### Database modules

#### [Plume Database](plume-db/)

Basic utilities to pool SQL connections with [HikariCP](https://github.com/brettwooldridge/HikariCP)
and manage transactions.

#### [Plume Querydsl](plume-db-querydsl/)

Integration with [Querydsl](https://github.com/querydsl/querydsl/tree/master/querydsl-sql)
for SQL only (no JPA :).

#### [Plume Querydsl Codegen](plume-db-querydsl-codegen/)

Code generation for [Querydsl](https://github.com/querydsl/querydsl/tree/master/querydsl-sql)
for SQL only.

#### [Plume Database test](plume-db-test/)

Use [Flyway](https://flywaydb.org/) to help you make integration tests with a database.

Plume ecosystem
---------------
- [Maven archetype for Plume](https://github.com/Coreoz/Plume-archetypes) helps to create a project using Jersey/Grizzly, QueryDsl, HikariCP, Flyway. The generated project includes sample unit and integration tests. It also includes some basic configuration for Gitlab and Github CI.
- [Plume Admin](https://github.com/Coreoz/Plume-admin) and [Plume File](https://github.com/Coreoz/Plume-file) libraries allow you to quickly create an admin area and upload files, respectively.
- [Plume Dependencies](https://github.com/Coreoz/Plume-dependencies) is a [Maven BOM](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#bill-of-materials-bom-pomsl) project to unify dependencies between Plume, Plume Admin and Plume File libraries.

Other Libraries to use with Plume
---------------------------------
If you need an HTTP client in a Plume application,
a good choice is to use:
- [OkHttp](http://square.github.io/okhttp/),
- or [Retrofit](https://square.github.io/retrofit/) if you need to query a standard REST API.

With these libraries, a JSON parser is often needed. To enforce security, it is a good practice to set parsing limits: [see how Jackson can be configured to set these parsing limits](plume-web-jersey/README.md#jackson). 

Upgrades
--------
### Upgrade from 3.x to 4.x
See upgrade instructions in the [release details](https://github.com/Coreoz/Plume/releases/tag/4.0.0).

### Upgrade from 2.x to 3.x
See upgrade instructions in the [release details](https://github.com/Coreoz/Plume/releases/tag/3.0.0).

### Upgrade from 1.x to 2.x
See upgrade instructions in the [release details](https://github.com/Coreoz/Plume/releases/tag/2.0.0). 
