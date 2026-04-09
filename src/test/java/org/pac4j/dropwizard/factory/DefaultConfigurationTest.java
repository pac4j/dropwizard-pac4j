package org.pac4j.dropwizard.factory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.matcher.Matcher;
import org.pac4j.core.matching.matcher.PathMatcher;
import org.pac4j.dropwizard.AbstractConfigurationTest;
import org.pac4j.dropwizard.Pac4jFactory;
import org.pac4j.oauth.client.FacebookClient;

public class DefaultConfigurationTest extends AbstractConfigurationTest {

    @Test
    public void configFactory() throws Exception {
        Pac4jFactory conf = getPac4jFactory("alloptions-pac4j.yaml");
        Config config = conf.build();

        assertThat(config).isExactlyInstanceOf(FakeConfig.class);
        assertThat(config.getClients().getClients()).hasSize(1);

        Client client0 = config.getClients().getClients().get(0);
        assertThat(client0).isExactlyInstanceOf(FacebookClient.class);
        assertThat(((FacebookClient) client0).getKey()).isEqualTo("fbId");

        assertThat(config.getAuthorizers().size()).isEqualTo(1);
        assertThat(config.getMatchers().size()).isEqualTo(1);
        Matcher matcher = config.getMatchers().values().iterator().next();
        assertThat(matcher).isInstanceOf(PathMatcher.class);
        assertThat(((PathMatcher) matcher).getExcludedPatterns().stream().map(Pattern::toString))
                .containsExactlyInAnyOrder("^/user/session$");
    }

    @Test
    public void defaultsUnset() throws Exception {
        Pac4jFactory conf = getPac4jFactory("defaults.yaml");
        Config config = conf.build();

        assertThat(conf.getConfigFactory()).isNull();
        assertThat(config).isNotNull();
        assertThat(config.getClients()).isNotNull();
        assertThat(config.getClients().getClients()).isEmpty();
    }
}
