package nl.sogyo.esperanto.domain.frazanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Funkcio;

class FrazoTest {

	@Test
	public void pluvasEstasLaĈefverbo() {
		Frazo pluvas = new Frazo("pluvas");
		
		Frazero ĉefverbo = pluvas.findĈefverbo();
		
		assertEquals("pluvas", ĉefverbo.getVortoj().get(0).getVorto());
		assertEquals(Funkcio.ĈEFVERBO, ĉefverbo.getFunkcio());
	}
}
