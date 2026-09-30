<p align="center">
  <img src="https://pac4j.github.io/pac4j/img/logo-dropwizard.png" width="300" />
</p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/org.pac4j/dropwizard-pac4j"><img src="https://img.shields.io/maven-central/v/org.pac4j/dropwizard-pac4j?label=Maven%20Central" alt="Maven Central" /></a>
  <a href="https://github.com/pac4j/dropwizard-pac4j/actions/workflows/ci.yml"><img src="https://github.com/pac4j/dropwizard-pac4j/actions/workflows/ci.yml/badge.svg" alt="Build status" /></a>
  <img src="https://img.shields.io/badge/Java-17%2B-blue" alt="Java 17+" />
  <img src="https://img.shields.io/badge/Dropwizard-5.x-blue" alt="Dropwizard 5.x" />
  <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%202.0-blue" alt="Apache 2 license" /></a>
</p>

> `dropwizard-pac4j` is the Dropwizard implementation of **[pac4j](https://github.com/pac4j/pac4j)**, the security engine for Java.
> If it is useful to you, please ⭐ **[star pac4j on GitHub](https://github.com/pac4j/pac4j)**: it helps other developers discover it!

# dropwizard-pac4j

A [Dropwizard](https://www.dropwizard.io/) bundle for securing REST endpoints
using [pac4j](https://www.pac4j.org/).

| dropwizard-pac4j | JDK | pac4j | jax-rs-pac4j | Dropwizard |
|------------------|-----|-------|--------------|------------|
| version >= 8.1   | 17  | v6    | v8           | v5         |
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
- A Dropwizard [bundle](https://www.dropwizard.io/en/stable/manual/core.html#bundles)
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
[![Maven Central](https://img.shields.io/maven-central/v/org.pac4j/dropwizard-pac4j.svg)](https://repo.maven.apache.org/maven2/org/pac4j/dropwizard-pac4j/))
- the appropriate `pac4j` [submodules](https://www.pac4j.org/docs/clients.html)
(<em>groupId</em>: **org.pac4j**, *version*:
[![Maven Central](https://img.shields.io/maven-central/v/org.pac4j/pac4j-core.svg?versionPrefix=6)](https://repo.maven.apache.org/maven2/org/pac4j/pac4j-core/)): `pac4j-oauth` for
OAuth support (Facebook, Twitter...), `pac4j-cas` for CAS support, `pac4j-ldap`
for LDAP authentication, etc.

All released artifacts are available in the
[Maven central repository](https://central.sonatype.com/namespace/org.pac4j).

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
    @Valid
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

The `@Valid` annotation is required for the `pac4j` section to be validated.

Note that it is also possible to have `pac4jFactory` be nullable and in this
case, pac4j won't be configured.

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
- `globalFilters` to declare a global filter: the `clients`, `authorizers`,
`matchers`, and `skipResponse` properties directly map to
[the parameters](https://github.com/pac4j/jax-rs-pac4j/wiki/Apply-security)
used by `org.pac4j.jax.rs.filters.SecurityFilter`.
Only one global filter is supported: the application fails at startup if
several ones are defined.

- `servlet` to declare servlet-level filters:
 - `security`: the `clients`, `authorizers`, and `matchers`
   properties directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Apply-security)
   used by `org.pac4j.jee.filter.SecurityFilter`.
   The `mapping` property is used to optionally specify urls to which this
   filter will be applied, defaulting to all urls (`/*`).
 - `callback`: the `defaultUrl`, `renewSession` and `defaultClient` properties
   directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Callback-configuration)
   used by `org.pac4j.jee.filter.CallbackFilter`.
   The `mapping` property is used to specify urls to which this filter will be
   applied. It does not usually contain a wildcard.
 - `logout`: the `defaultUrl`, `logoutUrlPattern`, `localLogout`,
   `destroySession` and `centralLogout` properties directly map to
   [the parameters](https://github.com/pac4j/jee-pac4j/wiki/Logout-configuration)
   used by `org.pac4j.jee.filter.LogoutFilter`.
   The `mapping` property is used to specify urls to which this filter will be
   applied. It does not usually contain a wildcard.

- `sessionEnabled`: set to `false` to disable Jetty session management
  (enabled by default).

Define pac4j component-level configuration (`clients`, `authorizers`,
`matchers`, callback URL, and related settings) inside your
`ConfigFactory` implementation.
In most setups, sensible defaults are applied automatically for both Jersey
resources and servlet filters.

#### AJAX requests and indirect clients

By default, the `DropwizardAjaxRequestResolver` is used:

- JAX-RS requests are always considered as AJAX requests: when an indirect
  client (form, CAS, OpenID Connect, SAML...) must start the login process, a
  401 error is returned instead of a redirection to the identity provider. This
  suits REST APIs, for which a redirection would break the API clients (like a
  single page application calling the API with `fetch`).
- Servlet requests (from the servlet filters) are handled as usual: indirect
  clients redirect to the identity provider, unless the request is an AJAX one
  (`X-Requested-With: XMLHttpRequest` header).

This has no impact on direct clients (basic auth, headers, JWT...), which never
redirect and always return a 401 error when the credentials are missing or
invalid.

If your JAX-RS resources serve web pages (like Dropwizard views) protected by
indirect clients, use the default pac4j behavior in your `ConfigFactory` so
that the indirect clients redirect to the identity provider:

```java
public class MyConfigFactory implements ConfigFactory {
    @Override
    public Config build(Object... parameters) {
        final Config config = new Config(/* your clients */);
        config.getClients().setAjaxRequestResolver(new DefaultAjaxRequestResolver());
        return config;
    }
}
```

See the [dropwizard-pac4j-demo](https://github.com/pac4j/dropwizard-pac4j-demo)
for a complete example.

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
        
        DirectBasicAuthClient c = (DirectBasicAuthClient) conf.getClients()
            .findClient("DirectBasicAuthClient").orElseThrow();
        c.setCredentialsExtractor(...);
        
        env.jersey().register(new DogsResource());
    }
}
```

### Securing REST endpoints

From here, `jax-rs-pac4j` takes over with its annotations. See `pac4j`
documentation on how to implement `Client`s, `Authorizer`s, `Matcher`s and all
the other points of extension.

* [pac4j's website](https://www.pac4j.org) and
  [README](https://github.com/pac4j/pac4j)
* [`jax-rs-pac4j`'s README](https://github.com/pac4j/jax-rs-pac4j)

### Usage with Dropwizard's ResourceExtension

When using `ResourceExtension` (JUnit 5), it usually makes sense to mock the
profile that is injected for `@Pac4JProfile` annotations by using one of the
alternative `Pac4JValueFactoryProvider` binders:

```java
@ExtendWith(DropwizardExtensionsSupport.class)
class MyResourceTest {

    private static final CommonProfile PROFILE = new CommonProfile();
    static {
        PROFILE.setId("my-mock-user-id");
    }

    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
        .addResource(new MyResource())
        .addProvider(new Pac4JValueFactoryProvider.Binder(PROFILE))
        .build();

    ...
}
```

## Demos

Start with the [dropwizard-pac4j-demo](https://github.com/pac4j/dropwizard-pac4j-demo).

The demo protects Dropwizard views served by JAX-RS resources with form login, basic auth (indirect and direct) and CAS.

## Release notes

The latest release is: [![Maven Central](https://img.shields.io/maven-central/v/org.pac4j/dropwizard-pac4j.svg)](https://repo.maven.apache.org/maven2/org/pac4j/dropwizard-pac4j/).

See the [release notes](https://github.com/pac4j/dropwizard-pac4j/wiki/Release-notes), which also describe the changes to make when upgrading.

## Need help?

You can use the [mailing lists](https://www.pac4j.org/mailing-lists.html) or the [commercial support](https://www.pac4j.org/commercial-support.html).


## Development

The version 8.1.0-SNAPSHOT is under development.

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
