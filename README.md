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

### Dependencies (`dropwizard-pac4j` + `pac4j-*` libraries)

You need to add a dependency on:

- the `dropwizard-pac4j` library (<em>groupId</em>: **org.pac4j**, *version*:
**1.0.0-SNAPSHOT**)
- the appropriate `pac4j` [submodules](http://www.pac4j.org/docs/clients.html)
(<em>groupId</em>: **org.pac4j**, *version*: **1.9.4**): `pac4j-oauth` for
OAuth support (Facebook, Twitter...), `pac4j-cas` for CAS support, `pac4j-ldap`
for LDAP authentication, etc.

All released artifacts are available in the
[Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cpac4j).

### Installing the bundle

Add the bundle within the application class' `initialize` method, just like any
other bundle:

```java
public class MySecureApplication extends Application<MySecureConfiguration> {
    final Pac4jBundle<MySecureConfiguration> bundle = new Pac4jBundle<MySecureConfiguration>() {
        @Override
        public Pac4jFactory getPac4jFactory(MySecureConfiguration configuration) {
            return configuration.getPac4jFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(bundle);
    }

    ...
```

It can be useful to store the bundle in its own field in order to be able to
access pac4j configuration as shown at the end of the next section.

### Configuring the bundle

Update the application's configuration class to expose accessor methods for
`Pac4jFactory`:

```java
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
```

Note that it is also possible to have `pac4jFactory` be nullable and in this
case, the bundle will be disabled.

Add a `pac4j` section to a Dropwizard application's configuration file:

```yaml
pac4j:
  # those protect the whole application at Jersey level
  globalFilters:
    - matchers: excludeUserSession
      authorizers: isAuthenticated
  matchers:
    # this let the /user/session url be handled by the annotations
    excludeUserSession:
      class: org.pac4j.core.matching.ExcludedPathMatcher
      excludePath: ^/user/session$
  callbackUrl: /user/session
  clients:
    - org.pac4j.http.client.direct.DirectBasicAuthClient:
        authenticator:
          class: org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
```

- `globalFilters` to declare global filters: the `clients`, `authorizers`,
`matchers`, `multiProfile` and `skipResponse` properties directly map to
[the parameters](https://github.com/pac4j/jax-rs-pac4j/#3-protect-urls-securityfilter)
used by `org.pac4j.jax.rs.filter.SecurityFilter`.

- `matchers`: the key is the name of the `Matcher` and its instance is
declared as explained below.
Their name can be used in `filter`'s `matchers` as well as in the
`Pac4JSecurity` annotation.

- `authorizers`: the key is the name of the `Authorizer` and its instance is
declared as explained below.
Their name can be used in `filter`'s `authorizers` as well as in the
`Pac4JSecurity` annotation.

- `clients`: the key is the class of the `Client` and its instance
is configured based on the properties. Its name is by default the short
name of its class, but it can also be set explictly.
Their name can be used in `filter`'s `clients` as well as in the
`Pac4JSecurity` annotation.

To specify instances of `Client`, `Authenticator`, `PasswordEncoder`,
`CredentialsExtractor`, `ProfileCreator`, `AuthorizationGenerator`,
`Authorizer`, `Matcher`, `CallbackUrlResolver` and `RedirectActionBuilder`, it
only necessary to refer to their class name using the `class` key as above and
the other properties are set on the instantiated object.

Note also that the configuration will use `JaxRsCallbackUrlResolver` as the default
`CallbackUrlResolver` if not overridden.

#### Advanced Configuration

For more complex setup of pac4j configuration, the Config can be retrieved from
the Pac4jBundle object stored in your `Application`:

```java
public class MySecureApplication extends Application<MySecureConfiguration> {

    final Pac4jBundle<MySecureConfiguration> bundle = ...;

    @Override
    public void run(MySecureConfiguration config, Environment env) throws Exception {
        Config conf = bundle.getConfig()
        
        DirectBasicAuthClient c = conf.getClients().findClient(DirectBasicAuthClient.class);
        c.setCredentialsExtractor(...);
        
        env.jersey().register(new DogsResource());
    }
}
```

### Securing REST endpoints

From here, `jax-rs-pac4j` takes over with its annotations. See `pac4j`
documentation on how to implement `Client`s, `Authorizer`s, `Matcher`s and all
the other points of extension.

* [pac4j's website](http://www.pac4j.org) and
  [README](https://github.com/pac4j/pac4j)
* [`jax-rs-pac4j`'s README](https://github.com/pac4j/jax-rs-pac4j)


## Release notes

See the [release notes](https://github.com/pac4j/dropwizard-pac4j/wiki/Release-Notes).
Learn more by browsing the
[dropwizard-pac4j Javadoc](http://www.javadoc.io/doc/org.pac4j/dropwizard-pac4j/1.0.0)
and the [pac4j Javadoc](http://www.pac4j.org/apidocs/pac4j/1.9.4/index.html).


## Need help?

If you have any question, please use the following mailing lists:

- [pac4j users](https://groups.google.com/forum/?hl=en#!forum/pac4j-users)
- [pac4j developers](https://groups.google.com/forum/?hl=en#!forum/pac4j-dev)


## Development

The version 1.0.0-SNAPSHOT is under development.

Maven artifacts are built via Travis:
[![Build Status](https://travis-ci.org/pac4j/dropwizard-pac4j.png?branch=master)](https://travis-ci.org/pac4j/dropwizard-pac4j)
and available in the
[Sonatype snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/org/pac4j).
This repository must be added in the Maven `pom.xml` file for example:

```xml
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>Sonatype Nexus Snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
      <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```