package nl.sogyo.esperanto.persistence;

import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoEnigo {
	private String vortero;
	private VorterSpeco vorterSpeco;
	private Transitiveco transitiveco;
	
	public ReVoEnigo() {
		this("", null);
	}
	public ReVoEnigo(String vortero) {
		this(vortero, null);
	}
	public ReVoEnigo(String vortero, VorterSpeco vorterSpeco) {
		this(vortero, vorterSpeco, Transitiveco.NEDIFINITA);
	}
	public ReVoEnigo(String vortero, VorterSpeco vorterSpeco, Transitiveco transitiveco) {
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
		return vortero + (vorterSpeco != null ? " " + vorterSpeco : "") + (transitiveco != null ? " " + transitiveco : "");
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
		ReVoEnigo other = (ReVoEnigo)obj;
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
