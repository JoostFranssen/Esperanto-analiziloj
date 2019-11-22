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
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertTrue(ĉefverbo.getRelatedFrazero(Funkcio.SUBJEKTO) == subjekto);
	}
	
	@Test
	public void ĉefverboRefersToObjekto() {
		Frazo frazo = new Frazo("Mi havas anason.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertTrue(ĉefverbo.getRelatedFrazero(Funkcio.OBJEKTO) == objekto);
	}
	
	@Test
	public void subjektoRefersToĈefverbo() {
		Frazo frazo = new Frazo("Mi iras.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertTrue(subjekto.getRelatedFrazero(Funkcio.ĈEFVERBO) == ĉefverbo);
	}
	
	@Test
	public void objektoRefersToĈefverbo() {
		Frazo frazo = new Frazo("Mi havas anason.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertTrue(objekto.getRelatedFrazero(Funkcio.ĈEFVERBO) == ĉefverbo);
	}
	
	@Test
	public void ĉiCombinesWithCorrelativeAfterĈefverbo() {
		Frazo frazo = new Frazo("Mi ŝatas ĉi tion.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("ĉi tion", objekto.toString());
	}
	
	@Test
	public void ĉiAfterKorelativoBelongsToObjekto() {
		Frazo frazo = new Frazo("Mi ŝatas tion ĉi.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("tion ĉi", objekto.toString());
	}
	
	@Test
	public void ajnAfterKorelativoBelongsToObject() {
		Frazo frazo = new Frazo("Mi ŝatas ion ajn.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("ion ajn", objekto.toString());
	}
	
	@Test
	public void ĉiBeforeKorelativoDoesNotInfluenceSubjektoCombination() {
		Frazo frazo = new Frazo("Ĉi tiu homo paŝas.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Ĉi tiu homo", subjekto.toString());
	}
	
	@Test
	public void ĉiAfterKorelativoDoesNotInfluenceSubjektoCombination() {
		Frazo frazo = new Frazo("Tiu ĉi homo paŝas.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Tiu ĉi homo", subjekto.toString());
	}
	
	@Test
	public void ajnAfterKorelativoDoesNotInfluenceSubjektoCombination() {
		Frazo frazo = new Frazo("Ĉiu ajn homo paŝas.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Ĉiu ajn homo", subjekto.toString());
	}
	
	@Test
	public void doublePrepozicioIsRecognized() {
		Frazo frazo = new Frazo("Mi iras ĝis sur la monto.");
		
		Frazero prepoziciaĵo = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		
		assertEquals("ĝis sur la monto", prepoziciaĵo.toString());
	}
	
	@Test
	public void infinitivoIsSubjekto() {
		Frazo frazo = new Frazo("Promeni estas bone.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Promeni", subjekto.toString());
	}
	
	@Test
	public void infinitivoIsIKomplemento() {
		Frazo frazo = new Frazo("Mi ŝatas promeni.");
		
		Frazero iKomplemento = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		
		assertEquals("promeni", iKomplemento.toString());
	}
	
	@Test
	public void fariTionNotObjektoOfĈefverbo() {
		Frazo frazo = new Frazo("Fari tion estas bone.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		
		assertFalse(ĉefverbo.hasRelatedFunkcio(Funkcio.OBJEKTO));
	}
	
	@Test
	public void objektoOfĈefverboButSubjektoOfInfinitivo() {
		Frazo frazo = new Frazo("Mi lasas vin iri.");
		
		Frazero vin = frazo.findByString("vin");
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		Frazero iKomplemento = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		
		assertEquals(vin, ĉefverbo.getRelatedFrazero(Funkcio.OBJEKTO));
		assertEquals(vin, iKomplemento.getRelatedFrazero(Funkcio.SUBJEKTO));
	}
	
	@Test
	public void objektoOfInfinitivoAfterĈefverbo() {
		Frazo frazo = new Frazo("Mi igas voki ŝin.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		Frazero infinitivo = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		
		assertEquals(objekto, infinitivo.getRelatedFrazero(Funkcio.OBJEKTO));
		assertEquals(infinitivo, ĉefverbo.getRelatedFrazero(Funkcio.OBJEKTO));
	}
	
	@Test
	public void subjektoIsNotSubjektoOfInfinitivo() {
		Frazo frazo = new Frazo("Mi igas voki ŝin.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		Frazero infinitivo = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		
		assertNotEquals(subjekto, infinitivo.getRelatedFrazero(Funkcio.SUBJEKTO));
	}
	
	@Test
	public void prepozicioBeforeInfinitivoRelatesToIt() {
		Frazo frazo = new Frazo("Mi faras tion por trovi ŝin.");
		
		Frazero prepoziciaĵo = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		Frazero infinitivo = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		
		assertEquals(infinitivo, prepoziciaĵo.getRelatedFrazero(Funkcio.I_KOMPLEMENTO));
	}
	
	@Test
	public void predikativoWithInfinitive() {
		Frazo frazo = new Frazo("Mi igas vin opinii tion bona.");
		
		Frazero ĉefverbo = frazo.findByFunkcio(Funkcio.ĈEFVERBO);
		Frazero infinitivo = frazo.findByFunkcio(Funkcio.I_KOMPLEMENTO);
		Frazero predikativo = frazo.findByFunkcio(Funkcio.PREDIKATIVO);
		
		assertFalse(ĉefverbo.hasRelatedFunkcio(Funkcio.PREDIKATIVO));
		assertEquals(predikativo, infinitivo.getRelatedFrazero(Funkcio.PREDIKATIVO));
	}
	
	@Test
	public void tielKielPairHaveTheSameFunkcio() {
		Frazo frazo = new Frazo("Ĝi estas tiel longa kiel larĝa.");
		
		Frazero[] predikativoj = frazo.findAllByFunkcio(Funkcio.PREDIKATIVO);
		
		assertEquals(2, predikativoj.length);
		assertEquals("tiel longa", predikativoj[0].toString());
		assertEquals("kiel larĝa", predikativoj[1].toString());
	}
	
	@Test
	public void pliOlIsSplitSeveralFrazeroj() {
		Frazo frazo = new Frazo("Ĝi estas pli longa ol larĝa.");
		
		Frazero[] predikativoj = frazo.findAllByFunkcio(Funkcio.PREDIKATIVO);
		
		assertEquals(2, predikativoj.length);
		assertEquals("pli longa", predikativoj[0].toString());
		assertEquals("larĝa", predikativoj[1].toString());
	}
	
	@Test
	public void olIsASeparateFrazero() {
		Frazo frazo = new Frazo("Ĝi estas pli longa ol larĝa.");
		
		Frazero ol = frazo.findByString("ol");
		
		assertNotNull(ol);
	}
	
	@Test
	public void numeralojGroupTogether() {
		Frazo frazo = new Frazo("Tie dek tri homoj staras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("dek tri homoj", subjekto.toString());
	}
	
	@Test
	public void numeraloSeparateFromVerbo() {
		Frazo frazo = new Frazo("Cent mil ludas tiun ludon.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("Cent mil", subjekto.toString());
	}
	
	@Test
	public void numeraloSeparateFromPrepozicio() {
		Frazo frazo = new Frazo("Ekzistas tri el tiaj homoj.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertEquals("tri", subjekto.toString());
	}
	
	@Test
	public void numeraloIsObjekto() {
		Frazo frazo = new Frazo("Mi havas tri el ili.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertEquals("tri", objekto.toString());
	}
	
	@Test
	public void daExpressionIsSplit() {
		Frazo frazo = new Frazo("Mi havas dekojn da libroj.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		Frazero da = frazo.findByFunkcio(Funkcio.PREPOZICIAĴO);
		
		assertEquals("dekojn", objekto.toString());
		assertEquals("da libroj", da.toString());
	}
}
