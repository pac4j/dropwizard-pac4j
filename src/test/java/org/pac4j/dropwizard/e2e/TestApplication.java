package org.pac4j.dropwizard.e2e;

import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<TestConfiguration> {
    
    private final Pac4jBundle<TestConfiguration> bundle = new Pac4jBundle<TestConfiguration>() {
        @Override
        public Pac4jFactory getPac4jFactory(TestConfiguration configuration) {
            return configuration.pac4jFactory;
        }
    };

    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(bundle);
    }
    
    @Override
    public void run(TestConfiguration config, Environment env)
            throws Exception {
        env.jersey().register(new DogsResource());
    }
}
