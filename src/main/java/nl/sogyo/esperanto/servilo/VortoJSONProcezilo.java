package nl.sogyo.esperanto.servilo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Analizaĵo;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

public class VortoJSONProcezilo {
	public static JSONObject konvertiVorteronAlJSON(IVortero vortero) {
		JSONObject json = new JSONObject();
		
		json.put("vortero", vortero.getVortero());
		json.put("vorterSpeco", vortero.getVorterSpeco().toString());
		json.put("Transitiveco", vortero.getTransitiveco().toString());
		
		return json;
	}
	
	public static JSONObject konvertiAnalizaĵonAlJSON(Analizaĵo analizaĵo) {
		JSONObject json = new JSONObject();
		
		json.put("vorteroj", analizaĵo.getVorteroj().stream().map(VortoJSONProcezilo::konvertiVorteronAlJSON).toArray());
		
		List<Trajto> trajtoj = new ArrayList<>();
		for(Trajto trajto : Trajto.values()) {
			if(analizaĵo.kontroliTrajton(trajto)) {
				trajtoj.add(trajto);
			}
		}
		
		json.put("trajtoj", trajtoj.stream().map(Trajto::toString).toArray(String[]::new));
		
		return json;
	}
	
	public static JSONObject konvertiVortonAlJSON(Vorto vorto) {
		JSONObject json = new JSONObject();
		
		json.put("vorto", vorto.getVorto());
		
		json.put("analizaĵoj", vorto.getEblajAnalizaĵojSortitaj().stream().map(VortoJSONProcezilo::konvertiAnalizaĵonAlJSON).toArray());
		
		return json;
	}
}
