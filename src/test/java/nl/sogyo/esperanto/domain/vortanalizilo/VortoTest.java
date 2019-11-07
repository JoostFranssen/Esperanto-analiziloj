package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Trajto;

class VortoTest {

	@Test
	public void laEstasArtikolo() {
		Vorto la = new Vorto("la");
		la.analiziĝi();

		
	}
}
