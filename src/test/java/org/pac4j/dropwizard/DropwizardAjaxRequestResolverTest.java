package org.pac4j.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.jax.rs.pac4j.JaxRsContext;
import org.pac4j.test.context.MockWebContext;
import org.pac4j.test.context.session.MockSessionStore;

final class DropwizardAjaxRequestResolverTest {

    private final DropwizardAjaxRequestResolver resolver = new DropwizardAjaxRequestResolver();

    @Test
    void jaxRsRequestIsAlwaysAjax() {
        assertThat(resolver.isAjax(new CallContext(mock(JaxRsContext.class), new MockSessionStore()))).isTrue();
    }

    @Test
    void servletRequestIsNotAjaxByDefault() {
        assertThat(resolver.isAjax(new CallContext(MockWebContext.create(), new MockSessionStore()))).isFalse();
    }

    @Test
    void servletRequestWithAjaxHeaderIsAjax() {
        MockWebContext context = MockWebContext.create()
                .addRequestHeader(HttpConstants.AJAX_HEADER_NAME, HttpConstants.AJAX_HEADER_VALUE);

        assertThat(resolver.isAjax(new CallContext(context, new MockSessionStore()))).isTrue();
    }
}
