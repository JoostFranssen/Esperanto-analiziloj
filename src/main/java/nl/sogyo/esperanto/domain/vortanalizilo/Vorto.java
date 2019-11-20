package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	
	public static final Predicate<IVortero> defaultFilterForDatabase = v -> {
		return v.getVorterSpeco() != VorterSpeco.LITERO && !v.equals(Vortero.J_SUFIKSO) && !v.equals(Vortero.ĈJ_SUFIKSO) && !v.equals(Vortero.NJ_SUFIKSO);
	};
	public static final Vorto ĈU = new Vorto("ĉu");
	public static final Vorto DA = new Vorto("da");
	public static final Vorto AJN = new Vorto("ajn");
	public static final Vorto ĈI = new Vorto("ĉi");

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
		String[] parts = vorto.split("-", 2);
		this.vorto = parts[0];
		possibleAnalizaĵoj = new HashSet<>();
		
		analyze();
		
		if(parts.length >= 2) {
			addVorto(new Vorto(parts[1], false));
		}
		
		this.vorto = String.join("-", parts);
		
		
		if(filterInvalidAnalizaĵoj) {
			filterInvalidAnalizaĵoj();
		}
	}
	public Vorto(Vorto vorto) {
		this.vorto = vorto.getVorto();
		this.possibleAnalizaĵoj = vorto.possibleAnalizaĵoj.stream().map(a -> new Analizaĵo(a)).collect(Collectors.toSet());
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
				for(Vortero v : result) {
					Vorto subVorto = new Vorto(vorto.substring(0, i), false);
					subVorto.addVortero(v);
					subVorto.possibleAnalizaĵoj.forEach(possibleAnalizaĵoj::add);
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
	
	/**
	 * Aldonas vorteron al ĉi tiu vorto kaj al ĝiaj analizaĵoj.
	 * @param vortero la vortero por aldoni je la fino
	 */
	private void addVortero(Vortero vortero) {
		vorto += vortero.getVortero();
		possibleAnalizaĵoj.forEach(a -> a.addVortero(vortero));
	}
	
	/**
	 * Aldonas tutan vorton je la fino de ĉi tiu vorto kaj kombinas ĉiuj analizaĵoj de ambaŭ vortoj.
	 * @param vorto la vorto por aldoni je la fino
	 */
	private void addVorto(Vorto vorto) {
		this.vorto += vorto.getVorto();
		Set<Analizaĵo> newAnalizaĵoj = new HashSet<>();
		for(Analizaĵo analizaĵo : possibleAnalizaĵoj) {
			for(Analizaĵo otherAnalizaĵo : vorto.possibleAnalizaĵoj) {
				Analizaĵo newAnalizaĵo = new Analizaĵo(analizaĵo);
				newAnalizaĵo.addAnalizaĵo(otherAnalizaĵo);
				newAnalizaĵoj.add(newAnalizaĵo);
			}
		}
		possibleAnalizaĵoj = newAnalizaĵoj;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vorto == null) ? 0 : vorto.hashCode());
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
		Vorto other = (Vorto)obj;
		if(vorto == null) {
			if(other.vorto != null) {
				return false;
			}
		} else if(!vorto.equalsIgnoreCase(other.vorto)) {
			return false;
		}
		return true;
	}
}
