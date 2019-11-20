package nl.sogyo.esperanto.servilo;

import java.util.Arrays;

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
		try {
			Frazo frazo = new Frazo(string);
			
			if(Arrays.asList(frazo.getVortoj()).stream().allMatch(v -> v.getPossibleAnalizaĵoj().isEmpty())) {
				return Response.status(HttpStatus.BAD_REQUEST_400).build();
			}
			
			JSONObject responseJSON = FrazoJSONProcessor.convertFrazoToJSON(frazo);
			
			return Response.status(HttpStatus.OK_200).entity(responseJSON.toString()).build();
		} catch(Exception e) {
			e.printStackTrace();
			
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
	}
}
