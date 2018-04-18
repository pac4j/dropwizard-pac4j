package org.pac4j.dropwizard;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jetty.server.session.SessionHandler;
import org.pac4j.core.config.Config;
import org.pac4j.dropwizard.Pac4jFactory.JaxRsSecurityFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletCallbackFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletLogoutFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletSecurityFilterConfiguration;
import org.pac4j.jax.rs.features.JaxRsConfigProvider;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
import org.pac4j.jax.rs.features.Pac4JSecurityFilterFeature;
import org.pac4j.jax.rs.filters.SecurityFilter;
import org.pac4j.jax.rs.jersey.features.Pac4JValueFactoryProvider;
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jetty.MutableServletContextHandler;
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
     * 
     * @see DefaultFeatureSupport
     * @return the features to support for configuration parsing
     */
    protected Collection<Pac4jFeatureSupport> supportedFeatures() {
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

            environment.jersey().register(new JaxRsConfigProvider(config));

            for (JaxRsSecurityFilterConfiguration fConf : pac4j
                    .getGlobalFilters()) {
                environment.jersey()
                        .register(new Pac4JSecurityFilterFeature(
                                fConf.getSkipResponse(), fConf.getAuthorizers(),
                                fConf.getClients(), fConf.getMatchers(),
                                fConf.getMultiProfile()));
            }

            for (ServletSecurityFilterConfiguration fConf : pac4j.getServlet()
                    .getSecurity()) {
                J2EHelper.registerSecurityFilter(environment, config, fConf);
            }

            for (ServletCallbackFilterConfiguration fConf : pac4j.getServlet()
                    .getCallback()) {
                J2EHelper.registerCallbackFilter(environment, config, fConf);
            }

            for (ServletLogoutFilterConfiguration fConf : pac4j.getServlet()
                    .getLogout()) {
                J2EHelper.registerLogoutFilter(environment, config, fConf);
            }

            environment.jersey()
                    .register(ServletJaxRsContextFactoryProvider.class);
            environment.jersey().register(new Pac4JSecurityFeature());
            environment.jersey()
                    .register(new Pac4JValueFactoryProvider.Binder());

            if (pac4j.getSessionEnabled()) {
                setupJettySession(environment);
            }
        }
    }

    /**
     * Override if needed, but prefer to exploit
     * {@link Pac4jFactory#setSessionEnabled(boolean)} first.
     * 
     * @param environment
     *            the dropwizard {@link Environment}
     * @since 1.1.0
     */
    protected void setupJettySession(Environment environment) {
        MutableServletContextHandler contextHandler = environment
                .getApplicationContext();
        if (contextHandler.getSessionHandler() == null) {
            contextHandler.setSessionHandler(new SessionHandler());
        }
    }

    /**
     * To be used only after this bundle has been run (i.e., in the
     * {@link Application#run(Configuration, Environment)} method.
     * 
     * @return the {@link Config} built during
     *         {@link #run(Configuration, Environment)} execution.
     */
    public Config getConfig() {
        return config;
    }
}
