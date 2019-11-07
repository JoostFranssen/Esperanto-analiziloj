package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vorto {
	private String vorto;
	private Set<Analizaĵo> eblajAnalizaĵoj;
	private Analizaĵo ĉefAnalizaĵo;
	
	public Vorto(String vorto) {
		this(vorto, new HashSet<>());
	}
	private Vorto(String vorto, Set<Analizaĵo> eblajAnalizaĵoj) {
		this.vorto = vorto;
		eblajAnalizaĵoj = this.eblajAnalizaĵoj;
	}
	
	public void analiziĝi() {
		List<Vorto> analizitajVortoj = new ArrayList<>();
		for(int i = vorto.length() - 1; i >= 0; i--) {
			String forprenaĵo = vorto.substring(i);
			
		}
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
