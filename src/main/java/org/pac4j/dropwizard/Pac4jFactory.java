package org.pac4j.dropwizard;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dropwizard configuration factory to configure pac4j's {@link Config},
 * {@link Clients}, global JAX-RS
 * {@link org.pac4j.jax.rs.filters.SecurityFilter}s as well as Servlet's
 * {@link org.pac4j.jee.filter.SecurityFilter}s,
 * {@link org.pac4j.jee.filter.CallbackFilter}s and
 * {@link org.pac4j.jee.filter.LogoutFilter}s.
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

    private boolean sessionEnabled = true;

    private String configFactory;

    @JsonProperty
    public String getConfigFactory() {
        return configFactory;
    }

    @JsonProperty
    public void setConfigFactory(final String configFactory) {
        this.configFactory = configFactory;
    }

    @JsonProperty
    public List<JaxRsSecurityFilterConfiguration> getGlobalFilters() {
        return globalFilters;
    }

    @JsonProperty
    public void setGlobalFilters(
            List<JaxRsSecurityFilterConfiguration> filters) {
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
        var config = new Config();
        if (configFactory != null && !configFactory.isBlank()) {
            config = ConfigBuilder.build(configFactory);
        }
        FrameworkAdapter.INSTANCE.applyDefaultSettingsIfUndefined(config);
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
     * @see org.pac4j.jee.filter.SecurityFilter
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
    }

    /**
     * @since 1.1.0
     * @see org.pac4j.jee.filter.CallbackFilter
     */
    public static class ServletCallbackFilterConfiguration {

        @NotNull
        private String mapping;

        private String defaultUrl;

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
        public void setRenewSession(Boolean renewSession) {
            this.renewSession = renewSession;
        }
    }

    /**
     * @since 1.1.0
     * @see org.pac4j.jee.filter.LogoutFilter
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
