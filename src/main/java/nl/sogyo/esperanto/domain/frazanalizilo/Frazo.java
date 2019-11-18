package nl.sogyo.esperanto.domain.frazanalizilo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import nl.sogyo.esperanto.API.Funkcio;
import nl.sogyo.esperanto.API.Trajto;
import nl.sogyo.esperanto.domain.vortanalizilo.Analizaĵo;
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
		
		List<Vorto> frazerVortoj = new ArrayList<>();
		Iterator<Vorto> iterator = Arrays.asList(vortoj).iterator();
		/*
		 * Ni bezonas tian konstruon, ĉar ni bezonas la sekvan vorton por analizi la lastan grupon de vortoj.
		 * Do post la lasta vorto el ‘vortoj’ ni devas fari kroman iteracion.
		 */
		do {
			Vorto vorto = (iterator.hasNext() ? iterator.next() : null);
			System.out.println(vorto);
			
			if(shouldGoToNextFrazero(frazerVortoj, vorto)) {
				System.out.println("TO NEXT FRAZERO");
				Frazero frazero = new Frazero(determineFunkcio(frazerVortoj), frazerVortoj.toArray(Vorto[]::new));
				frazeroj.add(frazero);
				System.out.println("DETERMINED: " + frazero + " is " + frazero.getFunkcio());
				frazerVortoj.clear();
			}
			
			frazerVortoj.add(vorto);
			
			if(vorto == null) {
				break;
			}
		} while(true);
	}
	
	/**
	 * Determinas de la lasta grupo de vortoj kaj la nuna vorto, ĉu ni kreu frazeron el la lasta grupo aŭ ne
	 * @param frazerVortoj grupo da vortoj, kiuj potenciale konsistigas frazeron
	 * @param currentVorto la nuna vorto, kiu tuj sekvas frazerVortojn
	 * @return ĉu ni kreu frazeron el la frazerVortoj aŭ aldonu la nuna vorto al la frazerVortoj
	 */
	private boolean shouldGoToNextFrazero(List<Vorto> frazerVortoj, Vorto currentVorto) {
		if(currentVorto == null) {
			return true;
		}
		if(!currentVorto.matchFinaĵojOf(frazerVortoj.toArray(Vorto[]::new))) {
			return true;
		}
		if(currentVorto.checkTrajto(Trajto.PREPOZICIO)) {
			return true;
		}
		if(!frazerVortoj.isEmpty() && frazerVortoj.get(frazerVortoj.size() - 1).checkTrajto(Trajto.PREPOZICIO)) {
			return true;
		}
		return false;
	}
	
	/**
	 * La funkcio, kiun certe havu la sekva grupo da vortoj (ekzemple post prepozicio)
	 */
	private Funkcio nextFunkcio;
	
	/**
	 * Determinas la funkcion de la listo de vortoj
	 * @param frazerVortoj
	 * @return la funkcion de la frazerVortoj
	 */
	private Funkcio determineFunkcio(List<Vorto> frazerVortoj) {
		if(nextFunkcio != null) {
			Funkcio f = nextFunkcio;
			nextFunkcio = null;
			return f;
		}
		
		if(frazerVortoj.size() == 1) {
			Vorto vorto = frazerVortoj.get(0);
			if(vorto.checkTrajto(Trajto.VERBO) && !vorto.checkTrajto(Trajto.VERBO_INFINITIVO)) {
				return Funkcio.ĈEFVERBO;
			} else if(vorto.checkTrajto(Trajto.PREPOZICIO)) {
				nextFunkcio = Funkcio.PREPOZICIA_KOMPLEMENTO;
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
