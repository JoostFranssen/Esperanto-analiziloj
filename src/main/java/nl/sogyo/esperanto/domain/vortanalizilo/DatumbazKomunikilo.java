package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.persistence.Datumbazo;

/**
 * La peranto inter la domajno kaj la datumbazo, por ke la domajna logiko estas pli disa de la datumbazo.
 * @author jfranssen
 *
 */
public class DatumbazKomunikilo {
	private static Datumbazo datumbazo = Datumbazo.getReVoDatumbazo();
	
	/**
	 * Kontrolas la datumbazon je {@code vortero}.
	 * @param vortero
	 * @return liston da vorteroj, kiuj kongruas kun la donita ĉeno {@code vortero}
	 */
	public static List<Vortero> preniElDatumbazo(String vortero) {
		return preniElDatumbazo(vortero, v -> true);
	}
	/**
	 * Kontrolas la datumbazon je {@code vortero} kaj aplikas la {@code filtrilo} antaŭ ol redoni la rezulton.
	 * @param vortero
	 * @param filtrilo {@code Predicate<IVortero>} por filtri la rezulton laŭ propraĵoj de la redonitaj vorteroj.
	 * @return liston da vorteroj, kiuj ongruas kun la donita ĉeno {@code vortero} kaj trapasas {@code filtrilo}n
	 */
	public static List<Vortero> preniElDatumbazo(String vortero, Predicate<IVortero> filtrilo) {
		List<Vortero> vorteroj = new ArrayList<>();
		for(IVortero v : datumbazo.preniElDatumbazo(vortero)) {
			if(filtrilo.test(v)) {
				vorteroj.add(new Vortero(v));
			}
		}
		return vorteroj;
	}
}
