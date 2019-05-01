package learning.restfulserver.example1.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/blah")
public class TestTwoResource {
  @GET
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public long test(@PathParam("id") long id) {
    return id;
  }
}