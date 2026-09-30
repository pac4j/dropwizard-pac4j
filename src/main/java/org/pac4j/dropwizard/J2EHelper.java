package org.pac4j.dropwizard;

import java.util.Arrays;
import java.util.EnumSet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletHandler;

import org.pac4j.core.config.Config;
import org.pac4j.dropwizard.Pac4jFactory.ServletCallbackFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletLogoutFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletSecurityFilterConfiguration;

import io.dropwizard.core.setup.Environment;
import org.pac4j.jee.config.AbstractConfigFilter;
import org.pac4j.jee.filter.CallbackFilter;
import org.pac4j.jee.filter.LogoutFilter;
import org.pac4j.jee.filter.SecurityFilter;

/**
 *
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.1.0
 *
 */
public final class J2EHelper {

    private J2EHelper() {
        // utility class
    }

    public static void registerSecurityFilter(Environment environment,
            Config config, ServletSecurityFilterConfiguration fConf) {

        final SecurityFilter filter = new SecurityFilter();

        filter.setClients(fConf.getClients());
        filter.setAuthorizers(fConf.getAuthorizers());
        filter.setMatchers(fConf.getMatchers());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    public static void registerCallbackFilter(Environment environment,
            Config config, ServletCallbackFilterConfiguration fConf) {

        final CallbackFilter filter = new CallbackFilter();

        filter.setDefaultUrl(fConf.getDefaultUrl());
        filter.setRenewSession(fConf.getRenewSession());
        filter.setDefaultClient(fConf.getDefaultClient());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    public static void registerLogoutFilter(Environment environment,
            Config config, ServletLogoutFilterConfiguration fConf) {

        final LogoutFilter filter = new LogoutFilter();

        filter.setDefaultUrl(fConf.getDefaultUrl());
        filter.setLogoutUrlPattern(fConf.getLogoutUrlPattern());
        filter.setLocalLogout(fConf.getLocalLogout());
        filter.setDestroySession(fConf.getDestroySession());
        filter.setCentralLogout(fConf.getCentralLogout());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    private static void registerFilter(Environment environment, Config config,
                                       AbstractConfigFilter filter, String mapping) {
        filter.setConfig(config);

        final String name = uniqueFilterName(environment,
                filter.getClass().getName());
        final FilterRegistration.Dynamic filterRegistration = environment
                .servlets().addFilter(name, filter);

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), true, mapping);
    }

    /**
     * Jetty binds a mapping to a filter by its name: several filters of the
     * same class must have distinct names, otherwise the last registered one
     * would be applied to all their mappings.
     */
    private static String uniqueFilterName(Environment environment,
            String baseName) {
        final ServletHandler handler = environment.getApplicationContext()
                .getServletHandler();
        String name = baseName;
        int i = 1;
        while (isFilterNameUsed(handler, name)) {
            i++;
            name = baseName + "-" + i;
        }
        return name;
    }

    private static boolean isFilterNameUsed(ServletHandler handler,
            String name) {
        final FilterHolder[] filters = handler.getFilters();
        return filters != null && Arrays.stream(filters)
                .anyMatch(f -> name.equals(f.getName()));
    }
}
