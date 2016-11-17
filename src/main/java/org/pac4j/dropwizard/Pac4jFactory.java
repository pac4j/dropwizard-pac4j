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
import org.pac4j.jax.rs.pac4j.JaxRsCallbackUrlResolver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Dropwizard configuration factory to configure pac4j's {@link Config},
 * {@link Clients} as well as global JAX-RS
 * {@link org.pac4j.jax.rs.filters.SecurityFilter}s.
 * 
 * @see Pac4jConfiguration
 * @see org.pac4j.core.config.Config
 * @see org.pac4j.core.client.Clients
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.0.0
 */
public class Pac4jFactory {

    @NotNull
    private List<FilterConfiguration> globalFilters = ImmutableList.of();

    private String clientNameParameter;

    private String callbackUrl;

    private CallbackUrlResolver callbackUrlResolver = new JaxRsCallbackUrlResolver();

    @NotNull
    private List<AuthorizationGenerator> authorizationGenerators = ImmutableList
            .of();

    @NotNull
    private Map<String, Matcher> matchers = ImmutableMap.of();

    @NotNull
    private List<Client> clients = ImmutableList.of();

    @NotNull
    private Map<String, Authorizer> authorizers = ImmutableMap.of();

    @JsonProperty
    public List<FilterConfiguration> getGlobalFilters() {
        return globalFilters;
    }

    @JsonProperty
    public void setGlobalFilters(List<FilterConfiguration> filters) {
        this.globalFilters = ImmutableList.copyOf(filters);
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
    public List<AuthorizationGenerator> getAuthorizationGenerators() {
        return authorizationGenerators;
    }

    @JsonProperty
    public void setAuthorizationGenerators(
            List<AuthorizationGenerator> authorizationGenerators) {
        this.authorizationGenerators = ImmutableList
                .copyOf(authorizationGenerators);
    }

    @JsonProperty
    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    @JsonProperty
    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = ImmutableMap.copyOf(matchers);
    }

    @JsonProperty
    public List<Client> getClients() {
        return clients;
    }

    @JsonProperty
    public void setClients(List<Client> clients) {
        this.clients = ImmutableList.copyOf(clients);
    }

    @JsonProperty
    public Map<String, Authorizer> getAuthorizers() {
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

        if (callbackUrl != null) {
            client.setCallbackUrl(callbackUrl);
        }
        if (clientNameParameter != null) {
            client.setClientNameParameter(clientNameParameter);
        }
        client.setCallbackUrlResolver(callbackUrlResolver);
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

        @JsonProperty
        public String getClients() {
            return clients;
        }

        @JsonProperty
        public String getAuthorizers() {
            return authorizers;
        }

        @JsonProperty
        public String getMatchers() {
            return matchers;
        }

        @JsonProperty
        public Boolean getMultiProfile() {
            return multiProfile;
        }

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
