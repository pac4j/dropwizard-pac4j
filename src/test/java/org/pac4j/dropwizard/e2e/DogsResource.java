package org.pac4j.dropwizard.e2e;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
