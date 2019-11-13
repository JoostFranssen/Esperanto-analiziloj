package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * Unu el la eblaj rezultoj de analizado de vorto. Ĉi tio reprezentas eblan disigon de vorto en siaj plej etaj partoj (krom literoj). Ekzemple, {@code Analizaĵo} de 'scii' entenas vorteroj 'sci' kaj 'i'.
 * Analizaĵo mem povas determini diversajn {@code Trajto}jn pri si mem.
 * @author jfranssen
 *
 */
public class Analizaĵo {
	private List<Vortero> vorteroj;
	
	public Analizaĵo() {
		this(new ArrayList<>());
	}
	public Analizaĵo(List<Vortero> vorteroj) {
		this.vorteroj = vorteroj;
	}
	/**
	 * Kreas profundan kopion de la donita {@code Analizaĵo}-objekto.
	 * @param analizaĵo
	 */
	public Analizaĵo(Analizaĵo analizaĵo) {
		vorteroj = new ArrayList<>(analizaĵo.vorteroj);
		vorteroj.replaceAll(v -> new Vortero(v));
	}
	
	/**
	 * Kontrolas, ĉu la analizaĵo estas valida aŭ senchava. Malplena analizaĵo estas valida.
	 * @return ĉu la analizaĵo estas valida aŭ ne.
	 */
	public boolean estasValida() {
		if(
			vorteroj.isEmpty()
			|| !vorterSpecoAperasNurSola(VorterSpeco.INTERJEKCIO)
			|| !vorterSpecoAperasNurSola(VorterSpeco.ARTIKOLO)
			|| !vorterSpecoAperasNurKomence(VorterSpeco.KONJUNKCIO)
			|| !vorterSpecoAperasNurKomence(VorterSpeco.KORELATIVO)
			|| !vorterSpecoAperasNurKomenceKunEventualaPrefikso(VorterSpeco.PREPOZICIO)
			|| !vorterSpecoAperasNurKomence(VorterSpeco.PRONOMO)
			|| !verbaFinaĵoAperasNurLaste()
			|| sufiksoAperasPostFinaĵo()
			|| lastaVorteroEstasAfiksoAŭRadiko()
			|| finaĵoAperasKomence()
			|| !akuzativoAperasNurLasteAŭPostE()
		) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Kontras, ĉu finaĵo aperas kiel la unua vortero.
	 * @return ĉu la unua vortero estas finaĵo aŭ ne
	 */
	private boolean finaĵoAperasKomence() {
		return vorteroj.get(0).getVorterSpeco() == VorterSpeco.FINAĴO;
	}
	
	/**
	 * Kontrolas, ĉu la akuzativa finaĵo aperas nur en la lasta pozicio aŭ tuj post la e-finaĵo. Ĉi-lasta estas por permesi vortojn kiel ‘reen-iri’.
	 * @return ĉu la n-finaĵo aperas nur laste aŭ tuj post la e-finaĵo
	 */
	private boolean akuzativoAperasNurLasteAŭPostE() {
		for(int i = 1; i < vorteroj.size() - 1; i++) {
			if(vorteroj.get(i).equals(Vortero.N_FINAĴO)) {
				if(!vorteroj.get(i - 1).equals(Vortero.E_FINAĴO)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu iu ajn verba finaĵo aperas nur en la lasta pozicio de la vort.
	 * @return ĉu verbaj finaĵoj aperas nur laste
	 */
	private boolean verbaFinaĵoAperasNurLaste() {
		for(int i = 0; i < vorteroj.size() - 1; i++) {
			if(
				vorteroj.get(i).equals(Vortero.I_FINAĴO)
				|| vorteroj.get(i).equals(Vortero.IS_FINAĴO)
				|| vorteroj.get(i).equals(Vortero.AS_FINAĴO)
				|| vorteroj.get(i).equals(Vortero.OS_FINAĴO)
				|| vorteroj.get(i).equals(Vortero.US_FINAĴO)
				|| vorteroj.get(i).equals(Vortero.U_FINAĴO)
			) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu vorteroj kun ĉi tiu {@code VorterSpeco} aperas sola.
	 * @param vorterSpeco {@code VorterSpeco}, kiu rajtas nur aperi tute sola
	 * @return ĉu, se unu el la vorteroj estas interjekcio, tiam ĝi estu sola
	 */
	private boolean vorterSpecoAperasNurSola(VorterSpeco vorterSpeco) {
		if(vorteroj.size() >= 2) {
			for(Vortero vortero : vorteroj) {
				if(vortero.getVorterSpeco() == vorterSpeco) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu vorteroj kun ĉi tiu {@code VorterSpeco} aperas nur en la komenco de la vorto.
	 * @param vorterSpeco {@code VorterSpeco}, kiu rajtas nur stari en la komenco de vorto
	 * @return ĉu ĉi tiaj vorteroj aperas nur en la komenco
	 */
	private boolean vorterSpecoAperasNurKomence(VorterSpeco vorterSpeco) {
		for(int i = 1; i < vorteroj.size(); i++) {
			if(vorteroj.get(i).getVorterSpeco() == vorterSpeco) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu vorteroj kun ĉi tiu {@code VorterSpeco} aperas nur en la komenco kun eventuale iuj prefiksoj antaŭ ĝi.
	 * @param vorterSpeco {@code VorterSpeco}, kiu rajtas nur stari en la komenco de vorto kun iuj prefiksoj
	 * @return ĉu ĉi tiaj vorteroj aperas nur en la komenco kun eventualaj prefiskoj antaŭ si
	 */
	private boolean vorterSpecoAperasNurKomenceKunEventualaPrefikso(VorterSpeco vorterSpeco) {
		boolean nurPrefiksoj = true;
		for(Vortero vortero : vorteroj) {
			if(vortero.getVorterSpeco() == vorterSpeco) {
				if(!nurPrefiksoj) {
					return false;
				}
			}
			if(vortero.getVorterSpeco() != VorterSpeco.PREFIKSO) {
				nurPrefiksoj = false;
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu iu sufikso aperas tuj pas iu finaĵo.
	 * @return ĉu iu ajn el la sufiksoj el la vorteroj aperas tuj post finaĵo
	 */
	private boolean sufiksoAperasPostFinaĵo() {
		Vortero antaŭaVortero = null;
		for(Vortero vortero : vorteroj) {
			if(antaŭaVortero != null && antaŭaVortero.getVorterSpeco() == VorterSpeco.FINAĴO) {
				if(vortero.getVorterSpeco() == VorterSpeco.SUFIKSO) {
					return true;
				}
			}
			
			antaŭaVortero = vortero;
		}
		return false;
	}
	
	/**
	 * Kontrolas, ĉu la lasta vortero estas prefikso, sufikso aŭ radiko (laŭ {@code VorterSpeco}), ĉar tio neniel okazu.
	 * @return ĉu la lasta vortero estas afikso aŭ radiko
	 */
	private boolean lastaVorteroEstasAfiksoAŭRadiko() {
		VorterSpeco vs = lastaVortero().getVorterSpeco();
		return vs == VorterSpeco.PREFIKSO || vs == VorterSpeco.SUFIKSO || vs == VorterSpeco.RADIKO;
	}
	
	public List<Vortero> getVorteroj() {
		return new ArrayList<>(vorteroj);
	}
	
	protected void aldoniVorteron(Vortero vortero) {
		vorteroj.add(vortero);
	}
	protected void aldoniVorteron(int indico, Vortero vortero) {
		vorteroj.add(indico, vortero);
	}
	
	protected Vortero preniVorteron(int indico) {
		return vorteroj.get(indico);
	}
	
	protected void forigiVorteron(int indico) {
		vorteroj.remove(indico);
	}
	protected void forigiVorteron(Vortero vortero) {
		vorteroj.remove(vortero);
	}
	
	/**
	 * Tuj kontralas, ĉu la {@code Analizaĵo} havas la {@code Trajto}n.
	 * @param trajto la {@code Trajto}, pri kiu oni volas kontroli, ke la {@code Analizaĵo} havas ĝin
	 * @return ĉu ĝi havas la {@code Trajto}n.
	 */
	public boolean kontroliTrajton(Trajto trajto) {
		switch(trajto) {
			case ADJEKTIVO: return aroDeLastajFinaĵoj().contains(Vortero.A_FINAĴO);
			case ADVERBO: return estasAdverbo();
			case AKUZATIVO: return lastaVortero().equals(Vortero.N_FINAĴO);
			case ARTIKOLO: return vorteroj.get(0).getVorterSpeco() == VorterSpeco.ARTIKOLO;
			case INTERJEKCIO: return lastaVortero().getVorterSpeco() == VorterSpeco.INTERJEKCIO;
			case KONJUNKCIO: return lastaVortero().getVorterSpeco() == VorterSpeco.KONJUNKCIO;
			case KORELATIVO: return estasKorelativo();
			case NUMERALO: return estasNumeralo();
			case PLURALO: return aroDeLastajFinaĵoj().contains(Vortero.J_FINAĴO);
			case PREPOZICIO: return lastaVortero().getVorterSpeco() == VorterSpeco.PREPOZICIO;
			case PRONOMO: return lastaVortero().getVorterSpeco() == VorterSpeco.PRONOMO;
			case SONIMITO: return lastaVortero().getVorterSpeco() == VorterSpeco.SONIMITO;
			case SUBSTANTIVO: return aroDeLastajFinaĵoj().contains(Vortero.O_FINAĴO);
			case VERBO: return estasVerbo();
			case VERBO_FUTURO: return lastaVortero().equals(Vortero.OS_FINAĴO);
			case VERBO_INFINITIVO: return lastaVortero().equals(Vortero.I_FINAĴO);
			case VERBO_KONDICIONALO: return lastaVortero().equals(Vortero.US_FINAĴO);
			case VERBO_NETRANSITIVA: break;
			case VERBO_PRETERITO: return lastaVortero().equals(Vortero.IS_FINAĴO);
			case VERBO_PREZENCO: return lastaVortero().equals(Vortero.AS_FINAĴO);
			case VERBO_TRANSITIVA: return estasTransitiva();
			case VERBO_VOLITIVO: return lastaVortero().equals(Vortero.U_FINAĴO);
		}
		
		return false;
	}
	
	public boolean estasTransitiva() {
		if(!estasVerbo()) {
			return false;
		}
		
		boolean transitiva = false;
		for(Vortero vortero : vorteroj) {
			if(vortero.getTransitiveco() == Transitiveco.TRANSITIVA || vortero.getTransitiveco() == Transitiveco.AMBAŬ) {
				transitiva = true;
			} else if(vortero.equals(Vortero.PASIVA_FINITA_PARTICIPA_SUFIKSO) || vortero.equals(Vortero.PASIVA_DAŬRA_PARTICIPA_SUFIKSO) || vortero.equals(Vortero.PASIVA_ESTONTA_PARTICIPA_SUFIKSO)) {
				transitiva = false;
			} else if(vortero.equals(Vortero.IĜ_SUFIKSO)) {
				transitiva = false;
			} else if(vortero.getVorterSpeco() == VorterSpeco.PREPOZICIO) {
				transitiva = true;
			}
		}
		
		return transitiva;
	}
	
	/**
	 * Testas, ĉu la vorto finiĝas per unu el la verbaj finaĵoj.
	 * @return ĉu la vort estas verbo
	 */
	public boolean estasVerbo() {
		List<Trajto> verbajTrajtoj = new ArrayList<Trajto>(Arrays.asList(Trajto.VERBO_FUTURO, Trajto.VERBO_INFINITIVO, Trajto.VERBO_KONDICIONALO, Trajto.VERBO_PRETERITO, Trajto.VERBO_PREZENCO, Trajto.VERBO_VOLITIVO));
		for(Trajto trajto : verbajTrajtoj) {
			if(kontroliTrajton(trajto)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Testas, ĉu la vorto estas numeralo.
	 * @return ĉu la vorto estas numeralo
	 */
	public boolean estasNumeralo() {
		for(Vortero vortero : vorteroj) {
			if(vortero.getVorterSpeco() != VorterSpeco.NUMERALO) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determinas, ĉu la vorto estas korelativo. Ankaŭ inkluzivas neni/o.
	 * @return ĉu la vorto estas korelativo
	 */
	public boolean estasKorelativo() {
		if(vorteroj.get(0).getVorterSpeco() == VorterSpeco.KORELATIVO) {
			return true;
		}
		return(vorteroj.get(0).equals(new Vortero("neni", VorterSpeco.RADIKO, Transitiveco.NEDIFINITA)));
	}
	
	/**
	 * Testas, ĉu ĉi tiu {@code Analizaĵo} estas adverbo.
	 * @return ĉu ĝi estas adverbo
	 */
	public boolean estasAdverbo() {
		if(lastaVortero().getVorterSpeco() == VorterSpeco.ADVERBO) {
			return true;
		}
		return aroDeLastajFinaĵoj().contains(Vortero.E_FINAĴO);
	}
	
	/**
	 * Redonas la lastan vorteron. Ĉi tiu metodo supozas, ke la listo da vorteroj ne estas malplena.
	 * @return la lastan vorteron el ĉiuj vorteroj
	 */
	public Vortero lastaVortero() {
		return vorteroj.get(vorteroj.size() - 1);
	}
	
	/**
	 * Determinas ĉiujn finaĵojn, kiuj aperas je la fino de la vorto. Se neniu finaĵo aperas, malplena aro estas redonita. La finaĵoj estas en arbitra ordo. Ne aperas duablajn finaĵojn.
	 * @return aron de la lastaj finaĵoj 
	 */
	public Set<Vortero> aroDeLastajFinaĵoj() {
		Set<Vortero> finaĵoj = new HashSet<Vortero>();
		for(int i = vorteroj.size() - 1; i >= 0; i--) {
			if(vorteroj.get(i).getVorterSpeco() == VorterSpeco.FINAĴO) {
				if(!finaĵoj.add(vorteroj.get(i))) { //preventi duoblajn finaĵojn
					break;
				}
			} else {
				break;
			}
		}
		return finaĵoj;
	}
	
	@Override
	public String toString() {
		StringBuilder ĉenilo = new StringBuilder();
		for(Vortero vortero : vorteroj) {
			ĉenilo.append("|" + vortero.getVortero());
		}
		if(ĉenilo.length() == 0) {
			return "";
		} else {
			return ĉenilo.substring(1);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vorteroj == null) ? 0 : vorteroj.hashCode());
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
		Analizaĵo other = (Analizaĵo)obj;
		if(vorteroj == null) {
			if(other.vorteroj != null) {
				return false;
			}
		} else if(!vorteroj.equals(other.vorteroj)) {
			return false;
		}
		return true;
	}
}
