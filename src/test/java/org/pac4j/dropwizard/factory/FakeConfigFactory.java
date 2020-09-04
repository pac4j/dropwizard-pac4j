package org.pac4j.dropwizard.factory;

import org.pac4j.core.authorization.authorizer.IsAnonymousAuthorizer;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.matcher.PathMatcher;

import java.util.Map;

/**
 * Fake configuration factory for tests.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public final class FakeConfigFactory implements ConfigFactory {

    @Override
    public Config build(Object... parameters) {
        final FakeConfig config = new FakeConfig();
        config.setProperties((Map<String, String>) parameters[0]);
        config.setAuthorizer(new IsAnonymousAuthorizer());
        config.setMatcher(new PathMatcher());
        return config;
    }
}
