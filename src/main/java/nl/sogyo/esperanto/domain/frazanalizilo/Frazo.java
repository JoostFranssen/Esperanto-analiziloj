package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Reprezentas frazon/
 * @author jfranssen
 *
 */
public class Frazo {
	private Vorto[] vortoj;
	private List<Frazero> frazeroj;
	
	public Frazo(String frazo) {
		String[] vortoStrings = frazo.replaceAll("[,\\.!\\?]", "").split("\\s");
		
		vortoj = new Vorto[vortoStrings.length];
		IntStream.range(0, vortoStrings.length).parallel().forEach(i -> vortoj[i] = new Vorto(vortoStrings[i]));
		
		analyze();
		addRelatedFrazeroj();
	}
	
	/**
	 * Analizas la frazon kaj inicias la liston de {@code Frazero}j
	 */
	private void analyze() {
		frazeroj = new ArrayList<>();
		
		determineFrazeroj();
		
		setĈefverbo();
		setFunkcioFromTrajto(f -> f.getVortoj().get(0), Trajto.PREPOZICIO, Funkcio.PREPOZICIAĴO);
		setFunkcioFromTrajto(Frazero::getLastVortoWithSkip, Trajto.ADVERBO, Funkcio.ADVERBO);
		setFunkcioFromTrajto(Frazero::getLastVortoWithSkip, Trajto.KONJUNKCIO, Funkcio.KONJUNKCIO);
		setSubjektoAndPredikativo();
		setObjektoj();
		setInfinitivoj();
	}
	
	/**
	 * Determinas la disigon de la frazo en frazerojn.
	 */
	private void determineFrazeroj() {
		Iterator<Vorto> iterator = Arrays.asList(vortoj).iterator();
		/*
		 * Ni bezonas tian konstruon, ĉar ni bezonas la sekvan vorton por analizi la lastan grupon de vortoj.
		 * Do post la lasta vorto el ‘vortoj’ ni devas fari kroman iteracion.
		 */
		Frazero frazero = new Frazero();
		do {
			Vorto vorto = (iterator.hasNext() ? iterator.next() : null);
			
			if(shouldGoToNextFrazero(frazero, vorto)) {
				frazeroj.add(frazero);
				
				frazero = new Frazero();
			}
			
			if(vorto == null) {
				break;
			}
			
			frazero.addVorto(vorto);
		} while(true);
	}
	
