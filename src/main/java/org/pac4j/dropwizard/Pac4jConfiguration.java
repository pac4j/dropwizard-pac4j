package org.pac4j.dropwizard;

import org.pac4j.core.config.Config;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;

/**
 * 
 * {@link Bundle}s can extend this to get a {@link Pac4jFactory}.
 * 
 * @author Evan Meagher
 * @since 1.0.0
 *
 * @param <T>
 *            the application configuration type
 */
public interface Pac4jConfiguration<T extends Configuration> {

    /**
     * The factory can comes from the configuration but also can be built by
     * hand in this method if desired.
     * 
     * @param configuration
     *            the application's configuration
     * @return a {@link Pac4jFactory} that will be used to build a pac4j
     *         {@link Config}.
     */
    Pac4jFactory getPac4jFactory(T configuration);
}
