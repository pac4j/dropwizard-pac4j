package org.pac4j.dropwizard;

import java.util.ArrayList;
import java.util.Collection;

import org.pac4j.core.config.Config;
import org.pac4j.dropwizard.Pac4jFactory.FilterConfiguration;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
import org.pac4j.jax.rs.features.Pac4JSecurityFilterFeature;
import org.pac4j.jax.rs.filters.SecurityFilter;
import org.pac4j.jax.rs.jersey.features.Pac4JValueFactoryProvider;
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * A {@link ConfiguredBundle} which sets up {@link Pac4JSecurityFeature},
 * {@link Pac4JValueFactoryProvider} as well as installs {@link SecurityFilter}s
 * into a Dropwizard application's Jersey filter chain.
 * 
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.0.0
 */
public abstract class Pac4jBundle<T extends Configuration>
        implements ConfiguredBundle<T>, Pac4jConfiguration<T> {

    private Config config;

    @Override
    public final void initialize(Bootstrap<?> bootstrap) {
        for (Pac4jFeatureSupport fs : supportedFeatures()) {
            fs.setup(bootstrap);
        }
    }

    /**
     * In case of override, do not forget to call
     * {@code super.supportedFeatures()} to get the default features, or to
     * redefine them instead!
     */
    public Collection<Pac4jFeatureSupport> supportedFeatures() {
        ArrayList<Pac4jFeatureSupport> res = new ArrayList<>();
        res.add(new DefaultFeatureSupport());
        return res;
    }

    @Override
    public final void run(T configuration, Environment environment)
            throws Exception {
        final Pac4jFactory pac4j = getPac4jFactory(configuration);

        if (pac4j != null) {
            config = pac4j.build();

            for (FilterConfiguration fConf : pac4j.getGlobalFilters()) {
                environment.jersey()
                        .register(new Pac4JSecurityFilterFeature(config,
                                fConf.getSkipResponse(), fConf.getAuthorizers(),
                                fConf.getClients(), fConf.getMatchers(),
                                fConf.getMultiProfile()));
            }

            environment.jersey()
                    .register(new ServletJaxRsContextFactoryProvider(config));
            environment.jersey().register(new Pac4JSecurityFeature(config));
            environment.jersey()
                    .register(new Pac4JValueFactoryProvider.Binder());
        }
    }

    public Config getConfig() {
        return config;
    }
}
