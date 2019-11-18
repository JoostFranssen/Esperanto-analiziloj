package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Analizita vorto.
 * @author jfranssen
 *
 */
public class Vorto {
	private String vorto;
	private Set<Analizaĵo> possibleAnalizaĵoj;
	private List<Analizaĵo> sortedAnalizaĵoj;
	
	public static Predicate<IVortero> defaultFilterForDatabase = v -> {
		return v.getVorterSpeco() != VorterSpeco.LITERO && !v.equals(Vortero.J_SUFIKSO) && !v.equals(Vortero.ĈJ_SUFIKSO) && !v.equals(Vortero.NJ_SUFIKSO);
	};

	/**
	 * Iniciatas novan vorton, analizas ĝin por trovi {@code Analizaĵo}jn, kiuj
	 * poste estas filtritaj laŭ ĝia valideco.
	 * 
	 * @param vorto
	 */
	public Vorto(String vorto) {
		this(vorto, true);
	}

	public Vorto(String vorto, boolean filterInvalidAnalizaĵoj) {
		this.vorto = vorto;
		possibleAnalizaĵoj = new HashSet<>();

		analyze();
		if(filterInvalidAnalizaĵoj) {
			filterInvalidAnalizaĵoj();
		}
	}

	/**
	 * Iniciatas la aron da {@code Analizaĵo}j.
	 */
	private void analyze() {
		if(vorto.isEmpty()) {
			possibleAnalizaĵoj.add(new Analizaĵo());
		}

		for(int i = vorto.length() - 1; i >= 0; i--) {
			String takenPart = vorto.substring(i);
			List<Vortero> result = DatabaseCommunicator.getFromDatabase(takenPart, defaultFilterForDatabase);
			if(!result.isEmpty()) {
				Vorto subVorto = new Vorto(vorto.substring(0, i), false);
				subVorto.analyze();
				for(Analizaĵo analizaĵo : subVorto.possibleAnalizaĵoj) {
					for(Vortero v : result) {
						Analizaĵo newAnalizaĵo = new Analizaĵo(analizaĵo);
						newAnalizaĵo.addVortero(v);
						possibleAnalizaĵoj.add(newAnalizaĵo);
					}
				}
			}
		}
	}

	/**
	 * Filtras la {@code Analizaĵo}jn laŭ valideco.
	 */
	private void filterInvalidAnalizaĵoj() {
		possibleAnalizaĵoj.removeIf(a -> !a.isValid());
	}

	public String getVorto() {
		return vorto;
	}

	public Set<Analizaĵo> getPossibleAnalizaĵoj() {
		return new HashSet<>(possibleAnalizaĵoj);
	}

	public List<Analizaĵo> getPossibleAnalizaĵojSorted() {
		if(sortedAnalizaĵoj == null) {
			sortedAnalizaĵoj = new ArrayList<>(possibleAnalizaĵoj);
			Collections.sort(sortedAnalizaĵoj, Analizaĵo.getComparator());
		}
		return sortedAnalizaĵoj;
	}
	
	public Analizaĵo getFirstAnalizaĵo() {
		if(!possibleAnalizaĵoj.isEmpty()) {
			return getPossibleAnalizaĵojSorted().get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Kontrolas, ĉu la finaĵoj kongruas kun la unua analizaĵo.
	 * @param finaĵoj
	 * @return ĉu la finaĵoj kongruas
	 */
	public boolean matchFinaĵoj(List<Vortero> finaĵoj) {
		return getFirstAnalizaĵo() != null && getFirstAnalizaĵo().matchFinaĵoj(finaĵoj);
	}
	
	/**
	 * Kontrolas, ĉu la finaĵoj kongruas kun la finaĵoj de ĉiuj donitaj vortoj.
	 * @param vortoj
	 * @return ĉu la finaĵoj kongruas unuj kun la aliaj
	 */
	public boolean matchFinaĵojOf(Vorto... vortoj) {
		return getFirstAnalizaĵo() != null && getFirstAnalizaĵo().matchFinaĵojOf(Arrays.asList(vortoj).stream().map(v -> v.getFirstAnalizaĵo()).toArray(Analizaĵo[]::new));
	}
	
	public boolean checkTrajto(Trajto trajto) {
		return getFirstAnalizaĵo() != null && getFirstAnalizaĵo().checkTrajto(trajto);
	}
	
	/**
	 * Kontrolas, ĉu unu el la vortoj en la listo havas la specifitan trajton.
	 * @param vortoj
	 * @param trajto
	 * @return ĉu unu el la vortoj havas tiun trajton
	 */
	public static boolean checkTrajtoForAny(List<Vorto> vortoj, Trajto trajto) {
		return vortoj.stream().anyMatch(v -> v.checkTrajto(trajto));
	}
	
	/**
	 * Kontrolas, ĉu ĉiu vorto en la listo havas la specifitan trajton.
	 * @param vortoj
	 * @param trajto
	 * @return ĉu ĉiu vorto havas tiun trajton
	 */
	public static boolean checkTrajtoForAll(List<Vorto> vortoj, Trajto trajto) {
		return vortoj.stream().allMatch(v -> v.checkTrajto(trajto));
	}
	
	/**
	 * Prenas unu el la eblaj {@code Analizaĵo}j laŭ la donita ŝablono. Redonas
	 * {@code null}, se neniu estas trovata.
	 * 
	 * @param string {@code String}-objekto, kiu reprezentas la vort-dividon de la
	 *			   dezirata {@code Analizaĵo}. Vertikala streko funkciu por la
	 *			   divido; ekzemple: "esper|ant|o"
	 * @return unu el la eblaj {@code Analizaĵo}j, kiu kongruas kun
	 *		 {@code dividaĈeno}
	 */
	public Analizaĵo getAnalizaĵoByString(String string) {
		for(Analizaĵo analizaĵo : possibleAnalizaĵoj) {
			if(analizaĵo.toString().equals(string)) {
				return analizaĵo;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		if(getFirstAnalizaĵo() != null) {
			return getFirstAnalizaĵo().toString();
		} else {
			return "";
		}
	}
}
