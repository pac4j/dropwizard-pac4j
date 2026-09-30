package org.pac4j.dropwizard.factory;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

/**
 * Build a configuration for end-to-end tests with an indirect form client.
 */
public final class IndirectClientConfigFactory implements ConfigFactory {

    public static final String LOGIN_URL = "http://localhost/login";

    @Override
    public Config build(Object... parameters) {
        FormClient form = new FormClient(LOGIN_URL, new SimpleTestUsernamePasswordAuthenticator());
        return new Config(new Clients("http://localhost/callback", form));
    }
}
