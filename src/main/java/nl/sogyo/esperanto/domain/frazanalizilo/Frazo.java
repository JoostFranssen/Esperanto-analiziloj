package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
		
		Iterator<Vorto> iterator = Arrays.asList(vortoj).iterator();
		/*
		 * Ni bezonas tian konstruon, ĉar ni bezonas la sekvan vorton por analizi la lastan grupon de vortoj.
		 * Do post la lasta vorto el ‘vortoj’ ni devas fari kroman iteracion.
		 */
		Frazero frazero = new Frazero();
		do {
			Vorto vorto = (iterator.hasNext() ? iterator.next() : null);
			
			if(shouldGoToNextFrazero(frazero, vorto)) {
				Funkcio funkcio = null;
				
				if(frazero.getFunkcio() == null) {
    				funkcio = determineFunkcio(frazero);
    				frazero.setFunkcio(funkcio);
				}
				
				frazeroj.add(frazero);
				
				frazero = new Frazero();
				frazero.setFunkcio(nextFunkcio(funkcio));
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
		
		//Konjunkcioj ĉiam kuntenu frazeron, ĉar ni ne konsideras subfrazojn; sed ‘ĉu’ ĉiam estu aparta
		if(lastVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return lastVorto.equals(Vorto.ĈU);
		}
		if(currentVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return currentVorto.equals(Vorto.ĈU);
		}
		
		if(currentVorto.checkTrajto(Trajto.PREPOZICIO)) { //prepozicio ĉiam komencas novan frazeron, krom ‘da’
			return !currentVorto.equals(Vorto.DA);
		}
		
		if(lastVorto.checkTrajto(Trajto.PREPOZICIO)) { //prepozicio ĉiam ligas al la sekva parto
			return false;
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
	 * Determinas la funkcion de la listo de vortoj
	 * @param frazerVortoj
	 * @return la funkcion de la frazerVortoj
	 */
	private Funkcio determineFunkcio(Frazero frazero) {
		List<Vorto> frazerVortoj = frazero.getVortoj();
		
		if(frazerVortoj.isEmpty()) {
			return null;
		}
		
		Vorto lastVorto = frazerVortoj.get(frazerVortoj.size() - 1);
		
		if(frazerVortoj.get(0).checkTrajto(Trajto.PREPOZICIO)) {
			return Funkcio.PREPOZICIAĴO;
		}
		if(lastVorto.checkTrajto(Trajto.VERBO) && !lastVorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
			return Funkcio.ĈEFVERBO;
		}
		if(lastVorto.checkTrajto(Trajto.ADVERBO)) {
			return Funkcio.ADVERBO;
		}
		if(lastVorto.checkTrajto(Trajto.KONJUNKCIO)) {
			return Funkcio.KONJUNKCIO;
		}
		
		if(Vorto.checkTrajtoForAny(frazerVortoj, Trajto.AKUZATIVO)) {
			if(findByFunkcio(Funkcio.OBJEKTO) == null) {
				return Funkcio.OBJEKTO;
			} else {
				return null;
			}
		} else if(findByFunkcio(Funkcio.SUBJEKTO) == null) {
			return Funkcio.SUBJEKTO;
		} else if(findByFunkcio(Funkcio.PREDIKATIVO) == null) {
			return Funkcio.PREDIKATIVO;
		}
		
		return null;
	}
	
	/**
	 * Determinas la funkcion de la sekva frazero, se tio eblas; ekzemple post prepozicio.
	 * @param currentFunkcio la funkcio de la nuna frazero
	 * @return la funkcion de la sekva frazero
	 */
	private Funkcio nextFunkcio(Funkcio currentFunkcio) {
		if(currentFunkcio == null) {
			return null;
		}
		switch(currentFunkcio) {
			default:
				return null;
		}
	}
	
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
