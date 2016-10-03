<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-dropwizard.png" width="300" />
</p>

# dropwizard-pac4j

A [Dropwizard](http://www.dropwizard.io/) bundle for securing REST
endpoints using [pac4j](http://www.pac4j.org/).

## Usage

`dropwizard-pac4j` provides two components which must be integrated
into applications:

- A configuration factory populated by values from a `pac4j` section
  within an application's config file.
- A Dropwizard
  [bundle](http://www.dropwizard.io/1.0.0/docs/manual/core.html#bundles)
  which connects the values defined in the `pac4j` configuration
  section to the [`j2e-pac4j`](https://github.com/pac4j/j2e-pac4j/)
  library.

### Installing the bundle

Add the bundle within the application class' `initialize` method, just
like any other bundle:

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

Update the application's configuration class to expose accessor
methods for `Pac4jFactory`:

    public class TestConfiguration extends Configuration {
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

Add a `pac4j` section to a Dropwizard application's configuration
file:

    pac4j:
      configFactory: "co.evanm.example.MyPac4jConfigFactory"
      clients: "DirectBaseicAuthClient"
      authorizers: ""
      matchers: ""
      multiProfile: false

These properties map directly to
[the five parameters](https://github.com/pac4j/j2e-pac4j/#3-protect-urls-securityfilter)
used by `org.pac4j.j2e.filter.SecurityFilter`.

### Securing REST endpoints

From here, `j2e-pac4j` takes over. See `pac4j` documentation on how to
implement `ConfigFactory`s, `Client`s, `Authorizer`s, and `Matcher`s.

* [pac4j's website](http://www.pac4j.org) and [README](https://github.com/pac4j/pac4j)
* [`j2e-pac4j`'s README](https://github.com/pac4j/j2e-pac4j)

## Maven Artifacts

Build status: [![Build Status](https://travis-ci.org/pac4j/dropwizard-pac4j.png?branch=master)](https://travis-ci.org/pac4j/dropwizard-pac4j)

Deployed on the [Sonatype snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/org/pac4j). 

Public Maven artifacts are forthcoming, pending the release of an
initial stable version of `dropwizard-pac4j`.

## Support

Please file bug reports and feature requests in
[GitHub issues](https://github.com/pac4j/dropwizard-pac4j/issues).

## License

Copyright (c) 2016 Evan Meagher

This library is licensed under the Apache License, Version 2.0.

See http://www.apache.org/licenses/LICENSE-2.0.html or the LICENSE
file in this repository for the full license text.
