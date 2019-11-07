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
}
