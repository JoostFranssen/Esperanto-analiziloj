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
	public static final Vortero O_FINAĴO = new Vortero("o", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero A_FINAĴO = new Vortero("a", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero E_FINAĴO = new Vortero("e", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero J_FINAĴO = new Vortero("j", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero N_FINAĴO = new Vortero("n", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero I_FINAĴO = new Vortero("i", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero AS_FINAĴO = new Vortero("as", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero IS_FINAĴO = new Vortero("is", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero OS_FINAĴO = new Vortero("os", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero US_FINAĴO = new Vortero("us", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero U_FINAĴO = new Vortero("u", VorterSpeco.FINAĴO, Transitiveco.NEDIFINITA);
	public static final Vortero AKTIVA_FINITA_PARTICIPA_SUFIKSO = new Vortero("int", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero AKTIVA_DAŬRA_PARTICIPA_SUFIKSO = new Vortero("ant", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero AKTIVA_ESTONTA_PARTICIPA_SUFIKSO = new Vortero("ont", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero PASIVA_FINITA_PARTICIPA_SUFIKSO = new Vortero("it", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero PASIVA_DAŬRA_PARTICIPA_SUFIKSO = new Vortero("at", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero PASIVA_ESTONTA_PARTICIPA_SUFIKSO = new Vortero("ot", VorterSpeco.SUFIKSO, Transitiveco.NEDIFINITA);
	public static final Vortero IG_SUFIKSO = new Vortero("ig", VorterSpeco.SUFIKSO, Transitiveco.TRANSITIVA);
	public static final Vortero IĜ_SUFIKSO = new Vortero("iĝ", VorterSpeco.SUFIKSO, Transitiveco.NETRANSITIVA);
	public static final Vortero SI_PRONOMO = new Vortero("si", VorterSpeco.PRONOMO, Transitiveco.NEDIFINITA);
	
	private String vortero;
	private VorterSpeco vorterSpeco;
	private Transitiveco transitiveco;
	
	public Vortero(IVortero entry) {
		this(entry.getVortero(), entry.getVorterSpeco(), entry.getTransitiveco());
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
