package io.dropwizard.pac4j;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.j2e.filter.SecurityFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * A {@link ConfiguredBundle} which installs a {@link SecurityFilter}
 * into a Dropwizard application's Jetty servlet handler chain.
 */
public abstract class Pac4jBundle<T extends Configuration>
    implements ConfiguredBundle<T>, Pac4jConfiguration<T>
{
    @Override
    public final void initialize(Bootstrap<?> bootstrap) { }

    @Override
    public final void run(T configuration, Environment environment) throws Exception {
        final Pac4jFactory pac4jFactory = getPac4jFactory(configuration);

        final ImmutableMap<String, String> initParameters = ImmutableMap.of(
            Pac4jConstants.CONFIG_FACTORY, pac4jFactory.getConfigFactory(),
            Pac4jConstants.CLIENTS, pac4jFactory.getClients(),
            Pac4jConstants.AUTHORIZERS, pac4jFactory.getAuthorizers(),
            Pac4jConstants.MATCHERS, pac4jFactory.getMatchers(),
            Pac4jConstants.MULTI_PROFILE, (pac4jFactory.getMultiProfile()) ? "true" : "false"
        );

        final FilterRegistration.Dynamic filterRegistration = environment.servlets()
            .addFilter(SecurityFilter.class.getName(), SecurityFilter.class);

        filterRegistration
            .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        filterRegistration
            .setInitParameters(initParameters);
    }
}
