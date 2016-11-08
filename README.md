Plume Framework
===============

[![Build Status](https://travis-ci.org/Coreoz/Plume.svg?branch=master)](https://travis-ci.org/Coreoz/Plume)

Plume Framework is a lightweight modular Java framework. It aims to remain as simple as possible while it can be customized 
enough to fit exactly your application need.
Plume is mainly aggregating (great) JVM libraries. This way, the Plume code base is limited to the minimum
so that the aggregated libraries can function together.  

Plume Framework require at least Java 8. Its modules contains connectors for
[Guice](https://github.com/google/guice) and [Dagger](https://github.com/google/dagger).

Plume Framework is maintained by [Coreoz](http://coreoz.com/)
and licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

Demo
----

TODO

Getting started
---------------

The best way to get started it to use a
[Maven archetype for Plume Framework](https://github.com/Coreoz/Plume-archetypes).

Dependency injection
--------------------
TODO explain how Guice should be used

For Dagger, you are on your own, just know there is a DaggerModule for each GuiceModule.
Moreover, the Dagger documentation is available at <http://google.github.io/dagger/users-guide.html>.
Also to use Dagger the annotation processor should be enabled in your IDE: <https://immutables.github.io/apt.html>.

Plume modules
-------------

### General modules

#### [Plume Dependencies](plume-dependencies/)

Reference all libraries versions used directly or indirectly by Plume Framework.
It will help you avoid dependency conflict in your `pom.xml` file.

#### [Plume Conf](plume-conf/)

This module is based on the [Config](https://github.com/typesafehub/config) library
and handles the application configuration.

#### [Plume Jersey](plume-web-jersey/)

Enable to build REST web-services with [Jersey](https://jersey.java.net/)
and expose the documentation with [Swagger](http://swagger.io/).

#### [Plume Services](plume-services/)

Common services that are often needed in projects or libraries.

#### [Plume Mail](plume-mail/)

Expose a `Mailer` object from [Simple Java Mail](http://www.simplejavamail.org/)
through a [Config](https://github.com/typesafehub/config) configuration.

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

#### [Plume Hibernate](plume-db-hibernate/)

The best known Java ORM [Hibernate](http://hibernate.org/)
with [Querydsl](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa)
and [Sql2o](http://www.sql2o.org/) to ease its usage.

#### [Plume Hibernate Oracle](plume-db-hibernate-oracle/)

This module comes on top of the Plume Hibernate module to provide Oracle specific data converters.

