package org.pac4j.dropwizard;

import io.dropwizard.setup.Environment;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.dropwizard.Pac4jFactory.ServletCallbackFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletLogoutFilterConfiguration;
import org.pac4j.dropwizard.Pac4jFactory.ServletSecurityFilterConfiguration;
import org.pac4j.j2e.filter.*;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

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

        final SecurityFilter filter = new SecurityFilter(config);
        /*	Merely passing in config to the ctor above doesn't apply any security
        	logic specified in the config. Seems like a bug in the SecurityFilter
        	to me. Attempt to work around it by doing that work myself if I
        	have any security logic set in the config.
         */
        SecurityLogic securityLogic = config.getSecurityLogic();
        if (securityLogic != null)
	        filter.setSecurityLogic(securityLogic);

        filter.setClients(fConf.getClients());
        filter.setAuthorizers(fConf.getAuthorizers());
        filter.setMatchers(fConf.getMatchers());
        filter.setMultiProfile(fConf.getMultiProfile());

        registerFilter(environment, config, filter, fConf.getMapping());
    }

    public static void registerCallbackFilter(Environment environment,
            Config config, ServletCallbackFilterConfiguration fConf) {

        final CallbackFilter filter = new CallbackFilter();

        filter.setDefaultUrl(fConf.getDefaultUrl());
        filter.setMultiProfile(fConf.getMultiProfile());
        filter.setRenewSession(fConf.getRenewSession());

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

        filter.setConfigOnly(config);

        final FilterRegistration.Dynamic filterRegistration = environment
                .servlets().addFilter(filter.getClass().getName(), filter);

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST), true, mapping);
    }
}
