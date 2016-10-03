package org.pac4j.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.After;
import org.junit.Test;
import org.pac4j.http.client.direct.DirectBasicAuthClient;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import io.dropwizard.Application;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;

public class EndToEndTest {
    private DropwizardTestSupport dropwizardTestSupport;
    private Client client = new JerseyClientBuilder().build();

    public void setup(
        Class<? extends Application<TestConfiguration>> applicationClass,
        ConfigOverride... configOverrides
    ) {
        dropwizardTestSupport = new DropwizardTestSupport<TestConfiguration>(
            applicationClass,
            ResourceHelpers.resourceFilePath("end-to-end-test.yaml"),
            configOverrides
        );
        dropwizardTestSupport.before();
    }

    private String getUrlPrefix() {
        return "http://localhost:" + dropwizardTestSupport.getLocalPort();
    }

    private String mkAuthField(String username, String password) {
        final String encodedBasicAuthCreds = BaseEncoding.base64()
            .encode(String.format("%s:%s", username, password).getBytes(Charsets.UTF_8));
        return String.format("Basic %s", encodedBasicAuthCreds);
    }

    @After
    public void tearDown() {
        dropwizardTestSupport.after();
        client.close();
    }

    @Test
    public void grantsAccessToResources() throws Exception {
        setup(
            TestApplication.class,
            ConfigOverride.config("pac4j.filters[0].clients", DirectBasicAuthClient.class.getSimpleName())
        );

        final String dogName = client.target(getUrlPrefix() + "/dogs/pierre")
            .request(MediaType.APPLICATION_JSON)
            // username == password
            .header(HttpHeaders.AUTHORIZATION, mkAuthField("rosebud", "rosebud"))
            .get(String.class);

        assertThat(dogName).isEqualTo("pierre");
    }

    @Test
    public void restrictsAccessToResources() throws Exception {
        setup(
            TestApplication.class,
            ConfigOverride.config("pac4j.filters[0].clients", DirectBasicAuthClient.class.getSimpleName())
        );

        final Response response = client.target(getUrlPrefix() + "/dogs/pierre")
            .request(MediaType.APPLICATION_JSON)
            // username != password
            .header(HttpHeaders.AUTHORIZATION, mkAuthField("boy", "howdy"))
            .get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.UNAUTHORIZED);
    }
}
