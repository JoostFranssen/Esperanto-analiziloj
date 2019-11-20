package nl.sogyo.esperanto.servilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import nl.sogyo.esperanto.domain.frazanalizilo.Frazero;
import nl.sogyo.esperanto.domain.frazanalizilo.Frazo;

/**
 * Kapablas konverti ĉiujn objektojn rilatajn al frazoj al JSON-objektoj.
 * @author jfranssen
 *
 */
public class FrazoJSONProcessor {
	private FrazoJSONProcessor() {}
	
	public static JSONObject convertFrazeroToJSON(Frazero frazero) {
		JSONObject json = new JSONObject();
		
		json.put("vortoj", frazero.getVortoj().stream().map(VortoJSONProcessor::convertVortoToJSON).toArray());
		json.put("funkcio", frazero.getFunkcio().toString());
		
		return json;
	}
	
	public static JSONObject convertFrazoToJSON(Frazo frazo) {
		JSONObject json = new JSONObject();
		
		json.put("vortoj", Arrays.asList(frazo.getVortoj()).stream().map(VortoJSONProcessor::convertVortoToJSON).toArray());
		
		/*
		 * Por trakti la reciprokaj referencoj inter la frazeroj, ni anstataŭigas ilin per indico en la listo de frazeroj.
		 * Tial ni unue devas asigni indicon al ĉiu frazero kaj poste anstataŭigi ilian referencon per la indico kaj Funkcio.
		 */
		
		JSONObject[] jsonFrazeroj = frazo.getFrazeroj().stream().map(FrazoJSONProcessor::convertFrazeroToJSON).toArray(JSONObject[]::new);
		
		Map<Object, Integer> map = new IdentityHashMap<>();
		for(int i = 0; i < jsonFrazeroj.length; i++) {
			jsonFrazeroj[i].put("index", i);
			map.put(frazo.getFrazeroj().get(i), i);
		}
		for(int i = 0; i < frazo.getFrazeroj().size(); i++) {
			Frazero frazero = frazo.getFrazeroj().get(i);
			List<JSONObject> jsonRelatedFrazeroj = new ArrayList<>();
			for(Frazero relatedFrazero : frazero.getRelatedFrazeroj()) {
				JSONObject jsonRelatedFrazero = new JSONObject();
				jsonRelatedFrazero.put("funkcio", relatedFrazero.getFunkcio().toString());
				jsonRelatedFrazero.put("index", map.get(relatedFrazero));
				jsonRelatedFrazeroj.add(jsonRelatedFrazero);
			}
			jsonFrazeroj[i].put("relatedFrazeroj", jsonRelatedFrazeroj);
		}
		
		json.put("frazeroj", jsonFrazeroj);
		
		return json;
	}
}