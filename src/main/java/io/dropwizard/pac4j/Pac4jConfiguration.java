package io.dropwizard.pac4j;

import io.dropwizard.Configuration;
import org.pac4j.j2e.filter.SecurityFilter;

/**
 * An interface defining getters for parameters required to configure
 * a {@link SecurityFilter}.
 *
 * Intended as a {@link Configuration} mixin.
 */
public interface Pac4jConfiguration {
    /**
     * Extracts a pac4j config factory class name from the
     * application's configuration class.
     */
    String getConfigFactory();

    /**
     * Extracts a pac4j clients string from the application's
     * configuration class.
     */
    String getClients();

    /**
     * Extracts a pac4j authorizers string from the application's
     * configuration class.
     */
    String getAuthorizers();

    /**
     * Extracts a pac4j matchers string from the application's
     * configuration class.
     */
    String getMatchers();

    /**
     * Returns true if multiple pac4j authentications are to be used.
     */
    boolean isMultiProfile();
}
