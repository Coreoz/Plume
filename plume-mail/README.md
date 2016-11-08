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

You should noticed that in the [Config](https://github.com/typesafehub/config) configuration,
Simple Java Mail **properties are wrapped in quotes**. This is important, else mail properties
will be ignored.