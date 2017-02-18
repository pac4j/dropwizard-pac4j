package org.pac4j.dropwizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.LogoutLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.http.CallbackUrlResolver;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.core.matching.Matcher;
import org.pac4j.jax.rs.annotations.Pac4JSecurity;
import org.pac4j.jax.rs.pac4j.JaxRsCallbackUrlResolver;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dropwizard configuration factory to configure pac4j's {@link Config},
 * {@link Clients}, global JAX-RS
 * {@link org.pac4j.jax.rs.filters.SecurityFilter}s as well as Servlet's
 * {@link org.pac4j.j2e.filter.SecurityFilter}s,
 * {@link org.pac4j.j2e.filter.CallbackFilter}s and
 * {@link org.pac4j.j2e.filter.ApplicationLogoutFilter}s.
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
    private List<JaxRsSecurityFilterConfiguration> globalFilters = new ArrayList<>();

    @NotNull
    private ServletConfiguration servlet = new ServletConfiguration();

    private SecurityLogic securityLogic;

    private CallbackLogic callbackLogic;

    private LogoutLogic logoutLogic;

    private String clientNameParameter;

    private String callbackUrl;

    private CallbackUrlResolver callbackUrlResolver = new JaxRsCallbackUrlResolver();

    private HttpActionAdapter httpActionAdapter;

    private boolean sessionEnabled = true;

    @NotNull
    private List<AuthorizationGenerator> authorizationGenerators = new ArrayList<>();

    @NotNull
    private Map<String, Matcher> matchers = new HashMap<>();

    @NotNull
    private List<Client> clients = new ArrayList<>();

    private String defaultClient = null;

    private String defaultClients = null;

    @NotNull
    private Map<String, Authorizer> authorizers = new HashMap<>();

    @JsonProperty
    public List<JaxRsSecurityFilterConfiguration> getGlobalFilters() {
        return globalFilters;
    }

    @JsonProperty
    public void setGlobalFilters(List<JaxRsSecurityFilterConfiguration> filters) {
        this.globalFilters = filters;
    }

    /**
     * @since 1.1.0
     * @return configuration for servlet filters
     */
    @JsonProperty
    public ServletConfiguration getServlet() {
        return servlet;
    }

    /**
     * @since 1.1.0
     * 
     * @param servlet
     *            configuration for servlet filters
     */
    @JsonProperty
    public void setServlet(ServletConfiguration servlet) {
        this.servlet = servlet;
    }

    /**
     * @since 1.1.1
     * 
     * @param securityLogic
     *            the {@link SecurityLogic} to use globally
     */
    @JsonProperty
    public void setSecurityLogic(SecurityLogic securityLogic) {
        this.securityLogic = securityLogic;
    }

    @JsonProperty
    public SecurityLogic getSecurityLogic() {
        return securityLogic;
    }

    /**
     * @since 1.1.1
     * 
     * @param callbackLogic
     *            the {@link CallbackLogic} to use globally
     */
    @JsonProperty
    public void setCallbackLogic(CallbackLogic callbackLogic) {
        this.callbackLogic = callbackLogic;
    }

    @JsonProperty
    public CallbackLogic getCallbackLogic() {
        return callbackLogic;
    }

    /**
     * @since 1.1.1
     * 
     * @param logoutLogic
     *            the {@link LogoutLogic} to use globally
     */
    @JsonProperty
    public void setApplicationLogoutLogic(LogoutLogic logoutLogic) {
        this.logoutLogic = logoutLogic;
    }

    @JsonProperty
    public LogoutLogic getApplicationLogoutLogic() {
        return logoutLogic;
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
        this.authorizationGenerators = authorizationGenerators;
    }

    @JsonProperty
    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    @JsonProperty
    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = matchers;
    }

    @JsonProperty
    public List<Client> getClients() {
        return clients;
    }

    @JsonProperty
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    /**
     * @since 1.1.0
     * @return the name of the client to set with
     *         {@link Clients#setDefaultClient(Client)}
     */
    @JsonProperty
    public String getDefaultClient() {
        return defaultClient;
    }

    /**
     * @since 1.1.0
     * @param defaultClient
     *            the name of the client to set with
     *            {@link Clients#setDefaultClient(Client)}
     */
    @JsonProperty
    public void setDefaultClient(String defaultClient) {
        this.defaultClient = defaultClient;
    }

    /**
     * @since 1.1.0
     * @return the names of he clients to use by default with
     *         {@link Pac4JSecurity}
     */
    @JsonProperty
    public String getDefaultClients() {
        return defaultClients;
    }

    /**
     * @since 1.1.0
     * @param defaultClients
     *            the names of he clients to use by default with
     *            {@link Pac4JSecurity}
     */
    @JsonProperty
    public void setDefaultClients(String defaultClients) {
        this.defaultClients = defaultClients;
    }

    @JsonProperty
    public Map<String, Authorizer> getAuthorizers() {
        return authorizers;
    }

    @JsonProperty
    public void setAuthorizers(Map<String, Authorizer> authorizers) {
        this.authorizers = authorizers;
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

    /**
     * @since 1.1.1
     * @return an {@link HttpActionAdapter}
     */
    @JsonProperty
    public HttpActionAdapter getHttpActionAdapter() {
        return httpActionAdapter;
    }

    @JsonProperty
    public void setHttpActionAdapter(HttpActionAdapter httpActionAdapter) {
        this.httpActionAdapter = httpActionAdapter;
    }

    /**
     * @since 1.1.0
     * @return <code>true</code> if the session management is to be enabled at
     *         Jetty level
     */
    @JsonProperty
    public boolean getSessionEnabled() {
        return sessionEnabled;
    }

    /**
     * @param sessionEnabled
     *            if <code>true</code> session management will be enabled at the
     *            Jetty level, if <code>false</code> it won't.
     * @since 1.1.0
     */
    @JsonProperty
    public void setSessionEnabled(boolean sessionEnabled) {
        this.sessionEnabled = sessionEnabled;
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

        if (defaultClient != null) {
            boolean found = false;
            for (Client c : clients) {
                if (defaultClient.equals(c.getName())) {
                    client.setDefaultClient(c);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Client '" + defaultClient
                        + "' is not one of the configured clients");
            }
        }

        config.setSecurityLogic(securityLogic);
        config.setCallbackLogic(callbackLogic);
        config.setLogoutLogic(logoutLogic);
        config.setHttpActionAdapter(httpActionAdapter);
        config.setAuthorizers(authorizers);
        config.setMatchers(matchers);

        return config;
    }

    /**
     * @since 1.1.0
     */
    public static class ServletConfiguration {

        @NotNull
        private List<ServletSecurityFilterConfiguration> security = new ArrayList<>();

        @NotNull
        private List<ServletCallbackFilterConfiguration> callback = new ArrayList<>();

        @NotNull
        private List<ServletLogoutFilterConfiguration> logout = new ArrayList<>();

        @JsonProperty
        public List<ServletSecurityFilterConfiguration> getSecurity() {
            return security;
        }

        @JsonProperty
        public List<ServletCallbackFilterConfiguration> getCallback() {
            return callback;
        }

        @JsonProperty
        public List<ServletLogoutFilterConfiguration> getLogout() {
            return logout;
        }

        @JsonProperty
        public void setSecurity(
                List<ServletSecurityFilterConfiguration> filters) {
            this.security = filters;
        }

        @JsonProperty
        public void setCallback(
                List<ServletCallbackFilterConfiguration> filters) {
            this.callback = filters;
        }

        @JsonProperty
        public void setLogout(List<ServletLogoutFilterConfiguration> filters) {
            this.logout = filters;
        }

    }

    /**
     * @since 1.0.0
     * @see org.pac4j.jax.rs.filters.SecurityFilter
     */
    public static class JaxRsSecurityFilterConfiguration
            extends SecurityFilterConfiguration {

        private Boolean skipResponse;

        @JsonProperty
        public Boolean getSkipResponse() {
            return skipResponse;
        }

        @JsonProperty
        public void setSkipResponse(Boolean skipResponse) {
            this.skipResponse = skipResponse;
        }
    }

    /**
     * @since 1.1.0
     * @see org.pac4j.j2e.filter.SecurityFilter
     */
    public static class ServletSecurityFilterConfiguration
            extends SecurityFilterConfiguration {

        @NotNull
        private String mapping = "/*";

        @JsonProperty
        public String getMapping() {
            return mapping;
        }

        @JsonProperty
        public void setMapping(String mapping) {
            this.mapping = mapping;
        }
    }

    /**
     * @since 1.1.0
     */
    public abstract static class SecurityFilterConfiguration {

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

    /**
     * @since 1.1.0
     * @see org.pac4j.j2e.filter.CallbackFilter
     */
    public static class ServletCallbackFilterConfiguration {

        @NotNull
        private String mapping;

        private String defaultUrl;

        private Boolean multiProfile;

        private Boolean renewSession;

        @JsonProperty
        public String getMapping() {
            return mapping;
        }

        @JsonProperty
        public String getDefaultUrl() {
            return defaultUrl;
        }

        @JsonProperty
        public Boolean getMultiProfile() {
            return multiProfile;
        }

        @JsonProperty
        public Boolean getRenewSession() {
            return renewSession;
        }

        @JsonProperty
        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        @JsonProperty
        public void setDefaultUrl(String defaultUrl) {
            this.defaultUrl = defaultUrl;
        }

        @JsonProperty
        public void setMultiProfile(Boolean multiProfile) {
            this.multiProfile = multiProfile;
        }

        @JsonProperty
        public void setRenewSession(Boolean renewSession) {
            this.renewSession = renewSession;
        }
    }

    /**
     * @since 1.1.0
     * @see org.pac4j.j2e.filter.ApplicationLogoutFilter
     */
    public static class ServletLogoutFilterConfiguration {

        @NotNull
        private String mapping;

        private String defaultUrl;

        private String logoutUrlPattern;

        private Boolean localLogout;

        private Boolean destroySession;

        private Boolean centralLogout;

        @JsonProperty
        public String getMapping() {
            return mapping;
        }

        @JsonProperty
        public String getDefaultUrl() {
            return defaultUrl;
        }

        @JsonProperty
        public String getLogoutUrlPattern() {
            return logoutUrlPattern;
        }

        @JsonProperty
        public Boolean getLocalLogout() {
            return localLogout;
        }

        @JsonProperty
        public Boolean getDestroySession() {
            return destroySession;
        }

        @JsonProperty
        public Boolean getCentralLogout() {
            return centralLogout;
        }

        @JsonProperty
        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        @JsonProperty
        public void setDefaultUrl(String defaultUrl) {
            this.defaultUrl = defaultUrl;
        }

        @JsonProperty
        public void setLogoutUrlPattern(String logoutUrlPattern) {
            this.logoutUrlPattern = logoutUrlPattern;
        }

        @JsonProperty
        public void setLocalLogout(Boolean localLogout) {
            this.localLogout = localLogout;
        }

        @JsonProperty
        public void setDestroySession(Boolean destroySession) {
            this.destroySession = destroySession;
        }

        @JsonProperty
        public void setCentralLogout(Boolean centralLogout) {
            this.centralLogout = centralLogout;
        }
    }
}
