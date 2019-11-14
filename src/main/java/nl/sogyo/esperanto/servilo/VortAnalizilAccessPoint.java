package nl.sogyo.esperanto.servilo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * La alirejo por la vortanalizilo.
 * @author jfranssen
 *
 */
@Path("vortanalizo")
public class VortAnalizilAccessPoint {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVortAnalysis(@QueryParam("vorto") String string) {
		Vorto vorto = new Vorto(string);
		
		JSONObject responseJSON = VortoJSONProcessor.convertVortoToJSON(vorto);
		
		return Response.status(HttpStatus.OK_200).entity(responseJSON.toString()).build();
	}
}