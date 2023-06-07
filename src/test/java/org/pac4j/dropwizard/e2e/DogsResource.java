package org.pac4j.dropwizard.e2e;

import java.util.Optional;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dogs")
@Produces(MediaType.APPLICATION_JSON)
public class DogsResource {
    @GET
    @Path("/{name}")
    public Optional<String> getDog(@PathParam("name") String name) {
        return Optional.of(name);
    }

    @POST
    @Path("/{name}")
    public Optional<String> getDogPost(@PathParam("name") String name) {
        return Optional.of(name);
    }
}
