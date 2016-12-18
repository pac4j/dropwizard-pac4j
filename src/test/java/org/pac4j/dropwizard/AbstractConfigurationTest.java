package org.pac4j.dropwizard;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Bootstrap;

public abstract class AbstractConfigurationTest {

    protected Pac4jFactory getPac4jFactory(
            Collection<Pac4jFeatureSupport> featuresSupported,
            String resourceName) throws Exception {
        ObjectMapper om = Jackson.newObjectMapper();
        Bootstrap<?> b = mock(Bootstrap.class);
        when(b.getObjectMapper()).thenReturn(om);

        for (Pac4jFeatureSupport fs : featuresSupported) {
            fs.setup(b);
        }

        return new YamlConfigurationFactory<>(Pac4jFactory.class,
                Validators.newValidator(), om, "dw").build(
                        new File(Resources.getResource(resourceName).toURI()));
    }

    protected Collection<Pac4jFeatureSupport> featuresSupported() {
        ArrayList<Pac4jFeatureSupport> res = new ArrayList<>();
        res.add(new DefaultFeatureSupport());
        return res;
    }

    protected Pac4jFactory getPac4jFactory(String resourceName)
            throws Exception {
        return getPac4jFactory(featuresSupported(), resourceName);
    }
}
