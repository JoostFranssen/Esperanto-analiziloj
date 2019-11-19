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
		IntStream.range(0, vortoStrings.length).parallel().forEach(i -> {
			vortoj[i] = new Vorto(vortoStrings[i]);
		});
		
		analyze();
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
		System.out.println("=".repeat(10));
		do {
			Vorto vorto = (iterator.hasNext() ? iterator.next() : null);
			
			if(vorto != null) {
				System.out.println(vorto.getVorto());
			} else {
				System.out.println("null");
			}
			
			if(shouldGoToNextFrazero(frazero, vorto)) {
				Funkcio funkcio = null;
				
				System.out.println("--" + frazero + ": " + frazero.getFunkcio());
				
				if(frazero.getFunkcio() == null) {
    				funkcio = determineFunkcio(frazero);
    				frazero.setFunkcio(funkcio);
				}
				
				System.out.println("--" + funkcio);
				
				frazeroj.add(frazero);
				
				frazero = new Frazero();
				frazero.setFunkcio(nextFunkcio(funkcio));
				
				System.out.println("--" + frazero.getFunkcio());
			}
			
			if(vorto == null) {
				break;
			}
			
			frazero.addVorto(vorto);
			
			System.out.println("-".repeat(10));
		} while(true);
	}
	
	/**
	 * Determinas de la lasta grupo de vortoj kaj la nuna vorto, ĉu ni kreu frazeron el la lasta grupo aŭ ne
	 * @param frazero la kreata frazero
	 * @param currentVorto la nuna vorto, kiu tuj sekvas frazerVortojn
	 * @return ĉu ni kreu frazeron el la frazerVortoj aŭ aldonu la nuna vorto al la frazerVortoj
	 */
	private boolean shouldGoToNextFrazero(Frazero frazero, Vorto currentVorto) {
		System.out.println("Next? " + frazero + ": " + frazero.getFunkcio() + " | " + (currentVorto != null ? currentVorto.getVorto() : "null"));
		List<Vorto> frazerVortoj = frazero.getVortoj();
		if(frazerVortoj.isEmpty()) {
			return false;
		}
		
		if(currentVorto == null) { //se ni estas ĉe la fino de la frazo
			return true;
		}
		if(!currentVorto.matchFinaĵojOf(frazerVortoj.toArray(Vorto[]::new))) {
			return true;
		}
		Vorto lastVorto = frazerVortoj.get(frazerVortoj.size() - 1);
		if(currentVorto.checkTrajto(Trajto.PREPOZICIO) || lastVorto.checkTrajto(Trajto.PREPOZICIO)) { //prepozicio ĉiam staras sola
			return true;
		}
		if(currentVorto.checkTrajto(Trajto.PRONOMO) || lastVorto.checkTrajto(Trajto.PRONOMO)) { //pronomo ĉiam komencas novan frazeron
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
		
		if(frazerVortoj.size() == 1) {
			Vorto vorto = frazerVortoj.get(0);
			if(vorto.checkTrajto(Trajto.VERBO) && !vorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
				return Funkcio.ĈEFVERBO;
			} else if(vorto.checkTrajto(Trajto.PREPOZICIO)) {
				return Funkcio.PREPOZICIO;
			}
		}
		
		if(Vorto.checkTrajtoForAny(frazerVortoj, Trajto.AKUZATIVO)) {
			if(findByFunkcio(Funkcio.OBJEKTO) == null) {
				return Funkcio.OBJEKTO;
			} else {
				return null;
			}
		} else if(findByFunkcio(Funkcio.SUBJEKTO) == null) {
			return Funkcio.SUBJEKTO;
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
			case PREPOZICIO:
				return Funkcio.PREPOZICIA_KOMPLEMENTO;
			default:
				return null;
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
