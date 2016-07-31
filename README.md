Plume Framework
===============

[![Build Status](https://travis-ci.org/Coreoz/Plume.svg?branch=master)](https://travis-ci.org/Coreoz/Plume)

Plume Framework is a lightweight modular framework. It aims to remain as simple as possible while it can be customized 
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

There are many options you can choose when using Plume Framework.
If you do not have time to review these options, just choose the Guice/Jersey/Hibernate one,
it is the more common one.

### Standard application with a REST API documented with [Swagger](http://swagger.io/)

This package is for you if you want:
 
- to be able to package a WAR web-app,
- your dev team to already know about all the technology inside your tech stack,
- to easily expose your API documentation.

If you don't already know Swagger, check out their API documentation example: <http://petstore.swagger.io/>

TODO link to the archetype

Dependency injection
--------------------
TODO explain how Guice should be used

For Dagger, you are on your own, just know there is a DaggerModule for each GuiceModule.
Moreover, the Dagger documentation is available at <http://google.github.io/dagger/users-guide.html>
Also to use Dagger the annotation processor should be enabled in your IDE: <https://immutables.github.io/apt.html>

Plume modules
-------------

### [Plume Conf](plume-conf/)

This module is based on the [Config](https://github.com/typesafehub/config) library
and handles the application configuration.

### [Plume Jersey](plume-jersey/)

Contains everything you need to build REST web-services with [Jersey](https://jersey.java.net/)
and expose your documentation with [Swagger](http://swagger.io/).

### [Plume Hibernate](plume-db-hibernate/)

The best known Java ORM [Hibernate](http://hibernate.org/)
with [QueryDSL](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa)
and [Sql2o](http://www.sql2o.org/) to ease its usage.

### [Plume Hibernate Oracle](plume-db-hibernate-oracle/)

This module comes on top of the Plume Hibernate module to provide Oracle specific data converters.

### [Plume Database test](plume-db-test/)

Use [Flyway](https://flywaydb.org/) to help you make integration tests with a database.
