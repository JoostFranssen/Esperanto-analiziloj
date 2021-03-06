package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Vorto;

/**
 * Reprezentas frazon/
 * 
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
		 * Ni bezonas tian konstruon, ĉar ni bezonas la sekvan vorton por analizi la
		 * lastan grupon de vortoj. Do post la lasta vorto el ‘vortoj’ ni devas fari
		 * kroman iteracion.
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
		
		if(currentVorto == null) { // se ni estas ĉe la fino de la frazo
			return true;
		}
		
		Vorto lastVorto = frazerVortoj.get(frazerVortoj.size() - 1);
		
		//la vortoj ‘ajn’ kaj ‘ĉi’ modifas la korelativon, kiu staras antaŭ ĝi; ni vere volas rigardi ties funkcion
		if(lastVorto.equals(Vorto.AJN) || lastVorto.equals(Vorto.ĈI)) {
			if(frazerVortoj.size() >= 2) {
				lastVorto = frazerVortoj.get(frazerVortoj.size() - 2);
			}
		}
		
		// konjunkcio ĉiam kuntenu frazeron, ĉar ni ne konsideras subfrazojn; sed ‘ĉu’ kaj ‘ol’ ĉiam estu aparta
		if(lastVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return lastVorto.equals(Vorto.ĈU) || lastVorto.equals(Vorto.OL);
		}
		if(currentVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return currentVorto.equals(Vorto.ĈU) || currentVorto.equals(Vorto.OL);
		}
		
		// infinitivo ĉiam estas tute aparta
		if(lastVorto.checkTrajto(Trajto.VERBO_INFINITIVO) || currentVorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
			return true;
		}
		
		if(currentVorto.checkTrajto(Trajto.PREPOZICIO)) { // prepozicio ĉiam komencas novan frazeron, krom post alia prepozicio
			return !lastVorto.checkTrajto(Trajto.PREPOZICIO);
		}
		if(lastVorto.checkTrajto(Trajto.PREPOZICIO)) { // prepozicio ĉiam ligas al la sekva parto (escepte kun sekva infinitivo)
			return false;
		}
		
		//numeraloj grupiĝo kaj rolu kiel adjektivoj, sed disiĝu de verboj kaj prepozicioj
		if(currentVorto.checkTrajto(Trajto.NUMERALO)) {
			return !lastVorto.checkTrajto(Trajto.NUMERALO);
		}
		if(lastVorto.checkTrajto(Trajto.NUMERALO)) {
			return currentVorto.checkTrajto(Trajto.VERBO) || currentVorto.checkTrajto(Trajto.PREPOZICIO);
		}
		
		//ajn kaj ĉi estu ĉiam kunu kun korelativo
		if(currentVorto.equals(Vorto.AJN) || currentVorto.equals(Vorto.ĈI)) {
			return !lastVorto.checkTrajto(Trajto.KORELATIVO);
		}
		if(lastVorto.equals(Vorto.ĈI)) {
			return !currentVorto.checkTrajto(Trajto.KORELATIVO);
		}
		
		//korelativo en aliaj kazoj komencas novan frazeron
		if(currentVorto.checkTrajto(Trajto.KORELATIVO)) {
			return true;
		}
		
		if(lastVorto.checkTrajto(Trajto.ADVERBO)) {
			if(currentVorto.checkTrajto(Trajto.VERBO)) {
				return true;
			}
			if(currentVorto.checkTrajto(Trajto.ADVERBO) || currentVorto.checkTrajto(Trajto.ADJEKTIVO)) {
				return false;
			}
		}
		
		if(lastVorto.checkTrajto(Trajto.ARTIKOLO)) { // artikolo neniam povas stari sole
			return false;
		}
		if(currentVorto.checkTrajto(Trajto.ARTIKOLO)) { // artikolo ĉiam staras ĉe la komenco de frazero (krom post prepozicio)
			return true;
		}
		
		if(lastVorto.checkTrajto(Trajto.SUBSTANTIVO)) {
			if(currentVorto.checkTrajto(Trajto.ADVERBO) || currentVorto.checkTrajto(Trajto.SUBSTANTIVO)) {
				return true;
			}
		}
		
		if(currentVorto.checkTrajto(Trajto.PRONOMO) || lastVorto.checkTrajto(Trajto.PRONOMO)) { // pronomo ĉiam komencas novan frazeron
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
	
	private int getĈefverboIndex() {
		for(int i = 0; i < frazeroj.size(); i++) {
			if(frazeroj.get(i).getFunkcio() == Funkcio.ĈEFVERBO) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Determinas la funkcion laŭ la donita trajto de unu el la vortoj el la frazero.
	 * @param getVortoToTest la funkcio, kiu ekstraktas la testotan vorton el la frazero
	 * @param trajto la trajton, kiun tiu vorto havu
	 * @param funkcio la funkcio, kiun la frazero havu
	 */
	private void setFunkcioFromTrajto(Function<Frazero, Vorto> getVortoToTest, Trajto trajto, Funkcio funkcio) {
		frazeroj.stream().filter(f -> getVortoToTest.apply(f).checkTrajto(trajto)).forEach(f -> f.setFunkcio(funkcio));
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
					if(!frazero.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO) && !frazero.isNumeralo()) {
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
			if(frazero.getFunkcio() == null && (frazero.hasAkuzativo() || frazero.isNumeralo())) {
				frazero.setFunkcio(Funkcio.OBJEKTO);
			}
		}
	}
	
	/**
	 * Determinas la i-komplementoj en la frazo.
	 */
	private void setInfinitivoj() {
		frazeroj.stream().filter(f -> f.getFunkcio() == null)
				.filter(f -> f.getVortoj().get(0).checkTrajto(Trajto.VERBO_INFINITIVO))
				.forEach(f -> f.setFunkcio(Funkcio.I_KOMPLEMENTO));
	}
	
	/**
	 * Asignas al ĉiu frazeroj la rilatajn frazeroj laŭ la funkcio rilate al tiu unua frazero.
	 */
	private void addRelatedFrazeroj() {
		setRelatedFrazerojForĈefverbo();
		setRelatedFrazerojForInfinitivo();
		setRelatedFrazerojForAdverbo();
	}
	
	/**
	 * Asignas al la ĉefverbo la rilatajn subjekton, objekton, kaj predikativon.
	 * Reciproke, al tiuj frazeroj asigniĝas la ĉefverbo.
	 */
	private void setRelatedFrazerojForĈefverbo() {
		Frazero ĉefverbo = findByFunkcio(Funkcio.ĈEFVERBO);
		int ĉefverboIndex = getĈefverboIndex();
		
		// subjekto
		Frazero subjekto = findByFunkcio(Funkcio.SUBJEKTO);
		if(subjekto != null) {
			ĉefverbo.setRelatedFrazeroWithFunkcio(Funkcio.SUBJEKTO, subjekto);
			subjekto.setRelatedFrazeroWithFunkcio(Funkcio.ĈEFVERBO, ĉefverbo);
		}
		
		// objekto
		if(ĉefverbo.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_TRANSITIVA)) {
			Frazero objekto = null;
			FIND_OBJEKTO:
			{
				for(int i = ĉefverboIndex + 1; i < frazeroj.size(); i++) {
					Frazero objektoCandidate = frazeroj.get(i);
					if(objektoCandidate.getFunkcio() == Funkcio.OBJEKTO || objektoCandidate.getFunkcio() == Funkcio.I_KOMPLEMENTO) {
						objekto = objektoCandidate;
						break FIND_OBJEKTO;
					}
				}
				for(int i = ĉefverboIndex - 1; i >= 0; i--) {
					Frazero objektoCandidate = frazeroj.get(i);
					if(objektoCandidate.getFunkcio() == Funkcio.OBJEKTO || objektoCandidate.getFunkcio() == Funkcio.I_KOMPLEMENTO) {
						objekto = objektoCandidate;
						break FIND_OBJEKTO;
					}
				}
			}
			
			if(objekto != null) {
				ĉefverbo.setRelatedFrazeroWithFunkcio(Funkcio.OBJEKTO, objekto);
				objekto.setRelatedFrazeroWithFunkcio(Funkcio.ĈEFVERBO, ĉefverbo);
			}
		}
		
		// predikativo
		//ni supozas, ke la predikativo staras post la ĉefverbo
		Frazero predikativo = null;
		
		for(int i = ĉefverboIndex + 1; i < frazeroj.size(); i++) {
			Frazero predikativoCandidate = frazeroj.get(i);
			
			//se ni renkontas infinitivon, verŝajne tiu verbo havus predikativon aŭ predikativo ne estas
			if(predikativoCandidate.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO)) {
				break;
			}
			if(predikativoCandidate.getFunkcio() == Funkcio.PREDIKATIVO) {
				predikativo = predikativoCandidate;
				break;
			}
		}
		if(predikativo != null) {
			ĉefverbo.setRelatedFrazeroWithFunkcio(Funkcio.PREDIKATIVO, predikativo);
			predikativo.setRelatedFrazeroWithFunkcio(Funkcio.ĈEFVERBO, ĉefverbo);
		}
	}
	
	/**
	 * Asignas al ĉiuj infinitivo la rilatajn subjekton, objekton, kaj predikativon.
	 * Reciproke, al tiuj frazeroj asigniĝas la ĉefverbo. Supozas, ke la subjekto staras antaŭ ĝi.
	 * Krome, aldonas rilaton al prepozicio, kiu staras rekte antaŭ infinitivo.
	 */
	private void setRelatedFrazerojForInfinitivo() {
		IntStream.range(0, frazeroj.size())
			.filter(i -> frazeroj.get(i).getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO))
			.forEach(infinitivoIndex -> {
				Frazero infinitivo = frazeroj.get(infinitivoIndex);
				
				//subjekto
				for(int i = infinitivoIndex - 1; i >= 0; i--) {
					Frazero subjektoCandidate = frazeroj.get(i);
					//se la antaŭa vorto estas prepozicio, ne estas subjekto (ekz: por fari)
					//se la antaŭa vorto estas verbo, verŝajne ne estas subjekto, ĉar tiu verbo jam havus antaŭantan frazeron kiel subjekton
					if(subjektoCandidate.getVortoj().get(0).checkTrajto(Trajto.PREPOZICIO)) {
						subjektoCandidate.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, infinitivo); //rilatigi la prepozicion al la infinitivo
						break;
					}
					if(subjektoCandidate.getLastVortoWithSkip().checkTrajto(Trajto.VERBO)) {
						break;
					}
					
					if(subjektoCandidate.getFunkcio() == Funkcio.SUBJEKTO || subjektoCandidate.getFunkcio() == Funkcio.OBJEKTO) {
						infinitivo.setRelatedFrazeroWithFunkcio(Funkcio.SUBJEKTO, subjektoCandidate);
						subjektoCandidate.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, infinitivo);
						break;
					}
				}
				
				//objekto
				if(infinitivo.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_TRANSITIVA)) {
					Frazero objekto = null;
					FIND_OBJEKTO:
					{
						//estas pli verŝajne, ke la objekto staras post la infinitivo
						for(int i = infinitivoIndex + 1; i < frazeroj.size(); i++) {
							Frazero objektoCandidate = frazeroj.get(i);
							if(objektoCandidate.getFunkcio() == Funkcio.OBJEKTO && objektoCandidate != infinitivo.getRelatedFrazero(Funkcio.SUBJEKTO)) {
								objekto = objektoCandidate;
								break FIND_OBJEKTO;
							}
							//se ni trovas alian verbon, estas malverŝajne, ke la objekto staras post tiu
							if(objektoCandidate.getLastVortoWithSkip().checkTrajto(Trajto.VERBO)) {
								break;
							}
						}
						
						//se ĝi ne staras post la infinitivo, ni ankaŭ kontrolas la antaŭo
						for(int i = infinitivoIndex - 1; i >= 0; i--) {
							Frazero objektoCandidate = frazeroj.get(i);
							if(objektoCandidate.getFunkcio() == Funkcio.OBJEKTO && objektoCandidate != infinitivo.getRelatedFrazero(Funkcio.SUBJEKTO)) {
								objekto = objektoCandidate;
								break FIND_OBJEKTO;
							}
							if(objektoCandidate.getLastVortoWithSkip().checkTrajto(Trajto.VERBO)) {
								break;
							}
						}
					}
					
					if(objekto != null) {
						infinitivo.setRelatedFrazeroWithFunkcio(Funkcio.OBJEKTO, objekto);
						objekto.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, infinitivo);
					}
				}
				
				//predikativo
				//ni supozas, ke la predikativo staras post la infinitivo
				Frazero predikativo = null;
				
				for(Frazero predikativoCandidate : frazeroj) {
					if(predikativoCandidate.getFunkcio() == Funkcio.PREDIKATIVO && !predikativoCandidate.hasRelatedVerbaFunkcio()) {
						predikativo = predikativoCandidate;
						break;
					}
				}
				if(predikativo != null) {
					infinitivo.setRelatedFrazeroWithFunkcio(Funkcio.PREDIKATIVO, predikativo);
					predikativo.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, infinitivo);
				}
			});
	}
	
	private void setRelatedFrazerojForAdverbo() {
		IntStream.range(0, frazeroj.size())
			.filter(i -> frazeroj.get(i).getFunkcio() == Funkcio.ADVERBO)
			.filter(i -> !frazeroj.get(i).getLastVortoWithSkip().getVorto().endsWith("aŭ"))
			.forEach(adverboIndex -> {
				Frazero adverbo = frazeroj.get(adverboIndex);
				
				FIND_RELATION:
				{
					for(int i = adverboIndex + 1; i < frazeroj.size(); i++) {
						Frazero frazero = frazeroj.get(i);
						if(frazero.getFunkcio() == Funkcio.ĈEFVERBO) {
							adverbo.setRelatedFrazeroWithFunkcio(Funkcio.ĈEFVERBO, frazero);
							break FIND_RELATION;
						} else if(frazero.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO)) {
							adverbo.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, frazero);
							break FIND_RELATION;
						}
					}
					for(int i = adverboIndex - 1; i >= 0; i--) {
						Frazero frazero = frazeroj.get(i);
						if(frazero.getFunkcio() == Funkcio.ĈEFVERBO) {
							adverbo.setRelatedFrazeroWithFunkcio(Funkcio.ĈEFVERBO, frazero);
							break FIND_RELATION;
						} else if(frazero.getLastVortoWithSkip().checkTrajto(Trajto.VERBO_INFINITIVO)) {
							adverbo.setRelatedFrazeroWithFunkcio(Funkcio.I_KOMPLEMENTO, frazero);
							break FIND_RELATION;
						}
					}
				}
			});
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
