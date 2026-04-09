package org.pac4j.dropwizard.factory;

import org.pac4j.core.authorization.authorizer.IsAnonymousAuthorizer;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.matcher.PathMatcher;
import org.pac4j.oauth.client.FacebookClient;

/**
 * Fake configuration factory for tests.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public final class FakeConfigFactory implements ConfigFactory {

    @Override
    public Config build(Object... parameters) {
        FakeConfig config = new FakeConfig();
        config.addClient(new FacebookClient("fbId", "fbSecret"));
        config.setAuthorizer(new IsAnonymousAuthorizer());
        config.setMatcher(new PathMatcher().excludeRegex("^/user/session$"));
        return config;
    }
}
