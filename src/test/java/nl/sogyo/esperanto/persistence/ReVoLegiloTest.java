package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.VorterSpeco;

class ReVoLegiloTest {
	
	private static final String LOKALA_PADO = "src/test/resources/ReVo/";
	private static final String AŬTO = LOKALA_PADO + "auxt.xml";
	
	@Test
	public void legiVortonAŭtoHavasĜustanRadikon() {
		ReVoEnigo enigo = (new ReVoLegilo(AŬTO)).legiDosieron();
		
		assertEquals("aŭt", enigo.getVortero());
	}
	
	@Test
	public void legiVortonAŭtoEstasRadiko() {
		ReVoEnigo enigi = (new ReVoLegilo(AŬTO)).legiDosieron();
		
		assertEquals(VorterSpeco.RADIKO, enigi.getVorterSpeco());
	}
}
