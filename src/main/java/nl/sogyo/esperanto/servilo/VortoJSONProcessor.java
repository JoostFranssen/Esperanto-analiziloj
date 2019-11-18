package nl.sogyo.esperanto.servilo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Analizaĵo;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Kapablas konverti ĉiujn objektojn rilatajn al vortoj al JSON-objektoj.
 * @author jfranssen
 *
 */
public class VortoJSONProcessor {
	private VortoJSONProcessor() {}
	
	public static JSONObject convertVorteroToJSON(IVortero vortero) {
		JSONObject json = new JSONObject();
		
		json.put("vortero", vortero.getVortero());
		json.put("vorterSpeco", vortero.getVorterSpeco().toString());
		json.put("Transitiveco", vortero.getTransitiveco().toString());
		
		return json;
	}
	
	public static JSONObject convertAnalizaĵoToJSON(Analizaĵo analizaĵo) {
		JSONObject json = new JSONObject();
		
		json.put("vorteroj", analizaĵo.getVorteroj().stream().map(VortoJSONProcessor::convertVorteroToJSON).toArray());
		
		List<Trajto> trajtoj = new ArrayList<>();
		for(Trajto trajto : Trajto.values()) {
			if(analizaĵo.checkTrajto(trajto)) {
				trajtoj.add(trajto);
			}
		}
		
		json.put("trajtoj", trajtoj.stream().map(Trajto::toString).toArray(String[]::new));
		
		return json;
	}
	
	public static JSONObject convertVortoToJSON(Vorto vorto) {
		JSONObject json = new JSONObject();
		
		json.put("vorto", vorto.getVorto());
		
		json.put("analizaĵoj", vorto.getPossibleAnalizaĵojSorted().stream().map(VortoJSONProcessor::convertAnalizaĵoToJSON).toArray());
		
		return json;
	}
}
