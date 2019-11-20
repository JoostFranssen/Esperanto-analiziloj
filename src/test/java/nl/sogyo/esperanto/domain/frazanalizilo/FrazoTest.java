package nl.sogyo.esperanto.domain.frazanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Funkcio;

class FrazoTest {

	@Test
	public void pluvasIsĈefverbo() {
		Frazo pluvas = new Frazo("Pluvas.");
		
		Frazero ĉefverbo = pluvas.findByFunkcio(Funkcio.ĈEFVERBO);
		
		assertEquals("Pluvas", ĉefverbo.getVortoj().get(0).getVorto());
		assertEquals(Funkcio.ĈEFVERBO, ĉefverbo.getFunkcio());
	}
	
	@Test
	public void homoIsSubjekto() {
		Frazo frazo = new Frazo("Homo iras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Homo", subjekto.toString());
	}
	
	@Test
	public void EsperantonIsObjekto() {
		Frazo frazo = new Frazo("Mi parolas Esperanton.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("Esperanton", objekto.toString());
	}
	
	@Test
	public void perMiaAŭtoEstasPrepoziciaĵo() {
		Frazo frazo = new Frazo("Mi veturas per mia aŭto");
		
		Frazero komplemento = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		
		assertEquals("per mia aŭto", komplemento.toString());
	}
	
	@Test
	public void pronomoIsNotPutTogetherWithOtherWordsWithCongruentEnding() {
		Frazo frazo = new Frazo("Al domo mi iras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("mi", subjekto.toString());
	}
	
	@Test
	public void pronomoBelongsToPrepoziciaĵo() {
		Frazo frazo = new Frazo("Al mi homo paŝas.");
		
		Frazero prepoziciaĵo = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		
		assertEquals("Al mi", prepoziciaĵo.toString());
	}
	
	@Test
	public void adverboModifyingNominativaAdjektivoIsWithinTheSameFrazero() {
		Frazo frazo = new Frazo("Tre longa frazo skribiĝis.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Tre longa frazo", subjekto.toString());
	}
	
	@Test
	public void adverboModifyingAkuzativaAdjektivoIsWithinTheSameFrazero() {
		Frazo frazo = new Frazo("Mi skribas tre longan frazon.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("tre longan frazon", objekto.toString());
	}
	
	@Test
	public void adverboModifyingNextVerboIsASeparateFrazero() {
		Frazo frazo = new Frazo("Li rapide falis.");
		
		Frazero adverbo = frazo.findByFunkcio(Funkcio.ADVERBO);
		
		assertEquals("rapide", adverbo.toString());
	}
	
	@Test
	public void adverboModifyingPreviousVerboIsASeparateFrazero() {
		Frazo frazo = new Frazo("Ĝi falos rapide.");
		
		Frazero adverbo = frazo.findByFunkcio(Funkcio.ADVERBO);
		
		assertEquals("rapide", adverbo.toString());
	}
	
	@Test
	public void adverboModifyingAdverboAreOneFrazero() {
		Frazo frazo = new Frazo("Planto tre rapide kreskas");
		
		Frazero adverbo = frazo.findByFunkcio(Funkcio.ADVERBO);
		
		assertEquals("tre rapide", adverbo.toString());
	}
	
	@Test
	public void akuzativaAdverboIsAdverbo() {
		Frazo frazo = new Frazo("Vi ĵetas pilkon antaŭen.");
		
		Frazero	adverbo = frazo.findByFunkcio(Funkcio.ADVERBO);
		
		assertEquals("antaŭen", adverbo.toString());
	}
	
	@Test
	public void laIsPartOfTheSubject() {
		Frazo frazo = new Frazo("La viro iras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("La viro", subjekto.toString());
	}
	
	@Test
	public void laIsPartOfTheObject() {
		Frazo frazo = new Frazo("Ŝi ĵetu la pilkon.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("la pilkon", objekto.toString());
	}
	
	@Test
	public void laIsPartOfAPrepoziciaĵo() {
		Frazo frazo = new Frazo("Mi sendas literon al la homoj.");
		
		Frazero prepoziciaĵo = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		
		assertEquals("al la homoj", prepoziciaĵo.toString());
	}
	
	@Test
	public void laWithAdverbojAndAdjektivoj() {
		Frazo frazo = new Frazo("La rapide iranta homo hastas.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("La rapide iranta homo", subjekto.toString());
	}
	
	@Test
	public void estasHasPredikativo() {
		Frazo frazo = new Frazo("Ri estas bela.");
		
		Frazero predikativo = frazo.findByFunkcio(Funkcio.PREDIKATIVO);
		
		assertEquals("bela", predikativo.toString());
	}
	
	@Test
	public void opiniiHasPredikativo() {
		Frazo frazo = new Frazo("Li opinias ĝin facila.");
		
		Frazero predikativo = frazo.findByFunkcio(Funkcio.PREDIKATIVO);
		
		assertEquals("facila", predikativo.toString());
	}
	
	@Test
	public void taksiWithComplexPredikativo() {
		Frazo frazo = new Frazo("La homo taksas sian sugeston bonega ideo.");
		
		Frazero predikativo = frazo.findByFunkcio(Funkcio.PREDIKATIVO);
		
		assertEquals("bonega ideo", predikativo.toString());
	}
	
	@Test
	public void kajWithinSubjekto() {
		Frazo frazo = new Frazo("Mi kaj li paŝas.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Mi kaj li", subjekto.toString());
	}
	
	@Test
	public void ĉuIsSeparate() {
		Frazo frazo = new Frazo("Ĉu mi kaj li paŝas?");
		
		Frazero ĉu = frazo.findByString("Ĉu");
		
		assertNotNull(ĉu);
	}
	
	@Test
	public void ĉuIsNotSubjekto() {
		Frazo frazo = new Frazo("Ĉu mi kaj li paŝas?");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("mi kaj li", subjekto.toString());
	}
	
	@Test
	public void ĉefverboRefersToSubjekto() {
		Frazo frazo = new Frazo("Mi iras.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		
		assertTrue(ĉefverbo.getRelatedFrazeroj().stream().anyMatch(f -> f.getFunkcio() == Funkcio.SUBJEKTO));
	}
}
