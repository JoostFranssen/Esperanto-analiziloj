package nl.sogyo.esperanto.persistence;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Reprezentas iom de la informoj eltiritaj el XML-dosiero de ReVo.
 * @author jfranssen
 *
 */
public class ReVoEntry implements IVortero {
	private String vortero;
	private VorterSpeco vorterSpeco;
	private Transitiveco transitiveco;
	
	public ReVoEntry() {
		this("", null);
	}
	public ReVoEntry(String vortero) {
		this(vortero, null);
	}
	public ReVoEntry(String vortero, VorterSpeco vorterSpeco) {
		this(vortero, vorterSpeco, Transitiveco.NEDIFINITA);
	}
	public ReVoEntry(String vortero, VorterSpeco vorterSpeco, Transitiveco transitiveco) {
		this.vortero = vortero;
		this.vorterSpeco = vorterSpeco;
		this.transitiveco = transitiveco;
	}
	
	public VorterSpeco getVorterSpeco() {
		return vorterSpeco;
	}
	void setVorterSpeco(VorterSpeco vorterSpeco) {
		this.vorterSpeco = vorterSpeco;
	}
	
	public Transitiveco getTransitiveco() {
		return transitiveco;
	}
	void setTransitiveco(Transitiveco transitiveco) {
		this.transitiveco = transitiveco;
	}
	
	public String getVortero() {
		return vortero;
	}
	void setVortero(String vortero) {
		this.vortero = vortero;
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
		if(!(obj instanceof IVortero)) {
			return false;
		}
		return IVortero.equals(this, (IVortero)obj);
	}
}
