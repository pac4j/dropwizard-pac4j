package org.pac4j.dropwizard;

import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigSingleton;
import org.pac4j.dropwizard.Pac4jFactory.FilterConfiguration;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
import org.pac4j.jax.rs.features.Pac4JSecurityFilterFeature;
import org.pac4j.jax.rs.features.jersey.Pac4JValueFactoryProvider;
import org.pac4j.jax.rs.filter.SecurityFilter;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * A {@link ConfiguredBundle} which installs a {@link SecurityFilter} into a
 * Dropwizard application's Jersey filter chain.
 */
public abstract class Pac4jBundle<T extends Configuration>
        implements ConfiguredBundle<T>, Pac4jConfiguration<T> {
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {
        // nothing to do
    }

    @Override
    public final void run(T configuration, Environment environment)
            throws Exception {
        final Pac4jFactory pac4jFactory = getPac4jFactory(configuration);

        final Config config = ConfigSingleton.getConfig() == null ? new Config()
                : ConfigSingleton.getConfig();
        ConfigSingleton.setConfig(config);

        if (pac4jFactory != null && pac4jFactory.getFilters() != null) {
            for (FilterConfiguration fConf : pac4jFactory.getFilters()) {
                environment.jersey()
                        .register(new Pac4JSecurityFilterFeature(config,
                                fConf.getSkipResponse(), fConf.getAuthorizers(),
                                fConf.getClients(), fConf.getMatchers(),
                                fConf.getMultiProfile()));
            }
        }

        String urlPrefix = getUrlPrefix(configuration, environment);

        environment.jersey()
                .register(new Pac4JSecurityFeature(config, urlPrefix));
        environment.jersey()
                .register(new Pac4JValueFactoryProvider.Binder(config));
    }

    private String getUrlPrefix(T configuration, Environment environment) {
        // the context path on the
        String cPath = environment.getApplicationContext().getContextPath();
        String sPath = ((AbstractServerFactory) configuration
                .getServerFactory()).getJerseyRootPath().orElse("");

        return (clean(cPath) + clean(sPath)).replace("//", "/");
    }

    private String clean(String path) {
        if (path == null) {
            return "";
        }

        String res;
        if (path.endsWith("*")) {
            res = path.substring(0, path.length() - 2);
        } else {
            res = path;
        }

        if ("/".equals(res)) {
            return "";
        } else {
            return res;
        }
    }
}
