package org.pac4j.dropwizard.bundle;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.eclipse.jetty.server.session.SessionHandler;
import org.junit.Test;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.dropwizard.AbstractApplicationTest;
import org.pac4j.jax.rs.pac4j.JaxRsUrlResolver;
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ConfigOverride;

public class BundleFactoryTest extends AbstractApplicationTest {

    public static class App extends TestApplication<TestConfiguration> {

    }

    private static final Condition<Object> CONDCLASS = new Condition<>(
            s -> {
            		return s.equals(ServletJaxRsContextFactoryProvider.class);
            	},
            "pac4j class");

    @Test
    public void noPac4jInConfig() {
        setup(App.class, "no-pac4j.yaml");

        App app = dropwizardTestSupport.getApplication();
        ObjectMapper om = dropwizardTestSupport.getObjectMapper();
        Environment env = dropwizardTestSupport.getEnvironment();

        assertThat(app.bundle.getConfig()).isNull();
        // if one use the bundle with null pac4j, one will get the mixing
        // registered anyway
        assertThat(om.findMixInClassFor(Client.class)).isNotNull();
        assertThat(env.jersey().getResourceConfig().getSingletons())
                .doesNotHave(CONDCLASS);
    }

    @Test
    public void emptyPac4jInConfig() {
        setup(App.class, "empty-pac4j.yaml");

        App app = dropwizardTestSupport.getApplication();
        ObjectMapper om = dropwizardTestSupport.getObjectMapper();
        Environment env = dropwizardTestSupport.getEnvironment();

        Config config = app.bundle.getConfig();
        assertThat(config).isNotNull();
        // this is the default url resolver!
        assertThat(config.getClients().getUrlResolver())
                .isInstanceOf(JaxRsUrlResolver.class);
        assertThat(om.findMixInClassFor(Client.class)).isNotNull();
        assertThat(env.jersey().getResourceConfig().getClasses())
                .haveAtLeastOne(CONDCLASS);

        assertThat(env.getApplicationContext().getSessionHandler())
                .isInstanceOf(SessionHandler.class);
    }

    @Test
    public void sessionEnabledForced() {
        setup(App.class, "empty-pac4j.yaml",
                ConfigOverride.config("pac4j.sessionEnabled", "true"));

        App app = dropwizardTestSupport.getApplication();
        Environment env = dropwizardTestSupport.getEnvironment();

        Config config = app.bundle.getConfig();
        assertThat(config).isNotNull();

        assertThat(env.getApplicationContext().getSessionHandler())
                .isInstanceOf(SessionHandler.class);
    }

    @Test
    public void sessiondisabledForced() {
        setup(App.class, "empty-pac4j.yaml",
                ConfigOverride.config("pac4j.sessionEnabled", "false"));

        App app = dropwizardTestSupport.getApplication();
        Environment env = dropwizardTestSupport.getEnvironment();

        Config config = app.bundle.getConfig();
        assertThat(config).isNotNull();

        assertThat(env.getApplicationContext().getSessionHandler()).isNull();
    }

}
