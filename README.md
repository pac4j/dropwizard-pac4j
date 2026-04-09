<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-dropwizard.png" width="300" />
</p>

[![Maven Central](https://img.shields.io/maven-central/v/org.pac4j/dropwizard-pac4j.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.pac4j%22%20AND%20a%3A%22dropwizard-pac4j%22)

# dropwizard-pac4j

A [Dropwizard](http://www.dropwizard.io/) bundle for securing REST endpoints
using [pac4j](http://www.pac4j.org/).

| dropwizard-pac4j | JDK | pac4j | jax-rs-pac4j | Dropwizard |
|------------------|-----|-------|--------------|------------|
| version >= 8     | 17  | v6    | v7           | v5         |
| version >= 7     | 17  | v5    | v6           | v5         |
| version >= 6     | 11  | v5    | v6           | v4         |
| version >= 5.3   | 11  | v5    | v5           | v3         |
| version >= 5     | 11  | v4    | v4           | v2         |
| version >= 4     | 8   | v4    | v4           | v1         |
| version >= 3     | 8   | v3    | v3           | v1         |

## Usage

`dropwizard-pac4j` provides two components which must be integrated into
applications:

- A configuration factory populated by values from a `pac4j` section within an
  application's config file.
- A Dropwizard [bundle](http://www.dropwizard.io/1.0.5/docs/manual/core.html#bundles)
  which:
    - connects the values defined in the `pac4j` configuration section to the
      [`jax-rs-pac4j`](https://github.com/pac4j/jax-rs-pac4j/) and
      [`jee-pac4j`](https://github.com/pac4j/jee-pac4j/) libraries.
    - enables the use of the annotation provided by the
      [`jax-rs-pac4j`](https://github.com/pac4j/jax-rs-pac4j/) library.
    - enables Jetty session management by default.

### Dependencies (`dropwizard-pac4j` + `pac4j-*` libraries)

You need to add a dependency on:

- the `dropwizard-pac4j` library (<em>groupId</em>: **org.pac4j**, *version*:
**8.0.0**)
- the appropriate `pac4j` [submodules](http://www.pac4j.org/docs/clients.html)
(<em>groupId</em>: **org.pac4j**, *version*: **6.4.1**): `pac4j-oauth` for
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
    public void initialize(Bootstrap<MySecureConfiguration> bootstrap) {
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
case, pac4j won't be configured but pac4j's type will be readable in the
configuration. If the latter is not desired, do not use this bundle!

Add a `pac4j` section to a Dropwizard application's configuration file:

```yaml
pac4j:
  configFactory: com.example.security.MyConfigFactory
  # those protect the whole application at Jersey level
  globalFilters:
    - matchers: securityMatcher
      authorizers: isAuthenticated
  servlet:
    security:
      - ...
    callback:
      - ...
    logout:
      - ...
```

- `configFactory`: fully-qualified class name of a custom implementation of
  `org.pac4j.core.config.ConfigFactory`. This factory is responsible for
  building the pac4j `Config` (clients, authorizers, matchers, callback URL,
  logic components, etc.).
For example:
```java
public class MyConfigFactory implements ConfigFactory {
    @Override
    public Config build(Object... parameters) {
        return new Config(/* your clients/authorizers/matchers */);
    }
}
```
- `globalFilters` to declare global filters: the `clients`, `authorizers`,
`matchers`, and `skipResponse` properties directly map to
[the parameters](https://github.com/pac4j/jax-rs-pac4j/wiki/Apply-security)
used by `org.pac4j.jax.rs.filters.SecurityFilter`.

- `servlet` to declare servlet-level filters:
 - `security`: the `clients`, `authorizers`, and `matchers`
   properties directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Apply-security)
   used by `org.pac4j.jee.filter.SecurityFilter`.
   The `mapping` property is used to optionally specify urls to which this
   filter will be applied to, defaulting to all urls (`/*`).
 - `callback`: the `defaultUrl`, and `renewSession` properties
   directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Callback-configuration)
   used by `org.pac4j.jee.filter.CallbackFilter`.
   The `mapping` property is used to specify urls to which this filter will be
   applied to. It does not usually contains a wildcard.
 - `logout`: the `defaultUrl` and `logoutUrlPattern` properties directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Logout-configuration)
   used by `org.pac4j.jee.filter.LogoutFilter`.
   The `mapping` property is used to specify urls to which this filter will be
   applied to. It does not usually contains a wildcard.

- `sessionEnabled`: set to `false` to disable Jetty session management
  (enabled by default).
Define pac4j component-level configuration (`clients`, `authorizers`,
`matchers`, callback URL, and related settings) inside your
`ConfigFactory` implementation.
In most setups, sensible defaults are applied automatically for both Jersey
resources and servlet filters.

#### URLs Relativity

Note that all urls used within Jersey filters are relative to the dropwizard
`applicationContext` suffixed by the dropwizard `rootPath` while the urls used
within Servlet filters are only relative to the dropwizard
`applicationContext`.
For Jersey, this also includes `callbackUrl`s defined in your `ConfigFactory`
configuration.

#### Advanced Configuration

For more complex setup of pac4j configuration, the Config can be retrieved from
the Pac4jBundle object stored in your `Application`:

```java
public class MySecureApplication extends Application<MySecureConfiguration> {

    final Pac4jBundle<MySecureConfiguration> bundle = ...;

    @Override
    public void run(MySecureConfiguration config, Environment env) throws Exception {
        Config conf = bundle.getConfig();
        
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

### Usage with Dropwizard's ResourceTestRule

When using `ResourceTestRule`, it usually make sense to mock the profile that
is injected for `@Pac4jProfile` annotations by using one of the alternative
`Pac4JValueFactoryProvider` binders:

```java
@Rule
public final ResourceTestRule resources = ResourceTestRule.builder()
      .addProvide(MyResource.class)
      .addProvider(new Pac4JValueFactoryProvider.Binder(new CockpitProfile("my-mock-user-id")))
      .build();
```
## Demos

Start with the [dropwizard-pac4j-demo](https://github.com/pac4j/dropwizard-pac4j-demo).

The demo illustrates several ways to integrate pac4j with Dropwizard (JAX-RS views, REST resources, and servlet/JAX-RS combinations) with authentication mechanisms like form login, basic auth, CAS, LDAP and SQL.

## Release notes

See the [release notes](https://github.com/pac4j/dropwizard-pac4j/wiki/Release-Notes).


## Need help?

You can use the [mailing lists](http://www.pac4j.org/mailing-lists.html) or the [commercial support](http://www.pac4j.org/commercial-support.html).


## Development

The version 8.0.0-SNAPSHOT is under development.

Maven artifacts are built via Github Actions and available in the Central Portal Snapshots repository. This repository must be added in the Maven `pom.xml` file for example:

```xml
<repositories>
  <repository>
    <name>Central Portal Snapshots</name>
    <id>central-portal-snapshots</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases>
      <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```
