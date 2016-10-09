package org.pac4j.dropwizard.e2e;

import org.pac4j.dropwizard.Pac4jFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class TestConfiguration extends Configuration {
    Pac4jFactory pac4jFactory = new Pac4jFactory();

    TestConfiguration(@JsonProperty("pac4j") Pac4jFactory pac4jFactory) {
        this.pac4jFactory = pac4jFactory;
    }
}
