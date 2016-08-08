package io.dropwizard.pac4j;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pac4j.j2e.filter.SecurityFilter;

/**
 * An interface defining getters for parameters required to configure
 * a {@link SecurityFilter}.
 */
public class Pac4jFactory {
    private String configFactory;
    private String clients;
    private String authorizers;
    private String matchers;
    private boolean multiProfile;

    /**
     * A pac4j config factory class name.
     */
    @JsonProperty
    public String getConfigFactory() {
        return configFactory;
    }

    /**
     * A pac4j clients string.
     */
    @JsonProperty
    public String getClients() {
        return clients;
    }

    /**
     * A pac4j authorizers string.
     */
    @JsonProperty
    public String getAuthorizers() {
        return authorizers;
    }

    /**
     * A pac4j matchers string.
     */
    @JsonProperty
    public String getMatchers() {
        return matchers;
    }

    /**
     * Returns true if multiple pac4j authentications are to be used.
     */
    @JsonProperty
    public boolean getMultiProfile() {
        return multiProfile;
    }

    @JsonProperty
    public void setConfigFactory(String configFactory) {
      this.configFactory = configFactory;
    }

    @JsonProperty
    public void setClients(String clients) {
        this.clients = clients;
    }

    @JsonProperty
    public void setAuthorizers(String authorizers) {
        this.authorizers = authorizers;
    }

    @JsonProperty
    public void setMatchers(String matchers) {
        this.matchers = matchers;
    }

    @JsonProperty
    public void setMultiProfile(Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }
}
