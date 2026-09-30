package org.pac4j.dropwizard;

import org.pac4j.core.context.CallContext;
import org.pac4j.core.http.ajax.DefaultAjaxRequestResolver;
import org.pac4j.jax.rs.pac4j.JaxRsAjaxRequestResolver;
import org.pac4j.jax.rs.pac4j.JaxRsContext;

/**
 * JAX-RS requests are always considered as AJAX requests (like
 * {@link JaxRsAjaxRequestResolver}) while the other requests (from the servlet
 * filters) are detected as usual, so that indirect clients can redirect to the
 * identity provider.
 *
 * @author Jerome LELEU
 * @since 8.1.0
 */
public class DropwizardAjaxRequestResolver extends DefaultAjaxRequestResolver {

    @Override
    public boolean isAjax(final CallContext ctx) {
        return ctx.webContext() instanceof JaxRsContext || super.isAjax(ctx);
    }
}
