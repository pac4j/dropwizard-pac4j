package org.pac4j.framework.adapter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.adapter.FrameworkAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.session.SessionStoreFactory;
import org.pac4j.jax.rs.pac4j.NoOpSessionStoreFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

final class FrameworkAdapterImplTest {

    @Test
    void shouldKeepUserDefinedSessionStoreFactory() {
        Config config = new Config();
        SessionStoreFactory sessionStoreFactory = new NoOpSessionStoreFactory();
        config.setSessionStoreFactory(sessionStoreFactory);

        FrameworkAdapter frameworkAdapter = new FrameworkAdapterImpl();
        frameworkAdapter.applyDefaultSettingsIfUndefined(config);

        assertSame(sessionStoreFactory, config.getSessionStoreFactory());
    }

    @Test
    void shouldSetDefaultSessionStoreFactoryIfUndefined() {
        Config config = new Config();

        assertNull(config.getSessionStoreFactory());

        FrameworkAdapter frameworkAdapter = new FrameworkAdapterImpl();
        frameworkAdapter.applyDefaultSettingsIfUndefined(config);

        assertNotNull(config.getSessionStoreFactory());
    }
}
