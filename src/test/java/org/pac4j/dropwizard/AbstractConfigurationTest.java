package org.pac4j.dropwizard;

import java.io.File;

import com.google.common.io.Resources;

import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

public abstract class AbstractConfigurationTest {

    protected Pac4jFactory getPac4jFactory(String resourceName)
            throws Exception {
        return new YamlConfigurationFactory<>(Pac4jFactory.class,
                Validators.newValidator(), Jackson.newObjectMapper(), "dw").build(
                        new File(Resources.getResource(resourceName).toURI()));
    }
}
