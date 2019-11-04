package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

class ReVoLegiloTest {
	
	private static final File dosiero = new File("src/test/resources/xml/auxt.xml");
	
	@Test
	public void legiVortonAŭtoHavasĜustanRadikon() {
		ReVoEnigo enigo = (new ReVoLegilo(dosiero)).legiDosieron();
		
		System.out.println(enigo);
		
		assertEquals("aŭt", enigo.getVortero());
	}
}
