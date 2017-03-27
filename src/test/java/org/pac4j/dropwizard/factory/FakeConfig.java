package org.pac4j.dropwizard.factory;

import org.pac4j.jax.rs.pac4j.JaxRsConfig;

import java.util.Map;

/**
 * Fake configuration for tests.
 *
 * @author Jerome Leleu
 * @since 2.0.0
 */
public final class FakeConfig extends JaxRsConfig {

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
}
