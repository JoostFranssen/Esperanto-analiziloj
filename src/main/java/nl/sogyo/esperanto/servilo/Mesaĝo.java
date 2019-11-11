package nl.sogyo.esperanto.servilo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

@Path("msg")
public class Mesaĝo {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMesaĝo() {
		JSONObject json = new JSONObject();
		json.put("s", "testmesaĝo");
		
		return Response.status(HttpStatus.OK_200).entity(json.toString()).build();
	}
}
