package nl.sogyo.esperanto.domain.vortanalizilo;

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
	private Set<Analizaĵo> eblajAnalizaĵoj;
	private Analizaĵo ĉefAnalizaĵo;
	
	/**
	 * Iniciatas novan vorton, analizas ĝin por trovi {@code Analizaĵo}jn, kiuj poste estas filtritaj laŭ ĝia valideco.
	 * @param vorto
	 */
	public Vorto(String vorto) {
		this(vorto, true);
	}
	public Vorto(String vorto, boolean filtriNevalidajnAnalizaĵojn) {
		this.vorto = vorto;
		eblajAnalizaĵoj = new HashSet<>();
		
		analiziĝi();
		if(filtriNevalidajnAnalizaĵojn) {
			filtriNevalidajnAnalizaĵojn();
		}
	}
	
	/**
	 * Iniciatas la aron da {@code Analizaĵo}j.
	 */
	private void analiziĝi() {
		if(vorto.isEmpty()) {
			eblajAnalizaĵoj.add(new Analizaĵo());
		}
		
		for(int i = vorto.length() - 1; i >= 0; i--) {
			String forprenaĵo = vorto.substring(i);
			List<Vortero> rezulto = DatumbazKomunikilo.preniElDatumbazo(forprenaĵo, v -> v.getVorterSpeco() != VorterSpeco.LITERO);
			if(!rezulto.isEmpty()) {
				Vorto subVorto = new Vorto(vorto.substring(0, i), false);
				subVorto.analiziĝi();
				for(Analizaĵo analizaĵo : subVorto.eblajAnalizaĵoj) {
					for(Vortero v : rezulto) {
						Analizaĵo novaAnalizaĵo = new Analizaĵo(analizaĵo);
						novaAnalizaĵo.aldoniVorteron(v);
						eblajAnalizaĵoj.add(novaAnalizaĵo);
					}
				}
			}
		}
	}
	
	/**
	 * Filtras la {@code Analizaĵo}jn laŭ valideco.
	 */
	private void filtriNevalidajnAnalizaĵojn() {
		eblajAnalizaĵoj.removeIf(a -> !a.estasValida());
	}
	
	public String getVorto() {
		return vorto;
	}

	public Set<Analizaĵo> getEblajAnalizaĵoj() {
		return new HashSet<>(eblajAnalizaĵoj);
	}

	public Analizaĵo getĈefAnalizaĵo() {
		return ĉefAnalizaĵo;
	}
	
	/**
	 * Prenas unu el la eblaj {@code Analizaĵo}j laŭ la donita ŝablono. Redonas {@code null}, se neniu estas trovata.
	 * @param dividaĈeno {@code String}-objekto, kiu reprezentas la vort-dividon de la dezirata {@code Analizaĵo}. Vertikala streko funkciu por la divido; ekzemple: "esper|ant|o"
	 * @return unu el la eblaj {@code Analizaĵo}j, kiu kongruas kun {@code dividaĈeno}
	 */
	public Analizaĵo preniAnalizaĵonLaŭDividaĈeno(String dividaĈeno) {
		for(Analizaĵo analizaĵo : eblajAnalizaĵoj) {
			if(analizaĵo.toString().equals(dividaĈeno)) {
				return analizaĵo;
			}
		}
		return null;
	}
}
