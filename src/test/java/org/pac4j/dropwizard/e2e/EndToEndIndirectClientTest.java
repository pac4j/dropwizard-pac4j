package org.pac4j.dropwizard.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.jupiter.api.Test;
import org.pac4j.dropwizard.AbstractApplicationTest;
import org.pac4j.dropwizard.factory.IndirectClientConfigFactory;

import io.dropwizard.core.setup.Environment;

public class EndToEndIndirectClientTest extends AbstractApplicationTest {

    public static class App extends TestApplication<TestConfiguration> {

        @Override
        public void run(TestConfiguration configuration,
                Environment environment) throws Exception {
            environment.jersey().register(new DogsResource());
        }
    }

    private Response getDog() {
        return client.target(getUrlPrefix() + "/dogs/pierre")
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.APPLICATION_JSON)
                .get();
    }

    @Test
    public void servletFilterRedirectsToIdentityProvider() throws Exception {
        setup(App.class, "end-to-end-indirect-servlet-test.yaml");

        final Response response = getDog();

        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.getHeaderString(HttpHeaders.LOCATION))
                .isEqualTo(IndirectClientConfigFactory.LOGIN_URL);
    }

    @Test
    public void jerseyFilterReturnsUnauthorized() throws Exception {
        setup(App.class, "end-to-end-indirect-jersey-test.yaml");

        final Response response = getDog();

        assertThat(response.getStatusInfo())
                .isEqualTo(Response.Status.UNAUTHORIZED);
    }

    private final Map<String, Cookie> cookies = new HashMap<>();

    private Invocation.Builder request(String path) {
        Invocation.Builder builder = client.target(getUrlPrefix() + path)
                .property(ClientProperties.FOLLOW_REDIRECTS, false)
                .request(MediaType.APPLICATION_JSON);
        for (Cookie cookie : cookies.values()) {
            builder = builder.cookie(cookie);
        }
        return builder;
    }

    private Response keepCookies(Response response) {
        response.getCookies().forEach((name, cookie) -> cookies.put(name, cookie.toCookie()));
        return response;
    }

    @Test
    public void servletLoginAndLogout() throws Exception {
        setup(App.class, "end-to-end-servlet-login-logout-test.yaml");

        // not authenticated: redirection to the login page
        Response response = keepCookies(request("/dogs/pierre").get());
        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.getHeaderString(HttpHeaders.LOCATION))
                .isEqualTo(IndirectClientConfigFactory.LOGIN_URL);

        // login on the callback endpoint without client name: the default client is used
        Form form = new Form();
        form.param("username", "rosebud");
        form.param("password", "rosebud");
        response = keepCookies(request("/callback")
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE)));
        // pac4j uses a 303 (See Other) redirection after a POST request
        assertThat(response.getStatus()).isEqualTo(303);
        assertThat(response.getHeaderString(HttpHeaders.LOCATION))
                .isEqualTo(getUrlPrefix() + "/dogs/pierre");

        // authenticated: access granted
        response = keepCookies(request("/dogs/pierre").get());
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("pierre");

        // logout: redirection to the default url
        response = keepCookies(request("/logout").get());
        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.getHeaderString(HttpHeaders.LOCATION))
                .isEqualTo("http://localhost/bye");

        // logged out: redirection to the login page again
        response = keepCookies(request("/dogs/pierre").get());
        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(response.getHeaderString(HttpHeaders.LOCATION))
                .isEqualTo(IndirectClientConfigFactory.LOGIN_URL);
    }
}
