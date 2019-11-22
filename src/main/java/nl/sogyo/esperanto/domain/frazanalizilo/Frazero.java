package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Reprezentas parton de frazo kun unu specifa funkcio (ekzemple subjekto aŭ ĉefverbo).
 * @author jfranssen
 *
 */
public class Frazero {
	private List<Vorto> vortoj;
	private Funkcio funkcio;
	private Map<Funkcio, Frazero> relatedFrazeroj;
	
	public Frazero() {
		this(null);
	}
	public Frazero(Funkcio funkcio, Vorto... vortoj) {
		this.funkcio = funkcio;
		this.vortoj = new ArrayList<>(Arrays.asList(vortoj));
		relatedFrazeroj = new HashMap<>();
	}

	public List<Vorto> getVortoj() {
		return new ArrayList<>(vortoj);
	}

	public Funkcio getFunkcio() {
		return funkcio;
	}
	
	void setFunkcio(Funkcio funkcio) {
		this.funkcio = funkcio;
	}
	
	void addVorto(Vorto vorto) {
		vortoj.add(vorto);
	}
	
	public Map<Funkcio, Frazero> getRelatedFrazeroj() {
		return new HashMap<>(relatedFrazeroj);
	}
	
	void setRelatedFrazeroWithFunkcio(Funkcio funkcio, Frazero frazero) {
		relatedFrazeroj.put(funkcio, frazero);
	}
	
	public Frazero getRelatedFrazero(Funkcio funkcio) {
		return relatedFrazeroj.get(funkcio);
	}
	
	public boolean hasRelatedFunkcio(Funkcio funkcio) {
		return relatedFrazeroj.containsKey(funkcio);
	}
	
	public boolean hasRelatedVerbaFunkcio() {
		return relatedFrazeroj.containsKey(Funkcio.ĈEFVERBO) || relatedFrazeroj.containsKey(Funkcio.I_KOMPLEMENTO);
	}
	
	/**
	 * Prenas la lastan vorton de la donita {@code Frazero} tiel, ke kelkaj vortoj estas ignorataj (specife: ĉi kaj ajn en kelkaj kazoj).
	 * @return la lastan vorton de la donita frazero kun kelkaj transsaltoj, bezonataj por la ĝusta funkcio.
	 */
	public Vorto getLastVortoWithSkip() {
		Vorto lastVorto = vortoj.get(vortoj.size() - 1);
		
		if(lastVorto.equals(Vorto.AJN) || lastVorto.equals(Vorto.ĈI)) {
			if(vortoj.size() >= 2) {
				lastVorto = vortoj.get(vortoj.size() - 2);
			}
		}
		return lastVorto;
	}
	
	public boolean hasAkuzativo() {
		return getLastVortoWithSkip().checkTrajto(Trajto.AKUZATIVO);
	}
	
	public boolean isNumeralo() {
		return vortoj.stream().allMatch(v -> v.checkTrajto(Trajto.NUMERALO));
	}
	
	@Override
	public String toString() {
		return String.join(" ", vortoj.stream().map(v -> v.getVorto()).toArray(String[]::new));
	}
}
