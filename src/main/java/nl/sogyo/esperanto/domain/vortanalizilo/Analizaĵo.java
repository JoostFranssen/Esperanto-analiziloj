package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.List;

import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Unu el la eblaj rezultoj de analizado de vorto. Ĉi tio reprezentas eblan disigon de vorto en siaj plej etaj partoj (krom literoj). Ekzemple, {@code Analizaĵo} de 'scii' entenas vorteroj 'sci' kaj 'i'.
 * Analizaĵo mem povas determini diversajn {@code Trajto}jn pri si mem.
 * @author jfranssen
 *
 */
public class Analizaĵo {
	private List<Vortero> vorteroj;
	
	public Analizaĵo() {
		this(new ArrayList<>());
	}
	public Analizaĵo(List<Vortero> vorteroj) {
		this.vorteroj = vorteroj;
	}
	/**
	 * Kreas profundan kopion de la donita {@code Analizaĵo}-objekto.
	 * @param analizaĵo
	 */
	public Analizaĵo(Analizaĵo analizaĵo) {
		vorteroj = new ArrayList<>(analizaĵo.vorteroj);
		vorteroj.replaceAll(v -> new Vortero(v));
	}
	
	/**
	 * Kontrolas, ĉu la analizaĵo estas valida aŭ senchava. Malplena analizaĵo estas valida.
	 * @return ĉu la analizaĵo estas valida aŭ ne.
	 */
	public boolean estasValida() {
		if(!interjekcioAperasNurSola()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Kontrolas, ĉu interjekcio aperas sola. Tio estas, interjekcio ne estu parto de pli granda vorto.
	 * @return ĉu, se unu el la vorteroj estas interjekcio, tiam ĝi estu solas.
	 */
	private boolean interjekcioAperasNurSola() {
		if(vorteroj.size() > 1) {
			for(Vortero vortero : vorteroj) {
				if(vortero.getVorterSpeco() == VorterSpeco.INTERJEKCIO) {
					return false;
				}
			}
		}
		return true;
	}
	
	public List<Vortero> getVorteroj() {
		return new ArrayList<>(vorteroj);
	}
	
	protected void aldoniVorteron(Vortero vortero) {
		vorteroj.add(vortero);
	}
	protected void aldoniVorteron(int indico, Vortero vortero) {
		vorteroj.add(indico, vortero);
	}
	
	protected Vortero preniVorteron(int indico) {
		return vorteroj.get(indico);
	}
	
	protected void forigiVorteron(int indico) {
		vorteroj.remove(indico);
	}
	protected void forigiVorteron(Vortero vortero) {
		vorteroj.remove(vortero);
	}
	
	/**
	 * Tuj kontralas, ĉu la {@code Analizaĵo} havas la {@code Trajto}n.
	 * @param trajto
	 * @return ĉu ĝi havas la {@code Trajto}n.
	 */
	public boolean kontroliTrajton(Trajto trajto) {
		//TODO realigi trajtokontroladon laŭ la konsistigaj vorteroj
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vorteroj == null) ? 0 : vorteroj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		Analizaĵo other = (Analizaĵo)obj;
		if(vorteroj == null) {
			if(other.vorteroj != null) {
				return false;
			}
		} else if(!vorteroj.equals(other.vorteroj)) {
			return false;
		}
		return true;
	}
}
