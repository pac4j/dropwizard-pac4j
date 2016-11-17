package org.pac4j.dropwizard;

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
    Pac4jFactory getPac4jFactory(T configuration);
}
