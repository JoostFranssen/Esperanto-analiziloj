package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

class ReVoLegiloTest {
	
	private static final String LOKALA_PADO = "src/test/resources/ReVo/";
	private static final String AŬTO = LOKALA_PADO + "auxt.xml";
	
	private static ReVoLegilo aŭtoLegilo;
	
	@BeforeAll
	public static void iniciato() {
		aŭtoLegilo = new ReVoLegilo(AŬTO);
	}
	
	@Test
	public void legiVortonAŭtoHavasĜustanRadikon() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals("aŭt", enigo.getVortero());
	}
	
	@Test
	public void legiVortonAŭtoEstasRadiko() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(VorterSpeco.RADIKO, enigo.getVorterSpeco());
	}
	
	@Test
	public void legiVortonAŭtoEstasNetransitiva() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(Transitiveco.NETRANSITIVA, enigo.getTransitiveco());
	}
}
