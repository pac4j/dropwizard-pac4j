package org.pac4j.dropwizard.factory;

import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.DirectFormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

/**
 * Build a configuration for end-to-end tests with direct basic/form clients.
 */
public final class DirectClientsConfigFactory implements ConfigFactory {

    @Override
    public Config build(Object... parameters) {
        SimpleTestUsernamePasswordAuthenticator authenticator = new SimpleTestUsernamePasswordAuthenticator();
        DirectBasicAuthClient basic = new DirectBasicAuthClient(authenticator);
        DirectFormClient form = new DirectFormClient(authenticator);
        return new Config(basic, form);
    }
}
