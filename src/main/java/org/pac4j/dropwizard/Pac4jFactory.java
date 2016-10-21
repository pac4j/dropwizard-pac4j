package org.pac4j.dropwizard;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.http.CallbackUrlResolver;
import org.pac4j.core.matching.Matcher;
import org.pac4j.jax.rs.filters.SecurityFilter;
import org.pac4j.jax.rs.pac4j.JaxRsCallbackUrlResolver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * An interface defining getters for parameters required to configure a pac4j
 * {@link Config} and {@link SecurityFilter}.
 * 
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.0.0
 */
public class Pac4jFactory {

    @NotNull
    private ImmutableList<FilterConfiguration> filters = ImmutableList.of();

    private String clientNameParameter;

    private String callbackUrl;

    private CallbackUrlResolver callbackUrlResolver = new JaxRsCallbackUrlResolver();

    @NotNull
    private ImmutableList<AuthorizationGenerator> authorizationGenerators = ImmutableList
            .of();

    @NotNull
    private ImmutableMap<String, Matcher> matchers = ImmutableMap.of();

    @NotNull
    private ImmutableList<Client> clients = ImmutableList.of();

    @NotNull
    private ImmutableMap<String, Authorizer> authorizers = ImmutableMap.of();

    @JsonProperty
    public ImmutableList<FilterConfiguration> getFilters() {
        return filters;
    }

    @JsonProperty
    public void setFilters(List<FilterConfiguration> filters) {
        this.filters = ImmutableList.copyOf(filters);
    }

    @JsonProperty
    public String getClientNameParameter() {
        return clientNameParameter;
    }

    @JsonProperty
    public void setClientNameParameter(String clientNameParameter) {
        this.clientNameParameter = clientNameParameter;
    }

    @JsonProperty
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @JsonProperty
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @JsonProperty
    public ImmutableList<AuthorizationGenerator> getAuthorizationGenerators() {
        return authorizationGenerators;
    }
    
    @JsonProperty
    public void setAuthorizationGenerators(
            List<AuthorizationGenerator> authorizationGenerators) {
        this.authorizationGenerators = ImmutableList
                .copyOf(authorizationGenerators);
    }

    @JsonProperty
    public ImmutableMap<String, Matcher> getMatchers() {
        return matchers;
    }
    
    @JsonProperty
    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = ImmutableMap.copyOf(matchers);
    }

    @JsonProperty
    public ImmutableList<Client> getClients() {
        return clients;
    }
    
    @JsonProperty
    public void setClients(List<Client> clients) {
        this.clients = ImmutableList.copyOf(clients);
    }

    @JsonProperty
    public ImmutableMap<String, Authorizer> getAuthorizers() {
        return authorizers;
    }

    @JsonProperty
    public void setAuthorizers(Map<String, Authorizer> authorizers) {
        this.authorizers = ImmutableMap.copyOf(authorizers);
    }

    @JsonProperty
    public CallbackUrlResolver getCallbackUrlResolver() {
        return callbackUrlResolver;
    }

    @JsonProperty
    public void setCallbackUrlResolver(
            CallbackUrlResolver callbackUrlResolver) {
        this.callbackUrlResolver = callbackUrlResolver;
    }

    public Config build() {
        Clients client = new Clients();
        Config config = new Config(client);

        client.setCallbackUrl(callbackUrl);
        client.setCallbackUrlResolver(callbackUrlResolver);
        client.setClientNameParameter(clientNameParameter);
        client.setAuthorizationGenerators(authorizationGenerators);
        client.setClients(clients);

        config.setAuthorizers(authorizers);
        config.setMatchers(matchers);

        return config;
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
