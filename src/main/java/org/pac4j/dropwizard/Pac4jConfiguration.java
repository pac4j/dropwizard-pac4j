package org.pac4j.dropwizard;

import io.dropwizard.Configuration;

public interface Pac4jConfiguration<T extends Configuration> {
    Pac4jFactory getPac4jFactory(T configuration);
}
