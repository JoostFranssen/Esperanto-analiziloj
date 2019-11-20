package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Reprezentas parton de frazo kun unu specifa funkcio (ekzemple subjekto aŭ ĉefverbo).
 * @author jfranssen
 *
 */
public class Frazero {
	private List<Vorto> vortoj;
	private Funkcio funkcio;
	private List<Frazero> relatedFrazeroj;
	
	public Frazero() {
		this(null);
	}
	public Frazero(Funkcio funkcio, Vorto... vortoj) {
		this.funkcio = funkcio;
		this.vortoj = new ArrayList<>(Arrays.asList(vortoj));
		relatedFrazeroj = new ArrayList<>();
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
	
	public List<Frazero> getRelatedFrazeroj() {
		return new ArrayList<>(relatedFrazeroj);
	}
	
	void addRelatedFrazeroj(Frazero... frazeroj) {
		relatedFrazeroj.addAll(Arrays.asList(frazeroj));
	}
	
	@Override
	public String toString() {
		return String.join(" ", vortoj.stream().map(v -> v.getVorto()).toArray(String[]::new));
	}
}
