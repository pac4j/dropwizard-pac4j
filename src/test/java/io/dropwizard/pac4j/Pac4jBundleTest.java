package io.dropwizard.pac4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pac4j.core.config.DefaultConfigFactory;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.j2e.filter.SecurityFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class Pac4jBundleTest {
    private static final Pac4jFactory pac4jFactory = new Pac4jFactory();
    private static final Configuration config = mock(Configuration.class);
    private static final Environment environment = mock(Environment.class);
    private static final ServletEnvironment servletEnvironment =
        mock(ServletEnvironment.class);
    private static final FilterRegistration.Dynamic filterRegistration =
        mock(FilterRegistration.Dynamic.class);

    private static final Pac4jBundle<Configuration> bundle = new Pac4jBundle<Configuration>() {
        @Override
        public Pac4jFactory getPac4jFactory(Configuration configuration) {
            return pac4jFactory;
        }
    };

    private static final String clients = "foo";
    private static final String authorizers = "bar";
    private static final String matchers = "baz";

    private static final Map<String, String> expectedInitParameters = ImmutableMap.of(
        Pac4jConstants.CONFIG_FACTORY, DefaultConfigFactory.class.getName(),
        Pac4jConstants.CLIENTS, clients,
        Pac4jConstants.AUTHORIZERS, authorizers,
        Pac4jConstants.MATCHERS, matchers,
        Pac4jConstants.MULTI_PROFILE, "false"
    );

    @Before
    public void setUp() throws Exception {
        pac4jFactory.setConfigFactory(DefaultConfigFactory.class.getName());
        pac4jFactory.setClients(clients);
        pac4jFactory.setAuthorizers(authorizers);
        pac4jFactory.setMatchers(matchers);
        pac4jFactory.setMultiProfile(false);

        when(environment.servlets()).thenReturn(servletEnvironment);
        when(servletEnvironment.addFilter(anyString(), any(Class.class)))
            .thenReturn(filterRegistration);
        when(filterRegistration.setInitParameters(any(Map.class)))
            .thenReturn(ImmutableSet.of());
    }

    @Test
    public void addsSecurityFilterToApplicationServletHandler() throws Exception {
        bundle.run(config, environment);

        verify(environment).servlets();
        verify(servletEnvironment).addFilter(
            eq(SecurityFilter.class.getName()),
            eq(SecurityFilter.class)
        );
        verify(filterRegistration).addMappingForUrlPatterns(
            eq(EnumSet.of(DispatcherType.REQUEST)),
            eq(true),
            eq("/*")
        );
        verify(filterRegistration).setInitParameters(eq(expectedInitParameters));
    }
}
