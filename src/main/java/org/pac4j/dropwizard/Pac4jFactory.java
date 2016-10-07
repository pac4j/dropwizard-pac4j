package org.pac4j.dropwizard;

import java.util.Collection;

import org.pac4j.jax.rs.filter.SecurityFilter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An interface defining getters for parameters required to configure a
 * {@link SecurityFilter}.
 */
public class Pac4jFactory {

    private Collection<FilterConfiguration> filters;

    @JsonProperty
    public Collection<FilterConfiguration> getFilters() {
        return filters;
    }

    @JsonProperty
    public void setFilters(Collection<FilterConfiguration> filters) {
        this.filters = filters;
    }

    public static class FilterConfiguration {

        private Boolean skipResponse;
        private String clients;
        private String authorizers;
        private String matchers;
        private Boolean multiProfile;

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
        public Boolean getMultiProfile() {
            return multiProfile;
        }

        /**
         * Returns true if pac4j generated response are to be skipped.
         */
        @JsonProperty
        public Boolean getSkipResponse() {
            return skipResponse;
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

        @JsonProperty
        public void setSkipResponse(Boolean skipResponse) {
            this.skipResponse = skipResponse;
        }
    }
}
