package org.pac4j.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.core.matching.Matcher;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

public class DefaultConfigurationTest extends AbstractConfigurationTest {

    @Test
    public void matchers() throws Exception {
        Pac4jFactory conf = getPac4jFactory("matchers.yaml");
        Config config = conf.build();

        assertThat(config).isNotNull();
        assertThat(config.getMatchers()).hasSize(1)
                .containsKey("excludeUserSession");
        Matcher m = config.getMatchers().values().iterator().next();
        assertThat(m).isInstanceOf(ExcludedPathMatcher.class);
        assertThat(((ExcludedPathMatcher) m).getExcludePath())
                .isEqualTo("^/user/session$");
    }

    @Test
    public void clients() throws Exception {
        Pac4jFactory conf = getPac4jFactory("clients.yaml");
        Config config = conf.build();

        assertThat(config.getClients().getClients()).hasSize(2);

        Client client = config.getClients().getClients().get(0);
        assertThat(client).isInstanceOf(DirectBasicAuthClient.class);
        assertThat(client.getName()).isEqualTo("DirectBasicAuthClient");
        assertThat(((DirectBasicAuthClient) client).getAuthenticator())
                .isNotNull()
                .isInstanceOf(SimpleTestUsernamePasswordAuthenticator.class);

        Client client1 = config.getClients().getClients().get(1);
        assertThat(client1).isInstanceOf(DirectBasicAuthClient.class);
        assertThat(client1.getName()).isEqualTo("basic");
        assertThat(((DirectBasicAuthClient) client1).getAuthenticator())
                .isNull();
    }

}
