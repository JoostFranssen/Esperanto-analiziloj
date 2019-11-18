package nl.sogyo.esperanto.servilo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import nl.sogyo.esperanto.domain.frazanalizilo.Frazo;

/**
 * La alirejo por la frazanalizilo.
 * @author jfranssen
 *
 */
@Path("frazanalizo")
public class FrazAnalizilAccessPoint {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFrazAnalysis(@QueryParam("frazo") String string) {
		Frazo frazo = new Frazo(string);
		
		JSONObject responseJSON = FrazoJSONProcessor.convertFrazoToJSON(frazo);
		
		return Response.status(HttpStatus.OK_200).entity(responseJSON.toString()).build();
	}
}
