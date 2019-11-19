package nl.sogyo.esperanto.domain.frazanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Funkcio;

class FrazoTest {

	@Test
	public void pluvasIsĈefverbo() {
		Frazo pluvas = new Frazo("Pluvas.");
		
		Frazero ĉefverbo = pluvas.findByFunkcio(Funkcio.ĈEFVERBO);
		
		assertTrue("pluvas".equalsIgnoreCase(ĉefverbo.getVortoj().get(0).getVorto()));
		assertEquals(Funkcio.ĈEFVERBO, ĉefverbo.getFunkcio());
	}
	
	@Test
	public void homoIsSubjekto() {
		Frazo frazo = new Frazo("Homo iras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertTrue("homo".equalsIgnoreCase(subjekto.toString()));
	}
	
	@Test
	public void EsperantonIsObjekto() {
		Frazo frazo = new Frazo("Mi parolas Esperanton.");
		
		Frazero objekto = frazo.findByFunkcio(Funkcio.OBJEKTO);
		
		assertTrue("Esperanton".equalsIgnoreCase(objekto.toString()));
	}
	
	@Test
	public void perEstasPrepozicio() {
		Frazo frazo = new Frazo("Mi veturas per aŭto.");
		
		Frazero prepozicio = frazo.findByFunkcio(Funkcio.PREPOZICIO);
		
		assertTrue("per".equalsIgnoreCase(prepozicio.toString()));
	}
	
	@Test
	public void miaAŭtoEstasPrepoziciaKomplemento() {
		Frazo frazo = new Frazo("Mi veturas per mia aŭto");
		
		Frazero komplemento = frazo.findByFunkcio(Funkcio.PREPOZICIA_KOMPLEMENTO);
		
		assertTrue("mia aŭto".equalsIgnoreCase(komplemento.toString()));
	}
	
	@Test
	public void pronomoIsNotPutTogetherWithOtherWordsWithCongruentEnding() {
		Frazo frazo = new Frazo("Al domo mi iras.");
		
		Frazero subjekto = frazo.findByFunkcio(Funkcio.SUBJEKTO);
		
		assertTrue("mi".equalsIgnoreCase(subjekto.toString()));
	}
	
	@Test
	public void pronomoStandsOnItsOwn() {
		Frazo frazo = new Frazo("Al mi homo paŝas.");
		
		Frazero prepociziaKomplemento = frazo.findByFunkcio(Funkcio.PREPOZICIA_KOMPLEMENTO);
		
		assertTrue("mi".equalsIgnoreCase(prepociziaKomplemento.toString()));
	}
}
