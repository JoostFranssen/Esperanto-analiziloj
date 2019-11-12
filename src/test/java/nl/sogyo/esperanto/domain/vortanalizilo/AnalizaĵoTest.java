package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Trajto;

class AnalizaĵoTest {

	@Test
	public void malplenaAnalizaĵoEstasValida() {
		Analizaĵo analizaĵo = new Analizaĵo();
		
		assertTrue(analizaĵo.estasValida());
	}
	
	@Test
	public void belaEstasAdjektivo() {
		Vorto bela = new Vorto("bela");
		
		Analizaĵo bel_a = bela.preniAnalizaĵonLaŭDividaĈeno("bel|a");
		
		assertTrue(bel_a.kontroliTrajton(Trajto.ADJEKTIVO));
	}
	
	@Test
	public void ĝojoEstasSubstantivo() {
		Vorto ĝojo = new Vorto("ĝojo");
		
		Analizaĵo ĝoj_o = ĝojo.preniAnalizaĵonLaŭDividaĈeno("ĝoj|o");
		
		assertTrue(ĝoj_o.kontroliTrajton(Trajto.SUBSTANTIVO));
	}
}
