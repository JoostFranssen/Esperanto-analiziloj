package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
class VortoTest {

	@Test
	public void laHasOneAnalizaĵo() {
		Vorto la = new Vorto("la");
		
		assertEquals(1, la.getPossibleAnalizaĵoj().size());
	}
	
	@Test
	public void belojnHasOneAnalizaĵo() {
		Vorto belojn = new Vorto("belojn");
		
		assertEquals(1, belojn.getPossibleAnalizaĵoj().size());
	}
	
	@Test
	public void getAnalizaĵoByStringTest() {
		Vorto batato = new Vorto("batato");
		
		Analizaĵo bat_at_o = batato.getAnalizaĵoByString("bat|at|o");
		List<Vortero> vorteroj = bat_at_o.getVorteroj();
		
		assertEquals("bat", vorteroj.get(0).getVortero());
		assertEquals("at", vorteroj.get(1).getVortero());
		assertEquals("o", vorteroj.get(2).getVortero());
	}
}
