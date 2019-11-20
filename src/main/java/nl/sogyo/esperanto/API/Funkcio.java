package nl.sogyo.esperanto.API;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;

/**
 * Reprezentas funkcion de frazero.
 * @author jfranssen
 *
 */
public enum Funkcio {
	ADVERBO,
	ĈEFVERBO,
	I_KOMPLEMENTO,
	KONJUNKCIO,
	OBJEKTO,
	PREDIKATIVO,
	PREPOZICIA_KOMPLEMENTO,
	PREPOZICIO,
	SUBJEKTO,
	TEMPO;
	
	private List<Funkcio> relatedFunkcioj;
	
	private Funkcio() {
		relatedFunkcioj = new ArrayList<>();
	}
	
	static {
		ADVERBO.relatedFunkcioj = asList(ADVERBO, ĈEFVERBO, I_KOMPLEMENTO);
		ĈEFVERBO.relatedFunkcioj = asList(SUBJEKTO, OBJEKTO);
		OBJEKTO.relatedFunkcioj = asList(ĈEFVERBO, I_KOMPLEMENTO);
		PREDIKATIVO.relatedFunkcioj = asList(ĈEFVERBO);
		SUBJEKTO.relatedFunkcioj = asList(ĈEFVERBO, I_KOMPLEMENTO);
	}
	
	public List<Funkcio> getRelatedFunkcioj() {
		return new ArrayList<>(relatedFunkcioj);
	}
}
