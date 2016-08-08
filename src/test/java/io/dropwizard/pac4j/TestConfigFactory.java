package io.dropwizard.pac4j;

import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

public class TestConfigFactory implements ConfigFactory {
    public Config build() {
        final Config config = new Config(
            new DirectBasicAuthClient(
                new SimpleTestUsernamePasswordAuthenticator()
            )
        );
        System.out.println(config.getClients().findClient(DirectBasicAuthClient.class.getSimpleName()));
        return config;
    }
}
