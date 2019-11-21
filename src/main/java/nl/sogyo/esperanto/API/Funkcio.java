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
	PREPOZICIAĴO,
	SUBJEKTO,
	TEMPO;
	
	private List<Funkcio> relatedFunkcioj;
	
	private Funkcio() {
		relatedFunkcioj = new ArrayList<>();
	}
	
	static {
		ADVERBO.relatedFunkcioj = asList(ADVERBO, ĈEFVERBO, I_KOMPLEMENTO);
		ĈEFVERBO.relatedFunkcioj = asList(SUBJEKTO, OBJEKTO, PREDIKATIVO);
		OBJEKTO.relatedFunkcioj = asList(ĈEFVERBO, I_KOMPLEMENTO);
		PREDIKATIVO.relatedFunkcioj = asList(ĈEFVERBO, I_KOMPLEMENTO);
		SUBJEKTO.relatedFunkcioj = asList(ĈEFVERBO, I_KOMPLEMENTO);
		I_KOMPLEMENTO.relatedFunkcioj = asList(SUBJEKTO, OBJEKTO, PREDIKATIVO);
		PREPOZICIAĴO.relatedFunkcioj = asList(I_KOMPLEMENTO);
	}
	
	public List<Funkcio> getRelatedFunkcioj() {
		return new ArrayList<>(relatedFunkcioj);
	}
}
