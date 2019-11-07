package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AnalizaĵoTest {

	@Test
	public void malplenaAnalizaĵoEstasValida() {
		Analizaĵo analizaĵo = new Analizaĵo();
		
		assertTrue(analizaĵo.estasValida());
	}

}
