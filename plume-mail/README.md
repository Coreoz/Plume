Plume Mail
==========

Enable to initialize [Simple Java Mail](http://www.simplejavamail.org/)
with a [Config](https://github.com/typesafehub/config) configuration.

Configuration parameters are listed at <http://www.simplejavamail.org/#section-available-properties>.
All properties prefixed by `mail` are converted to `simplejavamail` properties.
For example:
```INI
mail."smtp.host" = "127.0.0.1"
mail."smtp.port" = 25
mail."defaults.from.address" = "contact@your-company.com"
```
Will be passed to Simple Java Mail as:
```INI
simplejavamail.smtp.host=127.0.0.1
simplejavamail.smtp.port=25
simplejavamail.defaults.from.address=contact@your-company.com
```

You should noticed that in the Config configuration,
Simple Java Mail **properties are wrapped in quotes**. This is important, else mail properties
will be ignored.

Getting started
---------------
Include Plume Mail in your project:
```xml
<dependency>
    <groupId>com.coreoz</groupId>
    <artifactId>plume-mail</artifactId>
</dependency>
```

Install Guice module: `install(new GuiceMailModule());`

Configure the Mailer default properties in your `application.conf` file:
```INI
mail."smtp.host" = "127.0.0.1"
mail."smtp.port" = 25
mail."defaults.from.address" = "contact@your-company.com"
```

Create a mail service class that sends mails:
```java
@Singleton
public class EmailService {

  private final Mailer mailer;

  @Inject
  public EmailService(Mailer mailer) {
    this.mailer = mailer;
  }

  public void sendEmail() {
    Email email = EmailBuilder
      .startingBlank()
      .to("Russell Powell", "rpowell0@whitehouse.gov")
      .withSubject("Plume")
      .withPlainText("You should check this Java library out!")
      .buildEmail();
    mailer.sendMail(email, true);
  }
}
```

Troubleshooting
---------------
Simple Java Mail enforces SMTP server connection using TLS and identity verification. While this is a better security option, some SMTP servers do not offer TLS, or they are using a self-signed certificate. In these situations, these security configuration options can be disabled (if possible, only on non-production environments):
```hocon
# For self-signed SMTP server
mail."defaults.verifyserveridentity"=false
# For SMTP server not providing TLS connection
mail."opportunistic.tls"=false
```
More details are available in the [security page of Simple Java Mail](https://www.simplejavamail.org/security.html).
