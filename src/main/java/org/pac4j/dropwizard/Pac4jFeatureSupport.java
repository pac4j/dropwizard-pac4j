package org.pac4j.dropwizard;

import io.dropwizard.core.setup.Bootstrap;

/**
 * An interface to add optional Jackson's behaviour customizations to
 * {@link Pac4jBundle}.
 *
 * @author Victor Noel - Linagora
 * @since 1.0.0
 *
 */
public interface Pac4jFeatureSupport {
    void setup(Bootstrap<?> bootstrap);
}
