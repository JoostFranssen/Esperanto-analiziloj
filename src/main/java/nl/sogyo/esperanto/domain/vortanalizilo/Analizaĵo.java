package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	private static final Pattern LAST_FINAĴOJ_PATTERN = Pattern.compile("^(i)|(is)|(as)|(os)|(us)|(u)|(oj?n?)|(aj?n?)|(en?)|(j?n?)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE); //(j?n?) por permesi neniun finaĵon kaj nur j/n kun korelativoj: kio|n
	private static final Pattern ADJEKTIVA_KORELATIVO_PATTERN = Pattern.compile(".*(a|u|es)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern SUBSTANTIVA_KORELATIVO_PATTERN = Pattern.compile(".*(om?)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final Pattern ADVERBA_KORELATIVO_PATTERN = Pattern.compile(".*(al|am|el?)$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
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
	public boolean isValid() {
		if(
			vorteroj.isEmpty()
			|| !vorterSpecoAppearsOnlyAlone(VorterSpeco.INTERJEKCIO)
			|| !vorterSpecoAppearsOnlyAlone(VorterSpeco.ARTIKOLO)
			|| !vorterSpecoAppearsOnlyBeginning(VorterSpeco.KONJUNKCIO)
			|| !vorterSpecoAppearsOnlyBeginning(VorterSpeco.ADVERBO)
			|| !vorterSpecoAppearsOnlyBeginningWithExceptions(VorterSpeco.PREPOZICIO, VorterSpeco.PREFIKSO)
			|| !vorterSpecoAppearsOnlyBeginningWithExceptions(VorterSpeco.KORELATIVO, VorterSpeco.PREFIKSO, VorterSpeco.PREPOZICIO)
			|| !vorterSpecoAppearsOnlyBeginning(VorterSpeco.PRONOMO)
			|| !verbaFinaĵoAppearsOnlyEnd()
			|| sufiksoAppearsAfterFinaĵo()
			|| lastVorteroIsAfiksoOrRadiko()
			|| finaĵoAppearsBeginning()
			|| !akuzativoAppearsOnlyEndOrAfterEOrSi()
			|| !lastFinaĵojAreInCorrectFormation()
		) {
			return false;
		}
		return true;
	}
	
	/**
	 * Kontras, ĉu finaĵo aperas kiel la unua vortero.
	 * @return ĉu la unua vortero estas finaĵo aŭ ne
	 */
	private boolean finaĵoAppearsBeginning() {
		return vorteroj.get(0).getVorterSpeco() == VorterSpeco.FINAĴO;
	}
	
	/**
	 * Kontrolas, ĉu la akuzativa finaĵo aperas nur en la lasta pozicio aŭ tuj post la e-finaĵo. Ĉi-lasta estas por permesi vortojn kiel ‘reen-iri’.
	 * @return ĉu la n-finaĵo aperas nur laste aŭ tuj post la e-finaĵo
	 */
	private boolean akuzativoAppearsOnlyEndOrAfterEOrSi() {
		for(int i = 1; i < vorteroj.size() - 1; i++) {
			if(vorteroj.get(i).equals(Vortero.N_FINAĴO)) {
				Vortero previousVortero = vorteroj.get(i - 1);
				if(!previousVortero.equals(Vortero.E_FINAĴO) && !previousVortero.equals(Vortero.SI_PRONOMO)) {
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
	private boolean verbaFinaĵoAppearsOnlyEnd() {
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
	private boolean vorterSpecoAppearsOnlyAlone(VorterSpeco vorterSpeco) {
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
	private boolean vorterSpecoAppearsOnlyBeginning(VorterSpeco vorterSpeco) {
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
	private boolean vorterSpecoAppearsOnlyBeginningWithExceptions(VorterSpeco vorterSpeco, VorterSpeco... exceptions) {
		List<VorterSpeco> exceptionsList = Arrays.asList(exceptions);
		
		boolean onlyExceptions = true;
		for(Vortero vortero : vorteroj) {
			if(vortero.getVorterSpeco() == vorterSpeco) {
				if(!onlyExceptions) {
					return false;
				}
			}
			if(!exceptionsList.contains(vortero.getVorterSpeco())) {
				onlyExceptions = false;
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu iu sufikso aperas tuj pas iu finaĵo.
	 * @return ĉu iu ajn el la sufiksoj el la vorteroj aperas tuj post finaĵo
	 */
	private boolean sufiksoAppearsAfterFinaĵo() {
		Vortero previousVortero = null;
		for(Vortero vortero : vorteroj) {
			if(previousVortero != null && previousVortero.getVorterSpeco() == VorterSpeco.FINAĴO) {
				if(vortero.getVorterSpeco() == VorterSpeco.SUFIKSO) {
					return true;
				}
			}
			
			previousVortero = vortero;
		}
		return false;
	}
	
	/**
	 * Kontrolas, ĉu la lasta vortero estas prefikso, sufikso aŭ radiko (laŭ {@code VorterSpeco}), ĉar tio neniel okazu.
	 * @return ĉu la lasta vortero estas afikso aŭ radiko
	 */
	private boolean lastVorteroIsAfiksoOrRadiko() {
		VorterSpeco vs = getLastVortero().getVorterSpeco();
		return vs == VorterSpeco.PREFIKSO || vs == VorterSpeco.SUFIKSO || vs == VorterSpeco.RADIKO;
	}
	
	/**
	 * Kontrolas, ĉu la lastaj finaĵoj estas en akceptebla formo.
	 * @return ĉu estas tiel
	 */
	private boolean lastFinaĵojAreInCorrectFormation() {
		Optional<String> finaĵojOptionalString = getLastFinaĵoj().stream().map(f -> f.getVortero()).reduce((s1, s2) -> s1 + s2);
		String finaĵoj = "";
		if(!finaĵojOptionalString.isEmpty()) {
			finaĵoj = finaĵojOptionalString.get();
		}
		return LAST_FINAĴOJ_PATTERN.matcher(finaĵoj).matches();
	}
	
	public List<Vortero> getVorteroj() {
		return new ArrayList<>(vorteroj);
	}
	
	protected void addVortero(Vortero vortero) {
		vorteroj.add(vortero);
	}
	protected void addVortero(int index, Vortero vortero) {
		vorteroj.add(index, vortero);
	}
	
	protected Vortero getVortero(int index) {
		return vorteroj.get(index);
	}
	
	protected void removeVortero(int index) {
		vorteroj.remove(index);
	}
	protected void removeVortero(Vortero vortero) {
		vorteroj.remove(vortero);
	}
	
	protected void addAnalizaĵo(Analizaĵo analizaĵo) {
		vorteroj.addAll(analizaĵo.vorteroj);
	}
	
	/**
	 * Tuj kontralas, ĉu la {@code Analizaĵo} havas la {@code Trajto}n.
	 * @param trajto la {@code Trajto}, pri kiu oni volas kontroli, ke la {@code Analizaĵo} havas ĝin
	 * @return ĉu ĝi havas la {@code Trajto}n.
	 */
	public boolean checkTrajto(Trajto trajto) {
		switch(trajto) {
			case ADJEKTIVO: return isAdjektivo();
			case ADVERBO: return isAdverbo();
			case AKUZATIVO: return getLastVortero().equals(Vortero.N_FINAĴO);
			case ARTIKOLO: return vorteroj.get(0).getVorterSpeco() == VorterSpeco.ARTIKOLO;
			case INTERJEKCIO: return getLastVortero().getVorterSpeco() == VorterSpeco.INTERJEKCIO;
			case KONJUNKCIO: return getLastVortero().getVorterSpeco() == VorterSpeco.KONJUNKCIO;
			case KORELATIVO: return isKorelativo();
			case NUMERALO: return isNumeralo();
			case PLURALO: return getLastFinaĵoj().contains(Vortero.J_FINAĴO);
			case PREPOZICIO: return getLastVortero().getVorterSpeco() == VorterSpeco.PREPOZICIO;
			case PRONOMO: return isPronomo();
			case SONIMITO: return getLastVortero().getVorterSpeco() == VorterSpeco.SONIMITO;
			case SUBSTANTIVO: return isSubstantivo();
			case VERBO: return isVerbo();
			case VERBO_FUTURO: return getLastVortero().equals(Vortero.OS_FINAĴO);
			case VERBO_INFINITIVO: return getLastVortero().equals(Vortero.I_FINAĴO);
			case VERBO_KONDICIONALO: return getLastVortero().equals(Vortero.US_FINAĴO);
			case VERBO_NETRANSITIVA: return isNetransitiva();
			case VERBO_PRETERITO: return getLastVortero().equals(Vortero.IS_FINAĴO);
			case VERBO_PREZENCO: return getLastVortero().equals(Vortero.AS_FINAĴO);
			case VERBO_TRANSITIVA: return isTransitiva();
			case VERBO_VOLITIVO: return getLastVortero().equals(Vortero.U_FINAĴO);
		}
		
		return false;
	}
	
	/**
	 * Testas, ĉu vorto estas adjektivo, inkluzive de la korelativoj.
	 * @return ĉu vorto estas adjektivo
	 */
	public boolean isAdjektivo() {
		if(isKorelativo()) {
			String vorteroString = vorteroj.get(0).getVortero();
			return ADJEKTIVA_KORELATIVO_PATTERN.matcher(vorteroString).matches();
		} else {
			return getLastFinaĵoj().contains(Vortero.A_FINAĴO);
		}
	}
	
	/**
	 * Testas, ĉu vorto estas substantivo, inkluzive de la korelativoj.
	 * @return ĉu vorto estas substantivo
	 */
	public boolean isSubstantivo() {
		String vorteroString = vorteroj.get(0).getVortero();
		if(isKorelativo() && !vorteroString.equalsIgnoreCase("neni")) {
			return SUBSTANTIVA_KORELATIVO_PATTERN.matcher(vorteroString).matches();
		} else {
			return getLastFinaĵoj().contains(Vortero.O_FINAĴO);
		}
	}
	
	/**
	 * Testas, ĉu verbo estas transitiva.
	 * @return ĉu verbo estas transitiva. Se la vorto ne estas verbo, la rezulto estos {@code false}
	 */
	public boolean isTransitiva() {
		if(!isVerbo()) {
			return false;
		}
		
		boolean transitiva = false;
		boolean foundPrepozicio = false; //por kontroli transitivecon de vortoj kun la formo ‘prep. + rad.’, kiel ‘eniri’.
		
		for(Vortero vortero : vorteroj) {
			if(foundPrepozicio) {
				if(vortero.getVorterSpeco() != VorterSpeco.FINAĴO) {
					foundPrepozicio = false;
					transitiva = true;
				}
			}
			
			if(vortero.getTransitiveco() == Transitiveco.TRANSITIVA || vortero.getTransitiveco() == Transitiveco.AMBAŬ) {
				transitiva = true;
			} else if(vortero.equals(Vortero.PASIVA_FINITA_PARTICIPA_SUFIKSO) || vortero.equals(Vortero.PASIVA_DAŬRA_PARTICIPA_SUFIKSO) || vortero.equals(Vortero.PASIVA_ESTONTA_PARTICIPA_SUFIKSO)) {
				transitiva = false;
			} else if(vortero.equals(Vortero.IĜ_SUFIKSO)) {
				transitiva = false;
			} else if(vortero.getVorterSpeco() == VorterSpeco.PREPOZICIO) {
				foundPrepozicio = true;
			}
		}
		
		return transitiva;
	}
	
	/**
	 * Testas, ĉu verbo estas netransitiva.
	 * @return ĉu verbo estas netransitiva. Se la vorto ne estas verbo, la rezulto estos {$code false}
	 */
	public boolean isNetransitiva() {
		if(!isVerbo()) {
			return false;
		}
		
		boolean netransitiva = false;
		boolean foundPrepozicio = false; //por preventi, ke vortoj kiel ‘eniri’ markiĝis kiel netransitivaj.
		
		for(Vortero vortero : vorteroj) {
			if(vortero.getTransitiveco() == Transitiveco.NETRANSITIVA || vortero.getTransitiveco() == Transitiveco.AMBAŬ) {
				netransitiva = true;
			}
			
			if(foundPrepozicio) {
				if(vortero.getVorterSpeco() == VorterSpeco.RADIKO) {
					foundPrepozicio = false;
					netransitiva = false;
				}
			}
			
			if(vortero.equals(Vortero.IG_SUFIKSO)) {
				netransitiva = false;
			} else if(vortero.getVorterSpeco() == VorterSpeco.PREPOZICIO) {
				foundPrepozicio = true;
			}
		}
		
		return netransitiva;
	}
	
	/**
	 * Testas, ĉu la vorto finiĝas per unu el la verbaj finaĵoj.
	 * @return ĉu la vort estas verbo
	 */
	public boolean isVerbo() {
		List<Trajto> verbajTrajtoj = new ArrayList<Trajto>(Arrays.asList(Trajto.VERBO_FUTURO, Trajto.VERBO_INFINITIVO, Trajto.VERBO_KONDICIONALO, Trajto.VERBO_PRETERITO, Trajto.VERBO_PREZENCO, Trajto.VERBO_VOLITIVO));
		for(Trajto trajto : verbajTrajtoj) {
			if(checkTrajto(trajto)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Testas, ĉu la vorto estas numeralo.
	 * @return ĉu la vorto estas numeralo
	 */
	public boolean isNumeralo() {
		for(Vortero vortero : vorteroj) {
			if(vortero.getVorterSpeco() != VorterSpeco.NUMERALO) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Kontrolas, ĉu la vorto estas pronomo. Tio estas ĉiam nur pronoma vortero plus eventuala akuzativo, sed ne posedaj pronomoj
	 * @return ĉu la vorto estas pronomo
	 */
	public boolean isPronomo() {
		if(getLastVortero().getVorterSpeco() == VorterSpeco.PRONOMO) {
			return true;
		} else if(getLastVortero().equals(Vortero.N_FINAĴO)) {
			return vorteroj.get(vorteroj.size() - 2).getVorterSpeco() == VorterSpeco.PRONOMO;
		}
		return false;
	}
	
	/**
	 * Determinas, ĉu la vorto estas korelativo. Ankaŭ inkluzivas neni/o.
	 * @return ĉu la vorto estas korelativo
	 */
	public boolean isKorelativo() {
		Vortero firstVortero = vorteroj.get(0);
		if(firstVortero.getVorterSpeco() == VorterSpeco.KORELATIVO || firstVortero.equals(Vortero.NENI_KORELATIVO)) {
			for(int i = 1; i < vorteroj.size(); i++) {
				Vortero vortero = vorteroj.get(i);
				if(!vortero.equals(Vortero.J_FINAĴO) && !vortero.equals(Vortero.N_FINAĴO) && (firstVortero.equals(Vortero.NENI_KORELATIVO) ? !vortero.equals(Vortero.O_FINAĴO) : true)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Testas, ĉu ĉi tiu {@code Analizaĵo} estas adverbo.
	 * @return ĉu ĝi estas adverbo
	 */
	public boolean isAdverbo() {
		if(isKorelativo()) {
			String vorteroString = vorteroj.get(0).getVortero();
			return ADVERBA_KORELATIVO_PATTERN.matcher(vorteroString).matches();
		} else if(getLastVortero().getVorterSpeco() == VorterSpeco.ADVERBO) {
			return true;
		} else {
			return getLastFinaĵoj().contains(Vortero.E_FINAĴO);
		}
	}
	
	/**
	 * Redonas la lastan vorteron. Ĉi tiu metodo supozas, ke la listo da vorteroj ne estas malplena.
	 * @return la lastan vorteron el ĉiuj vorteroj
	 */
	public Vortero getLastVortero() {
		return vorteroj.get(vorteroj.size() - 1);
	}
	
	/**
	 * Determinas ĉiujn finaĵojn, kiuj aperas je la fino de la vorto. Se neniu finaĵo aperas, malplena aro estas redonita. La finaĵoj estas en la ordo, en kiu ili aperas.
	 * @return aron de la lastaj finaĵoj 
	 */
	public List<Vortero> getLastFinaĵoj() {
		List<Vortero> finaĵoj = new ArrayList<>();
		for(int i = vorteroj.size() - 1; i >= 0; i--) {
			if(vorteroj.get(i).getVorterSpeco() == VorterSpeco.FINAĴO) {
				finaĵoj.add(0, vorteroj.get(i));
			} else {
				break;
			}
		}
		return finaĵoj;
	}
	
	/**
	 * Kontrolas, ĉu la finaĵoj de ĉi tiu analizaĵ kongruas kun la donita listo de finaĵoj.
	 * @param finaĵoj la finaĵoj, kun kiuj la analizaĵ kongruu
	 * @return ĉu la finaĵoj kongruas unuj kun la aliaj
	 */
	public boolean matchFinaĵoj(List<Vortero> finaĵoj) {
		Predicate<Vortero> ignoreAEOFinaĵoj = v -> !v.equals(Vortero.O_FINAĴO) && !v.equals(Vortero.A_FINAĴO) && !v.equals(Vortero.E_FINAĴO);
		return finaĵoj.stream().filter(ignoreAEOFinaĵoj).collect(Collectors.toList())
				.equals(getLastFinaĵoj().stream().filter(ignoreAEOFinaĵoj).collect(Collectors.toList()));
	}
	
	/**
	 * Kontrolas, ĉu la finaĵoj kongruas kun tiuj de la donita analizaĵo.
	 * @param analizaĵo analizaĵo, kies finaĵoj kungruu kun ĉi tiu
	 * @return ĉu la finaĵoj de ambaŭ analizaĵoj kongruas
	 */
	public boolean matchFinaĵojOf(Analizaĵo... analizaĵoj) {
		return Arrays.asList(analizaĵoj).stream().filter(a -> matchFinaĵoj(a.getLastFinaĵoj())).count() == analizaĵoj.length;
	}
	
	@Override
	public String toString() {
		return String.join("|", vorteroj.stream().map(v -> v.getVortero()).toArray(String[]::new));
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
	
	/**
	 * Determinas la kvanton de vortero, kiuj kongruas kun la donita {@code condition}.
	 * @param condition @{code Predicate<Vortero>} por testi ĉiun vorteron. Se tio redonas {@code true}, tiam ĝi estas alkalkulita
	 * @return la kvanton de vorteroj, kiuj kongruas kun {@code condition}
	 */
	private int countVorteroj(Predicate<Vortero> condition) {
		return (int)vorteroj.stream().filter(condition).count();
	}
	
	/**
	 * Komparas la vorternombradon kun {@code alia}.
	 * @param alia @{code Analizaĵo} por kompari
	 * @param condition @{code Predicate<Vortero>} por testi ĉiun vorteron. Se tio redonas {@code true}, tiam ĝi estas alkalkulita
	 * @return la diferencon de la kvaonto de vorteroj
	 */
	private int compareCount(Analizaĵo alia, Predicate<Vortero> condition) {
		return countVorteroj(condition) - alia.countVorteroj(condition);
	}
	
	/**
	 * Rimarku, ke ĉi tiu {@code Comparator} ne estas konsekvenca, t.e., ne nepre estas du {@code Analizaĵo}j egalaj, se ĉi tiu {@code Comparator} redonas {@code 0}. 
	 * @return {@code Comparater<Analizaĵo} por ordigi {@code Analizaĵo}jn
	 */
	public static Comparator<Analizaĵo> getComparator() {
		return (a1, a2) -> {
			int difference = a1.compareCount(a2, v -> true);
			if(difference != 0) {
				return difference;
			}
			difference = a1.compareCount(a2, v -> v.getVorterSpeco() == VorterSpeco.RADIKO);
			if(difference != 0) {
				return difference;
			}
			
			return 0;
		};
	}
}
