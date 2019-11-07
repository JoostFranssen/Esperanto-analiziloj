package nl.sogyo.esperanto.domain.vortanalizilo;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Vortero de {@code Analizaĵo}.
 * @author jfranssen
 *
 */
public class Vortero implements IVortero {
	private String vortero;
	private VorterSpeco vorterSpeco;
	private Transitiveco transitiveco;
	
	public Vortero(IVortero enigo) {
		this(enigo.getVortero(), enigo.getVorterSpeco(), enigo.getTransitiveco());
	}
	public Vortero(String vortero, VorterSpeco vorterSpeco, Transitiveco transitiveco) {
		this.vortero = vortero;
		this.vorterSpeco = vorterSpeco;
		this.transitiveco = transitiveco;
	}
	
	public String getVortero() {
		return vortero;
	}
	
	public VorterSpeco getVorterSpeco() {
		return vorterSpeco;
	}
	
	public Transitiveco getTransitiveco() {
		return transitiveco;
	}
	
	@Override
	public String toString() {
		return getVortero() + (getVorterSpeco() != null ? " " + getVorterSpeco() : "") + (getTransitiveco() != null ? " " + getTransitiveco() : "");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transitiveco == null) ? 0 : transitiveco.hashCode());
		result = prime * result + ((vorterSpeco == null) ? 0 : vorterSpeco.hashCode());
		result = prime * result + ((vortero == null) ? 0 : vortero.hashCode());
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
		Vortero other = (Vortero)obj;
		if(transitiveco != other.transitiveco) {
			return false;
		}
		if(vorterSpeco != other.vorterSpeco) {
			return false;
		}
		if(vortero == null) {
			if(other.vortero != null) {
				return false;
			}
		} else if(!vortero.equals(other.vortero)) {
			return false;
		}
		return true;
	}
	
	
}
