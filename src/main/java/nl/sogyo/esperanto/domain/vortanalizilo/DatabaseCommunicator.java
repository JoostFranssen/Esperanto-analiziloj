package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.persistence.Database;

/**
 * La peranto inter la domajno kaj la datumbazo, por ke la domajna logiko estas pli disa de la datumbazo.
 * @author jfranssen
 *
 */
public class DatabaseCommunicator {
	private static Database database = Database.getReVoDatabase();
	
	/**
	 * Kontrolas la datumbazon je {@code vortero}.
	 * @param vortero
	 * @return liston da vorteroj, kiuj kongruas kun la donita ĉeno {@code vortero}
	 */
	public static List<Vortero> getFromDatabase(String vortero) {
		return getFromDatabase(vortero, v -> true);
	}
	/**
	 * Kontrolas la datumbazon je {@code vortero} kaj aplikas la {@code filtrilo} antaŭ ol redoni la rezulton.
	 * @param vortero
	 * @param condition {@code Predicate<IVortero>} por filtri la rezulton laŭ propraĵoj de la redonitaj vorteroj.
	 * @return liston da vorteroj, kiuj ongruas kun la donita ĉeno {@code vortero} kaj trapasas {@code filtrilo}n
	 */
	public static List<Vortero> getFromDatabase(String vortero, Predicate<IVortero> condition) {
		List<Vortero> vorteroj = new ArrayList<>();
		for(IVortero v : database.getFromDatabase(vortero)) {
			if(condition.test(v)) {
				vorteroj.add(new Vortero(v));
			}
		}
		return vorteroj;
	}
}
