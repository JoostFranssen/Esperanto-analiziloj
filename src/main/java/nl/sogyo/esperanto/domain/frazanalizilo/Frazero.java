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
	
	public Frazero() {
		this(null);
	}
	public Frazero(Funkcio funkcio, Vorto... vortoj) {
		this.funkcio = funkcio;
		this.vortoj = new ArrayList<>(Arrays.asList(vortoj));
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
	
	public void addVorto(Vorto vorto) {
		vortoj.add(vorto);
	}
	
	@Override
	public String toString() {
		return String.join(" ", vortoj.stream().map(v -> v.getVorto()).toArray(String[]::new));
	}
}
