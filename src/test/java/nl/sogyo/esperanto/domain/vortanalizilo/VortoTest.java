package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

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
	
	@Test
	public void threeSingularNominativoMatch() {
		Vorto tia = new Vorto("tia");
		Vorto aĉa = new Vorto("aĉa");
		Vorto hundo = new Vorto("hundo");
		
		assertTrue(tia.matchFinaĵojOf(aĉa, hundo));
	}
	
	@Test
	public void threePluralAkuzativoMatch() {
		Vorto bonajn = new Vorto("ĉiajn");
		Vorto stultajn = new Vorto("stultajn");
		Vorto ideojn = new Vorto("ideojn");
		
		assertTrue(bonajn.matchFinaĵojOf(stultajn, ideojn));
	}
	
	@Test
	public void onePluralOneAkuzativoDoNotMatch() {
		Vorto bonan = new Vorto("bonan");
		Vorto ludoj = new Vorto("ludoj");
		
		assertFalse(bonan.matchFinaĵojOf(ludoj));
	}
}
