package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Trajto;

class VortoTest {

	@Test
	public void laHavasUnuAnalizaĵon() {
		Vorto la = new Vorto("la");
		
		assertEquals(1, la.getEblajAnalizaĵoj().size());
	}
	
	@Test
	public void bulojnHavasUnuAnalizaĵon() {
		Vorto belojn = new Vorto("belojn");
		
		belojn.getEblajAnalizaĵoj().forEach(a -> System.out.println(a.getVorteroj()));
		
		assertEquals(2, belojn.getEblajAnalizaĵoj().size());
	}
}
