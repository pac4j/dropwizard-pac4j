package io.dropwizard.pac4j;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.j2e.filter.SecurityFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * A {@link ConfiguredBundle} which installs a {@link SecurityFilter}
 * into a Dropwizard application's Jetty servlet handler chain.
 */
public abstract class Pac4jBundle<T extends Configuration & Pac4jConfiguration>
    implements ConfiguredBundle<T>
{
    @Override
    public final void initialize(Bootstrap<?> bootstrap) { }

    @Override
    public final void run(T configuration, Environment environment) throws Exception {
        final ImmutableMap<String, String> initParameters = ImmutableMap.of(
            Pac4jConstants.CONFIG_FACTORY, configuration.getConfigFactory(),
            Pac4jConstants.CLIENTS, configuration.getClients(),
            Pac4jConstants.AUTHORIZERS, configuration.getAuthorizers(),
            Pac4jConstants.MATCHERS, configuration.getMatchers(),
            Pac4jConstants.MULTI_PROFILE, (configuration.isMultiProfile()) ? "true" : "false"
        );

        environment.getApplicationContext()
            .addFilter(SecurityFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST))
            .setInitParameters(initParameters);
    }
}
