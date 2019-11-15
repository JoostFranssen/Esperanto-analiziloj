package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

public class Frazero {
	private List<Vorto> vortoj;
	private Funkcio funkcio;
	
	public Frazero(Funkcio funkcio, Vorto... vortoj) {
		this.funkcio = funkcio;
		this.vortoj = Arrays.asList(vortoj);
	}

	public List<Vorto> getVortoj() {
		return new ArrayList<>(vortoj);
	}

	public Funkcio getFunkcio() {
		return funkcio;
	}
}
