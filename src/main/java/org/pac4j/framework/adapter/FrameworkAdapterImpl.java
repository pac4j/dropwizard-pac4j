package org.pac4j.framework.adapter;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.FrameworkParameters;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.jax.rs.filters.DefaultJaxRsHttpActionAdapter;
import org.pac4j.jax.rs.pac4j.DefaultJaxRsWebContextFactory;
import org.pac4j.jax.rs.pac4j.JaxRsAjaxRequestResolver;
import org.pac4j.jax.rs.pac4j.JaxRsContext;
import org.pac4j.jax.rs.pac4j.JaxRsFrameworkParameters;
import org.pac4j.jax.rs.pac4j.JaxRsUrlResolver;
import org.pac4j.jax.rs.servlet.pac4j.ServletSessionStoreFactory;
import org.pac4j.jee.adapter.JEEFrameworkAdapter;
import org.pac4j.jee.context.JEEContextFactory;
import org.pac4j.jee.context.JEEFrameworkParameters;
import org.pac4j.jee.context.session.JEESessionStoreFactory;
import org.pac4j.jee.http.adapter.JEEHttpActionAdapter;

/**
 * Specific config startup for Dropwizard (JAX-RS + servlet filters).
 *
 * @author Jerome LELEU
 * @since 8.0.0
 */
public class FrameworkAdapterImpl extends JEEFrameworkAdapter {

    @Override
    public void applyDefaultSettingsIfUndefined(final Config config) {
        CommonHelper.assertNotNull("config", config);

        config.setWebContextFactoryIfUndefined(this::buildWebContext);
        config.setSessionStoreFactory(parameters -> {
            if (parameters instanceof JEEFrameworkParameters) {
                return JEESessionStoreFactory.INSTANCE.newSessionStore(parameters);
            }
            return ServletSessionStoreFactory.INSTANCE.newSessionStore(parameters);
        });
        config.setHttpActionAdapterIfUndefined((action, context) -> {
            if (context instanceof JaxRsContext) {
                return DefaultJaxRsHttpActionAdapter.INSTANCE.adapt(action, context);
            }
            return JEEHttpActionAdapter.INSTANCE.adapt(action, context);
        });

        var clients = config.getClients();
        if (clients != null) {
            if (clients.getAjaxRequestResolver() == null) {
                clients.setAjaxRequestResolver(new JaxRsAjaxRequestResolver());
            }
            if (clients.getUrlResolver() == null) {
                clients.setUrlResolver(new JaxRsUrlResolver());
            }
        }

        super.applyDefaultSettingsIfUndefined(config);
    }

    protected WebContext buildWebContext(final FrameworkParameters parameters) {
        if (parameters instanceof JaxRsFrameworkParameters) {
            return DefaultJaxRsWebContextFactory.INSTANCE.newContext(parameters);
        } else if (parameters instanceof JEEFrameworkParameters) {
            return JEEContextFactory.INSTANCE.newContext(parameters);
        }
        throw new TechnicalException("Unsupported framework parameters: " + parameters);
    }

    @Override
    public String toString() {
        return "Dropwizard";
    }
}
