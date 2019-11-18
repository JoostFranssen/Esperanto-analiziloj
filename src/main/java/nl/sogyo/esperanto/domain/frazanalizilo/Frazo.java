package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Analizaĵo;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Reprezentas frazon/
 * @author jfranssen
 *
 */
public class Frazo {
	private Vorto[] vortoj;
	private List<Frazero> frazeroj;
	
	public Frazo(String frazo) {
		String[] vortoStrings = frazo.replaceAll("[,\\.!?]", "").split("\\s");
		
		vortoj = new Vorto[vortoStrings.length];
		IntStream.range(0, vortoStrings.length).parallel().forEach(i -> {
			vortoj[i] = new Vorto(vortoStrings[i]);
		});
		
		analyze();
	}
	
	/**
	 * Analizas la frazon kaj inicias la liston de {@code Frazero}j
	 */
	private void analyze() {
		frazeroj = new ArrayList<>();
		
		Frazero ĉefverbo = findĈefverbo();
		if(ĉefverbo != null) {
			frazeroj.add(ĉefverbo);
		}
	}
	
	/**
	 * Trovas la unuan ĉefverbon en la frazo; t.e., la unua finitiva verbo
	 * @return
	 */
	public Frazero findĈefverbo() {
		for(Vorto vorto : vortoj) {
			Analizaĵo analizaĵo = vorto.getFirstAnalizaĵo();
			if(analizaĵo.checkTrajto(Trajto.VERBO) && !analizaĵo.checkTrajto(Trajto.VERBO_INFINITIVO)) {
				return new Frazero(Funkcio.ĈEFVERBO, vorto);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.join(" ", Arrays.asList(vortoj).stream().map(v -> v.toString()).toArray(String[]::new));
	}
	
	public Vorto[] getVortoj() {
		return vortoj.clone();
	}
	
	public List<Frazero> getFrazeroj() {
		return new ArrayList<>(frazeroj);
	}
}
