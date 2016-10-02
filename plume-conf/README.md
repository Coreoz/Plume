Plume Conf
==========

This module is based on the [Config](https://github.com/typesafehub/config) library.

To summarize, the Config library do everything you'll ever want from a Java configuration library.
The main features are:

- parse simple data as well as more advance types (list, duration, size...),
- allow to include a configuration file into one another and easily override properties from the included file,
- can load configuration file from classpath, file system or URL.

Getting started
---------------

Include Plume Config in your project:
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-conf</artifactId>
</dependency
```

Install Guice or Dagger module in your application:

- Guice: `install(new GuiceConfModule())`
- Dagger: `@Component(modules = DaggerConfModule.class)`

Then by default, the `application.conf` file in your `src/main/resources` folder will be loaded.
Properties can be retrieved by using the instance of `com.typesafe.config.Config`.
For example: 
```java
@Singleton
public class MailService {

    private final String smtpHost;
    
    @Inject
    public MailService(Config config) {
        this.smtpHost = config.getString("smtp.host");
    }

}
```

Environment handling
--------------------

Most of the time you will create a configuration file on the target environment file system you want to put
your application on. This file will include your default configuration file and override some properties. 
Then in your JVM parameter you will specify that you want to use the configuration file on the file system instead of
the default `application.conf` file.
For example, here is what may contains the file `/apps/my-app/conf/my-app.conf`:
```
include classpath("application.conf")

my-property = Overriden value
```

Your startup script should then reference the file system configuration file using the `-Dconfig.file` system property.
For example on Tomcat: `export JAVA_OPTS=-Dconfig.file=/apps/my-app/conf/my-app.conf`

Note that you can also load a configuration file from the classpath or from a URL using `-Dconfig.resource`
or `-Dconfig.url`.

Links
-----

- [Complete presentation of Config](https://github.com/typesafehub/config)
- [How the configurations are loaded and how to externalize the configuration](https://github.com/typesafehub/config#standard-behavior)
- [Exhaustive syntax definition](https://github.com/typesafehub/config/blob/master/HOCON.md)
