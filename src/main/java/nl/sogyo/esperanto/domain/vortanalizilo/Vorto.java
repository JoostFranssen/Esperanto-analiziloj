package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Analizita vorto.
 * @author jfranssen
 *
 */
public class Vorto {
	private String vorto;
	private Set<Analizaĵo> possibleAnalizaĵoj;
	
	/**
	 * Iniciatas novan vorton, analizas ĝin por trovi {@code Analizaĵo}jn, kiuj poste estas filtritaj laŭ ĝia valideco.
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
			List<Vortero> result = DatabaseCommunicator.getFromDatabase(takenPart, v -> v.getVorterSpeco() != VorterSpeco.LITERO);
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
		ArrayList<Analizaĵo> possibleAnalizaĵojList = new ArrayList<>(possibleAnalizaĵoj);
		Collections.sort(possibleAnalizaĵojList, Analizaĵo.getComparator());
		return possibleAnalizaĵojList;
	}
	
	/**
	 * Prenas unu el la eblaj {@code Analizaĵo}j laŭ la donita ŝablono. Redonas {@code null}, se neniu estas trovata.
	 * @param string {@code String}-objekto, kiu reprezentas la vort-dividon de la dezirata {@code Analizaĵo}. Vertikala streko funkciu por la divido; ekzemple: "esper|ant|o"
	 * @return unu el la eblaj {@code Analizaĵo}j, kiu kongruas kun {@code dividaĈeno}
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
		if(possibleAnalizaĵoj.size() > 0) {
			return getPossibleAnalizaĵojSorted().get(0).toString();
		} else {
			return "";
		}
	}
}
