package org.pac4j.dropwizard.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.pac4j.dropwizard.AbstractApplicationTest;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.DirectFormClient;

import io.dropwizard.core.setup.Environment;
import io.dropwizard.testing.ConfigOverride;

public class EndToEndTest extends AbstractApplicationTest {

    public static class App extends TestApplication<TestConfiguration> {

        @Override
        public void run(TestConfiguration configuration,
                Environment environment) throws Exception {
            environment.jersey().register(new DogsResource());
        }
    }

    private void setup(ConfigOverride config) throws Exception {
        super.setup(App.class, "end-to-end-test.yaml", config);
    }

    @Test
    public void grantsAccessToResourcesForm() throws Exception {
        setup(ConfigOverride.config("pac4j.globalFilters[0].clients",
                DirectFormClient.class.getSimpleName()));

        // username == password
        Form form = new Form();
        form.param("username", "rosebud");
        form.param("password", "rosebud");
        final String dogName = client.target(getUrlPrefix() + "/dogs/pierre")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form,
                        MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                        String.class);

        assertThat(dogName).isEqualTo("pierre");
    }

    @Test
    public void grantsAccessToResources() throws Exception {
        setup(ConfigOverride.config("pac4j.globalFilters[0].clients",
                DirectBasicAuthClient.class.getSimpleName()));

        final String dogName = client.target(getUrlPrefix() + "/dogs/pierre")
                .request(MediaType.APPLICATION_JSON)
                // username == password
                .header(HttpHeaders.AUTHORIZATION,
                        mkAuthField("rosebud", "rosebud"))
                .get(String.class);

        assertThat(dogName).isEqualTo("pierre");
    }

    @Test
    public void restrictsAccessToResources() throws Exception {
        setup(ConfigOverride.config("pac4j.globalFilters[0].clients",
                DirectBasicAuthClient.class.getSimpleName()));

        final Response response = client.target(getUrlPrefix() + "/dogs/pierre")
                .request(MediaType.APPLICATION_JSON)
                // username != password
                .header(HttpHeaders.AUTHORIZATION, mkAuthField("boy", "howdy"))
                .get();

        assertThat(response.getStatusInfo())
                .isEqualTo(Response.Status.UNAUTHORIZED);
    }
}
