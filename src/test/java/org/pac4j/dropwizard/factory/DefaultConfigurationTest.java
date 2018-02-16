package org.pac4j.dropwizard.factory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.Matcher;
import org.pac4j.core.matching.PathMatcher;
import org.pac4j.dropwizard.AbstractConfigurationTest;
import org.pac4j.dropwizard.Pac4jFactory;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.jax.rs.pac4j.JaxRsAjaxRequestResolver;
import org.pac4j.jax.rs.pac4j.JaxRsUrlResolver;
import org.pac4j.oauth.client.FacebookClient;

public class DefaultConfigurationTest extends AbstractConfigurationTest {

    @Test
    public void matchers() throws Exception {
        Pac4jFactory conf = getPac4jFactory("matchers.yaml");
        Config config = conf.build();

        assertThat(config).isNotNull();
        assertThat(config.getMatchers()).hasSize(1)
                .containsKey("excludeUserSession");
        Matcher m = config.getMatchers().values().iterator().next();
        assertThat(m).isInstanceOf(PathMatcher.class);
        assertThat(((PathMatcher) m).getExcludedPatterns().stream().map(Pattern::toString))
                .containsExactlyInAnyOrder("^/user/session$");
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

    @Test
    public void allOptionsClients() throws Exception {
        Pac4jFactory conf = getPac4jFactory("alloptions-pac4j.yaml");
        Config config = conf.build();

        assertThat(config).isExactlyInstanceOf(FakeConfig.class);
        final FakeConfig fakeConfig = (FakeConfig) config;
        assertThat(fakeConfig.getProperties().size()).isEqualTo(2);
        assertThat(config.getClients().getClients()).hasSize(2);

        Client client0 = config.getClients().getClients().get(0);
        assertThat(client0).isExactlyInstanceOf(FacebookClient.class);
        assertThat(((FacebookClient) client0).getKey()).isEqualTo("fbId");

        Client client1 = config.getClients().getClients().get(1);
        assertThat(client1).isInstanceOf(DirectBasicAuthClient.class);
        assertThat(client1.getName()).isEqualTo("DirectBasicAuthClient");
        assertThat(((DirectBasicAuthClient) client1).getAuthenticator())
                .isNotNull()
                .isInstanceOf(SimpleTestUsernamePasswordAuthenticator.class);

        assertThat(config.getAuthorizers().size()).isEqualTo(1);

        assertThat(config.getMatchers().size()).isEqualTo(1);
    }

    @Test
    public void clientsAndProperties() throws Exception {
        Pac4jFactory conf = getPac4jFactory("clientsandproperties-pac4j.yaml");
        Config config = conf.build();

        assertThat(config.getClients().getClients()).hasSize(2);

        Client client0 = config.getClients().getClients().get(0);
        assertThat(client0).isExactlyInstanceOf(FacebookClient.class);
        assertThat(((FacebookClient) client0).getKey()).isEqualTo("fbId");

        Client client1 = config.getClients().getClients().get(1);
        assertThat(client1).isInstanceOf(DirectBasicAuthClient.class);
        assertThat(client1.getName()).isEqualTo("DirectBasicAuthClient");
        assertThat(((DirectBasicAuthClient) client1).getAuthenticator())
                .isNotNull()
                .isInstanceOf(SimpleTestUsernamePasswordAuthenticator.class);

        assertThat(config.getAuthorizers().size()).isEqualTo(0);

        assertThat(config.getMatchers().size()).isEqualTo(0);
    }

    @Test
    public void clientsProperties() throws Exception {
        Pac4jFactory conf = getPac4jFactory("clientsproperties-pac4j.yaml");
        Config config = conf.build();

        assertThat(config.getClients().getClients()).hasSize(1);

        Client client0 = config.getClients().getClients().get(0);
        assertThat(client0).isExactlyInstanceOf(FacebookClient.class);
        assertThat(((FacebookClient) client0).getKey()).isEqualTo("fbId");

        assertThat(config.getAuthorizers().size()).isEqualTo(1);

        assertThat(config.getMatchers().size()).isEqualTo(0);
    }

    @Test
    public void defaultsUnset() throws Exception {
        Pac4jFactory conf = getPac4jFactory("defaults.yaml");
        Config config = conf.build();

        Clients clients = config.getClients();
        // check that it is the correct file
        assertThat(clients.getCallbackUrl()).isEqualTo("test");
        // the default settings should be used!
        assertThat(clients.getAjaxRequestResolver()).isExactlyInstanceOf(JaxRsAjaxRequestResolver.class);
        assertThat(clients.getUrlResolver()).isExactlyInstanceOf(JaxRsUrlResolver.class);
    }
}
