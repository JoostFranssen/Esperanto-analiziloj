package nl.sogyo.esperanto.servilo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import nl.sogyo.esperanto.domain.vortanalizilo.Analizaĵo;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * La alirejo por la vortanalizilo.
 * @author jfranssen
 *
 */
@Path("vortanalizo")
public class VortAnalizAlirejo {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response akiriVortAnalizon(@QueryParam("vorto") String vortĈeno) {
		Vorto vorto = new Vorto(vortĈeno);
		
		List<String> analizaĵoj = new ArrayList<>();
		
		for(Analizaĵo analizaĵo : vorto.getEblajAnalizaĵoj()) {
			analizaĵoj.add(analizaĵo.toString());
		}

		JSONObject respondaTeksto = new JSONObject();
		respondaTeksto.put("analizaĵoj", analizaĵoj);
		
		return Response.status(HttpStatus.OK_200).entity(respondaTeksto.toString()).build();
	}
}
