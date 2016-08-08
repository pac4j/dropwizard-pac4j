package io.dropwizard.pac4j;

import io.dropwizard.Configuration;

public interface Pac4jConfiguration<T extends Configuration> {
    Pac4jFactory getPac4jFactory(T configuration);
}
