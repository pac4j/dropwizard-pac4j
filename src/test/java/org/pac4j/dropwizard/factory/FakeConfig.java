package org.pac4j.dropwizard.factory;

import java.util.Map;

import org.pac4j.core.config.Config;

/**
 * Fake configuration for tests.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public final class FakeConfig extends Config {

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
}
