Plume Web Jersey
================

This module provides multiple utilities to use Jersey, Grizzly, Servlets.

Setup
-----
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-web-jersey</artifactId>
</dependency>
```

Jackson
-------
The module `GuiceJacksonModule` provides an injectable Jackson `ObjectMapper` with common defaults, especially:
- a support for Java 8 Time objects,
- unknown attributes handling non-mandatory. 

Asynchronous web-services
-------------------------
TODO

