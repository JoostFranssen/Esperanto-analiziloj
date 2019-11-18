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
}
