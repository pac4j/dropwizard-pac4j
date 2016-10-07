package org.pac4j.dropwizard;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.ConfigSingleton;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<TestConfiguration> {
    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        final Pac4jBundle<TestConfiguration> bundle = new Pac4jBundle<TestConfiguration>() {
            @Override
            public Pac4jFactory getPac4jFactory(
                    TestConfiguration configuration) {
                return configuration.pac4jFactory;
            }
        };
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(TestConfiguration config, Environment env)
            throws Exception {
        ConfigSingleton.getConfig()
                .setClients(new Clients(new DirectBasicAuthClient(
                        new SimpleTestUsernamePasswordAuthenticator())));
        env.jersey().register(new DogsResource());
    }
}
