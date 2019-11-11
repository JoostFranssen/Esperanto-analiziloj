package nl.sogyo.esperanto.servilo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

@Path("msg")
public class Mesaĝo {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMesaĝo() {
		return Response.status(HttpStatus.OK_200).entity("Testmesaĝo").build();
	}
}
