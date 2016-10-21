<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-dropwizard.png" width="300" />
</p>

# dropwizard-pac4j

A [Dropwizard](http://www.dropwizard.io/) bundle for securing REST endpoints
using [pac4j](http://www.pac4j.org/).

## Usage

`dropwizard-pac4j` provides two components which must be integrated into
applications:

- A configuration factory populated by values from a `pac4j` section within an
  application's config file.
- A Dropwizard [bundle](http://www.dropwizard.io/1.0.2/docs/manual/core.html#bundles)
  which:
    - connects the values defined in the `pac4j` configuration section to the
      [`jax-rs-pac4j`](https://github.com/pac4j/jax-rs-pac4j/) library.
    - enables the use of the annotation provided in by the
      [`jax-rs-pac4j`](https://github.com/pac4j/jax-rs-pac4j/) library.

### Installing the bundle

Add the bundle within the application class' `initialize` method, just like any
other bundle:

    public class MySecureApplication extends Application<MySecureConfiguration> {
        @Override
        public void initialize(Bootstrap<TestConfiguration> bootstrap) {
            final Pac4jBundle<MySecureConfiguration> bundle = new Pac4jBundle<MySecureConfiguration>() {
                @Override
                public Pac4jFactory getPac4jFactory(MySecureConfiguration configuration) {
                    return configuration.getPac4jFactory();
                }
            };
            bootstrap.addBundle(bundle);
        }

        ...

### Configuring the bundle

Update the application's configuration class to expose accessor methods for
`Pac4jFactory`:

    public class MySecureConfiguration extends Configuration {
        @NotNull
        Pac4jFactory pac4jFactory = new Pac4jFactory();

        @JsonProperty("pac4j")
        public Pac4jFactory getPac4jFactory() {
            return pac4jFactory;
        }

        @JsonProperty("pac4j")
        public void setPac4jFactory(Pac4jFactory pac4jFactory) {
            this.pac4jFactory = pac4jFactory;
        }
    }

Note that it is also possible to have `pac4jFactory` be null and in this
case, the bundle will be disabled.

Add a `pac4j` section to a Dropwizard application's configuration file:

```yaml
    pac4j:
      filters:
        # this protects the whole application
        - matchers: excludeUserSession
          authorizers: isAuthenticated
      matchers:
        # this let the /user/session url be handled by the annotations
        excludeUserSession:
          class: org.pac4j.core.matching.ExcludedPathMatcher
          excludePath: ^/user/session$
      callbackUrl: /user/session
      clients:
        org.pac4j.http.client.direct.DirectBasicAuthClient:
          authenticator:
            class: org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
```

For `filters`, the properties directly map to
[the five parameters](https://github.com/pac4j/jax-rs-pac4j/#3-protect-urls-securityfilter)
used by `org.pac4j.jax.rs.filter.SecurityFilter.SecurityFilter`.

For `matchers`, the key is the name of the `Matcher` and its instance is
declared as explained below.
Their name can be used in `filter`'s `matchers` as well as in the
`Pac4JSecurity` annotation.

For `authorizers`, the key is the name of the `Authorizer` and its instance is
declared as explained below.
Their name can be used in `filter`'s `authorizers` as well as in the
`Pac4JSecurity` annotation.

For `clients`'s, the key is the class of the `Client` and its instance
is configured based on the properties. Its name is by default the short
name of its class, but it can also be set explictly.
Their name can be used in `filter`'s `clients` as well as in the
`Pac4JSecurity` annotation.

To specify instances of `Client`, `Authenticator`, `PasswordEncoder`,
`CredentialsExtractor`, `ProfileCreator`, `AuthorizationGenerator`, `Authorizer`,
`Matcher`, `CallbackUrlResolver` and `RedirectActionBuilder`, it only necessary to refer
to their class name using the `class` key as above and the other properties are set on 
the instantiated object.

Note also that the configuration will use `JaxRsCallbackUrlResolver` as the default
`CallbackUrlResolver`.

For more complex setup of pac4j configuration, this can be done in your
application using `ConfigSingleton` from pac4j:

    public class MySecureApplication extends Application<MySecureConfiguration> {

        ....

        @Override
        public void run(MySecureConfiguration config, Environment env) throws Exception {
            Config conf = ConfigSingleton.getConfig()
            
            DirectBasicAuthClient c = conf.getClients().findClient(DirectBasicAuthClient.class);
            c.setCredentialsExtractor(...);
            
            env.jersey().register(new DogsResource());
        }
    }

### Securing REST endpoints

From here, `jax-rs-pac4j` takes over with its annotations. See `pac4j`
documentation on how to implement `Client`s, `Authorizer`s, `Matcher`s and all
the other points of extension.

* [pac4j's website](http://www.pac4j.org) and
  [README](https://github.com/pac4j/pac4j)
* [`jax-rs-pac4j`'s README](https://github.com/pac4j/jax-rs-pac4j)

## Maven Artifacts

Build status: [![Build Status](https://travis-ci.org/pac4j/dropwizard-pac4j.png?branch=master)](https://travis-ci.org/pac4j/dropwizard-pac4j)

Deployed on the [Sonatype snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/org/pac4j).

Public Maven artifacts are forthcoming, pending the release of an initial
stable version of `dropwizard-pac4j`.

## Support

Please file bug reports and feature requests in [GitHub issues](https://github.com/pac4j/dropwizard-pac4j/issues).

## License

Copyright (c) 2016 Evan Meagher

This library is licensed under the Apache License, Version 2.0.

See http://www.apache.org/licenses/LICENSE-2.0.html or the LICENSE
file in this repository for the full license text.
