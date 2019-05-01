package learning.restfulserver.example1.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//https://stackoverflow.com/questions/4687271/jax-rs-how-to-return-json-and-http-status-code-together
@Path("/hello")
public class TestThreeResource {
  @GET
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response test(@PathParam("id") long id) {
    return Response.status(Response.Status.OK)
            .entity("Hello " + id)
            .header("blah", 1234)
            .build();
  }
}