Plume Framework
===============
[![Build Status](https://travis-ci.org/Coreoz/Plume.svg?branch=master)](https://travis-ci.org/Coreoz/Plume)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.coreoz/plume-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.coreoz/plume-parent)

Plume Framework is a lightweight modular Java framework. It aims to remain as simple as possible while it can be customized 
enough to fit exactly your application need.
Plume is mainly aggregating (great) JVM libraries. This way, the Plume code base is limited to the minimum
so that the aggregated libraries can function together.

Plume Framework require at least Java 8. Its modules contains connectors for
[Guice](https://github.com/google/guice) and [Dagger](https://github.com/google/dagger).

Plume Framework is maintained by [Coreoz](http://coreoz.com/)
and licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Upgrade from 1.x to 2.x
-----------------------
See upgrade instructions in the [release details](releases/tag/2.0.0). 

Philosophy
----------
The goal of Plume Framework is to maximize the reusability of its modules.
For example Plume Framework components can be easily used
with [Play Framework](https://github.com/playframework/playframework)
or with [Spark Java](https://github.com/perwendel/spark).
In these cases the [Plume Jersey module](plume-web-jersey/) will be replaced
with the corresponding Play and Spark engines.

To make modules reusable in other contexts,
we tried to limit to the minimum the dependencies between each module.

Though Plume Framework only offers dependency injection connectors to Guice and Dragger,
it is possible to adapt these connectors to work with Spring or CDI.

Demo
----
Sample projects can be found here: <https://github.com/Coreoz/Plume-demo>.

Getting started
---------------
The best way to get started is to use a
[Maven archetype for Plume Framework](https://github.com/Coreoz/Plume-archetypes).

Dependency injection
--------------------
If you are not familiar with the dependency injection concept, read the
[Guice Motivation Guice](https://github.com/google/guice/wiki/Motivation).

Dependency injection takes a central place in Plume Framework.
Indeed, all modules provided by Plume Framework contain injectable objects.
To use these injectables objects, the default choice is Guice: it is really easy to use.

If you are already familiar with Guice, you may want to have a look at
[Dagger](http://google.github.io/dagger/users-guide.html) 
which enables to detect dependency injection problems at compile time.
To use Dagger the annotation processor should be enabled in your IDE: <https://immutables.github.io/apt.html>.
However note that Dagger may be more difficult to use than Guice.

In Plume Framework documentation, examples use Guice modules.
Most of the time there is a corresponding Dagger module to each Guice module.
For example, the corresponding Dagger module for `GuiceConfModule` is `DaggerConfModule`.

Plume core modules
------------------

### General modules

#### [Plume Framework Dependencies](plume-framework-dependencies/)

Reference all libraries versions used by Plume Framework.
It will help you avoid dependency conflicts in your `pom.xml` file.

#### [Plume Conf](plume-conf/)

This module is based on the [Config](https://github.com/typesafehub/config) library
and handles the application configuration.

#### [Plume Jersey](plume-web-jersey/)

Enables to build REST web-services with [Jersey](https://jersey.java.net/)
and expose the documentation with [Swagger](http://swagger.io/).

#### [Plume Services](plume-services/)

Common services that are often needed in projects or libraries.

#### [Plume Mail](plume-mail/)

Expose a `Mailer` object from [Simple Java Mail](http://www.simplejavamail.org/)
through a [Config](https://github.com/typesafehub/config) configuration.

#### [Plume Scheduler](plume-scheduler/)

Enables to easily execute recurring tasks/jobs through
[Wisp Scheduler](https://github.com/Coreoz/Wisp).

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

Other Libraries to use with Plume Framework
-------------------------------------------
If you need an HTTP client in a Plume application,
a good choice is to use:
- [OkHttp](http://square.github.io/okhttp/),
- or [Retrofit](https://square.github.io/retrofit/) if you need to query a standard REST API.
