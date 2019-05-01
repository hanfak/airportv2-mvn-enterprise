package learning.restfulserver.example1.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class TestFourResource {
  @GET
  @Path("/{id}/query") //http://localhost:8080/hello/3/query?city=blah
  @Produces(MediaType.TEXT_PLAIN)
  public Response test(@PathParam("id") long id, @QueryParam("city") String city) {
    return Response.status(Response.Status.OK)
            .entity("Hello " + id + " Query param: " + city)
            .header("blah", 1234)
            .build();
  }
}