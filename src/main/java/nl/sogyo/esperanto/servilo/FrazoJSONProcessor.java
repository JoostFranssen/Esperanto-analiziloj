package nl.sogyo.esperanto.servilo;

import java.util.Arrays;

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
		json.put("frazeroj", frazo.getFrazeroj().stream().map(FrazoJSONProcessor::convertFrazeroToJSON).toArray());
		
		return json;
	}
}