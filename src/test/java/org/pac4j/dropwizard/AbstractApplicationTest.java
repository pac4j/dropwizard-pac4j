package org.pac4j.dropwizard;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.After;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;

public class AbstractApplicationTest {

    protected DropwizardTestSupport<TestConfiguration> dropwizardTestSupport;

    protected Client client = new JerseyClientBuilder().build();

    public void setup(
            Class<? extends Application<TestConfiguration>> applicationClass,
            String configPath, ConfigOverride... configOverrides) {
        dropwizardTestSupport = new DropwizardTestSupport<>(applicationClass,
                ResourceHelpers.resourceFilePath(configPath), configOverrides);
        dropwizardTestSupport.before();
    }

    @After
    public void tearDown() {
        dropwizardTestSupport.after();
        client.close();
    }

    public static class TestConfiguration extends Configuration {

        private Pac4jFactory pac4jFactory = null;

        @JsonProperty("pac4j")
        public Pac4jFactory getPac4jFactory() {
            return pac4jFactory;
        }

        @JsonProperty("pac4j")
        public void setPac4jFactory(Pac4jFactory pac4jFactory) {
            this.pac4jFactory = pac4jFactory;
        }
    }

    public static class TestApplication<C extends TestConfiguration>
            extends Application<C> {

        public final Pac4jBundle<C> bundle = new Pac4jBundle<C>() {
            @Override
            public Pac4jFactory getPac4jFactory(C configuration) {
                return configuration.getPac4jFactory();
            }
        };

        @Override
        public void initialize(Bootstrap<C> bootstrap) {
            bootstrap.addBundle(bundle);
        }

        @Override
        public void run(C configuration, Environment environment)
                throws Exception {
            // nothing by default
        }
    }

}
