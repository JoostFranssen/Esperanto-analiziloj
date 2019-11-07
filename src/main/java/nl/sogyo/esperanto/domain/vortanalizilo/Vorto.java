package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.sogyo.esperanto.API.VorterSpeco;

public class Vorto {
	private String vorto;
	private Set<Analizaĵo> eblajAnalizaĵoj;
	private Analizaĵo ĉefAnalizaĵo;
	
	public Vorto(String vorto) {
		this.vorto = vorto;
		eblajAnalizaĵoj = new HashSet<>();
		
		analiziĝi();
		filtriNevalidajnAnalizaĵojn();
	}
	
	private void analiziĝi() {
		if(vorto.isEmpty()) {
			eblajAnalizaĵoj.add(new Analizaĵo());
		}
		
		for(int i = vorto.length() - 1; i >= 0; i--) {
			String forprenaĵo = vorto.substring(i);
			List<Vortero> rezulto = DatumbazKomunikilo.preniElDatumbazo(forprenaĵo, v -> v.getVorterSpeco() != VorterSpeco.LITERO);
			if(!rezulto.isEmpty()) {
				Vorto subVorto = new Vorto(vorto.substring(0, i));
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
}
