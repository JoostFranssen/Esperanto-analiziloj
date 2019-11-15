package nl.sogyo.esperanto.API;

/**
 * Reprezento de vortero kun speco kaj transitiveco.
 * @author jfranssen
 *
 */
public interface IVortero {
	public String getVortero();
	public VorterSpeco getVorterSpeco();
	public Transitiveco getTransitiveco();
	
	public static boolean equals(IVortero v1, IVortero v2) {
		if(v1 == v2) {
			return true;
		}
		if(v1 == null || v2 == null) {
			return false;
		}
		if(v1.getVortero() == null ^ v2.getVortero() == null) {
			return false;
		}
		if(!v1.getVortero().equals(v2.getVortero())) {
			return false;
		}
		if(v1.getVorterSpeco() != v2.getVorterSpeco()) {
			return false;
		}
		if(v1.getTransitiveco() != v2.getTransitiveco()) {
			return false;
		}
		return true;
	}
}
