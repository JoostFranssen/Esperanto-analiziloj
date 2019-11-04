package nl.sogyo.esperanto.persistence;

import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

public class ReVoEnigo {
	private String vortero;
	private VorterSpeco vorterSpeco;
	private Transitiveco transitiveco;
	
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
		return vortero + " " + "vorterSpeco" + (transitiveco != null ? " " + transitiveco : "");
	}
}