	/**
	 * Determinas de la lasta grupo de vortoj kaj la nuna vorto, ĉu ni kreu frazeron el la lasta grupo aŭ ne
	 * @param frazero la kreata frazero
	 * @param currentVorto la nuna vorto, kiu tuj sekvas frazerVortojn
	 * @return ĉu ni kreu frazeron el la frazerVortoj aŭ aldonu la nuna vorto al la frazerVortoj
	 */
	private boolean shouldGoToNextFrazero(Frazero frazero, Vorto currentVorto) {
		List<Vorto> frazerVortoj = frazero.getVortoj();
		if(frazerVortoj.isEmpty()) {
			return false;
		}
		
		if(currentVorto == null) { //se ni estas ĉe la fino de la frazo
			return true;
		}
		
		Vorto lastVorto = frazerVortoj.get(frazerVortoj.size() - 1);
		
		//la vortoj ‘ajn’ kaj ‘ĉi’ modifas la korelativon, kiu staras antaŭ ĝi; ni vere volas rigardi ties funkcion
		if(lastVorto.equals(Vorto.AJN) || lastVorto.equals(Vorto.ĈI)) {
			if(frazerVortoj.size() >= 2) {
				lastVorto = frazerVortoj.get(frazerVortoj.size() - 2);
			}
		}
		
		
		//konjunkcio ĉiam kuntenu frazeron, ĉar ni ne konsideras subfrazojn; sed ‘ĉu’ ĉiam estu aparta
		if(lastVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return lastVorto.equals(Vorto.ĈU);
		}
		if(currentVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return currentVorto.equals(Vorto.ĈU);
		}
		
		//infinitivo ĉiam estas tute aparta
		if(lastVorto.checkTrajto(Trajto.VERBO_INFINITIVO) || currentVorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
			return true;
		}
		
		if(currentVorto.checkTrajto(Trajto.PREPOZICIO)) { //prepozicio ĉiam komencas novan frazeron, krom post alia prepozicio kaj krom ‘da’
			return !lastVorto.checkTrajto(Trajto.PREPOZICIO) && !currentVorto.equals(Vorto.DA);
		}
		if(lastVorto.checkTrajto(Trajto.PREPOZICIO)) { //prepozicio ĉiam ligas al la sekva parto (escepte kun sekva infinitivo)
			return false;
		}
		
		if(currentVorto.equals(Vorto.AJN) || currentVorto.equals(Vorto.ĈI)) {
			return !lastVorto.checkTrajto(Trajto.KORELATIVO);
		}
		if(lastVorto.equals(Vorto.ĈI)) {
			return !currentVorto.checkTrajto(Trajto.KORELATIVO);
		}
		
		if(lastVorto.checkTrajto(Trajto.ADVERBO)) {
			if(currentVorto.checkTrajto(Trajto.VERBO)) {
				return true;
			}
			if(currentVorto.checkTrajto(Trajto.ADVERBO) || currentVorto.checkTrajto(Trajto.ADJEKTIVO)) {
				return false;
			}
		}
		
		if(lastVorto.checkTrajto(Trajto.ARTIKOLO)) { //artikolo neniam povas stari sole
			return false;
		}
		if(currentVorto.checkTrajto(Trajto.ARTIKOLO)) { //artikolo ĉiam staras ĉe la komenco de frazero (krom post prepozicio)
			return true;
		}
		
		if(lastVorto.checkTrajto(Trajto.SUBSTANTIVO)) {
			if(currentVorto.checkTrajto(Trajto.ADVERBO)) {
				return true;
			}
		}
		
		if(currentVorto.checkTrajto(Trajto.PRONOMO) || lastVorto.checkTrajto(Trajto.PRONOMO)) { //pronomo ĉiam komencas novan frazeron
			return true;
		}
		
		if(!currentVorto.matchFinaĵojOf(lastVorto)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Trovas la finitajn verboj kaj aldonas la funkcion {@code Funkcio.ĈEFVERBO}.
	 */
	private void setĈefverbo() {
		for(Frazero frazero : frazeroj) {
			Vorto lastVorto = frazero.getLastVortoWithSkip();
			if(lastVorto.checkTrajto(Trajto.VERBO) && !lastVorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
				frazero.setFunkcio(Funkcio.ĈEFVERBO);
			}
		}
	}
	
	/**
	 * Determinas la funkcion laŭ la donita trajto de unu el la vortoj el la frazero.
	 * @param getVortoToTest la funkcio, kiu ekstraktas la testotan vorton el la frazero
	 * @param trajto la trajton, kiun tiu vorto havu
	 * @param funkcio la funkcio, kiun la frazero havu
	 */
	private void setFunkcioFromTrajto(Function<Frazero, Vorto> getVortoToTest, Trajto trajto, Funkcio funkcio) {
		frazeroj.stream()
			.filter(f -> getVortoToTest.apply(f).checkTrajto(trajto))
			.forEach(f -> f.setFunkcio(funkcio));
	}
	
	/**
	 * Determinas tion, kiu frazero estas la subjekto kaj kiu estas predikativo, se ekzistas.
	 */
	private void setSubjektoAndPredikativo() {
		for(Frazero frazero : frazeroj) {
			if(frazero.getFunkcio() == null && !frazero.hasAkuzativo()) {
				if(findByFunkcio(Funkcio.SUBJEKTO) == null) {
					frazero.setFunkcio(Funkcio.SUBJEKTO);
				} else {
					if(!frazero.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO)) {
						frazero.setFunkcio(Funkcio.PREDIKATIVO);
					}
				}
			}
		}
	}
	
	/**
	 * Determinas la objektojn de la frazo.
	 */
	private void setObjektoj() {
		for(Frazero frazero : frazeroj) {
			if(frazero.getFunkcio() == null && frazero.hasAkuzativo()) {
				frazero.setFunkcio(Funkcio.OBJEKTO);
			}
		}
	}
	
	/**
	 * Determinas la i-komplementoj en la frazo.
	 */
	private void setInfinitivoj() {
		frazeroj.stream()
			.filter(f -> f.getFunkcio() == null)
			.filter(f -> f.getVortoj().get(0).checkTrajto(Trajto.VERBO_INFINITIVO))
			.forEach(f -> f.setFunkcio(Funkcio.I_KOMPLEMENTO));
	}
	
	/**
	 * Asignas al ĉiu frazeroj la rilatajn frazeroj laŭ la funkcio rilate al tiu unua frazero.
	 */
	private void addRelatedFrazeroj() {
		for(Frazero frazero : frazeroj) {
			Funkcio funkcio = frazero.getFunkcio();
			
			if(funkcio != null) {
				for(Funkcio relatedFunkcio : funkcio.getRelatedFunkcioj()) {
					Frazero[] frazerojWithRelatedFunkcio = findAllByFunkcio(relatedFunkcio);
					if(frazerojWithRelatedFunkcio != null) {
						frazero.addRelatedFrazeroj(frazerojWithRelatedFunkcio);
					}
				}
			}
		}
	}
	
	/**
	 * Trovas la unuan frazeron, kiu havas la indikitan funkcion.
	 * @param funkcio la dezirata funkcio
	 * @return la unuan frazeron kun la dezirata funkcio
	 */
	public Frazero findByFunkcio(Funkcio funkcio) {
		for(Frazero frazero : frazeroj) {
			if(frazero.getFunkcio() == funkcio) {
				return frazero;
			}
		}
		return null;
	}
	
	/**
	 * Trovas ĉiujn frazerojn, kiuj havas la indikitan funkcion.
	 * @param funkcio la dezirata funkcio
	 * @return listo kun ĉiuj frazeroj kun la dezirata funkcio
	 */
	public Frazero[] findAllByFunkcio(Funkcio funkcio) {
		return frazeroj.stream().filter(f -> f.getFunkcio() == funkcio).toArray(Frazero[]::new);
	}
	
	/**
	 * Trovas la unuan frazeron, kies teksto kongruas ekzakte kun la donita teksto
	 * @param string teksto por serĉi tiun frazeron
	 * @return la unuan frazeron kun la donita teksto
	 */
	public Frazero findByString(String string) {
		for(Frazero frazero : frazeroj) {
			if(string.equals(frazero.toString())) {
				return frazero;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.join(" ", Arrays.asList(vortoj).stream().map(v -> v.toString()).toArray(String[]::new));
	}
	
	public Vorto[] getVortoj() {
		return vortoj.clone();
	}
	
	public List<Frazero> getFrazeroj() {
		return new ArrayList<>(frazeroj);
	}
}
