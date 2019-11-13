package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Trajto;

class AnalizaĵoTest {

	@Test
	public void malplenaAnalizaĵoEstasNeValida() {
		Analizaĵo analizaĵo = new Analizaĵo();
		
		assertFalse(analizaĵo.estasValida());
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
	
	@Test
	public void ŝuojEstasPlurala() {
		Vorto ŝuoj = new Vorto("ŝuoj");
		
		Analizaĵo ŝu_o_j = ŝuoj.preniAnalizaĵonLaŭDividaĈeno("ŝu|o|j");
		
		assertTrue(ŝu_o_j.kontroliTrajton(Trajto.PLURALO));
	}
	
	@Test
	public void ŝuonHavasAkuzativon() {
		Vorto ŝuon = new Vorto("ŝuon");
		
		Analizaĵo ŝu_o_n = ŝuon.preniAnalizaĵonLaŭDividaĈeno("ŝu|o|n");
		
		assertTrue(ŝu_o_n.kontroliTrajton(Trajto.AKUZATIVO));
	}
	
	@Test
	public void ŝuojnEstasSubstantivoKajPluralaKajHavasAkuzativon() {
		Vorto ŝuojn = new Vorto("ŝuojn");
		
		Analizaĵo ŝu_o_j_n = ŝuojn.preniAnalizaĵonLaŭDividaĈeno("ŝu|o|j|n");
		
		assertTrue(ŝu_o_j_n.kontroliTrajton(Trajto.SUBSTANTIVO));
		assertTrue(ŝu_o_j_n.kontroliTrajton(Trajto.PLURALO));
		assertTrue(ŝu_o_j_n.kontroliTrajton(Trajto.AKUZATIVO));
	}
	
	@Test
	public void voliEstasInfinitivo() {
		Vorto voli = new Vorto("voli");
		
		Analizaĵo vol_i = voli.preniAnalizaĵonLaŭDividaĈeno("vol|i");
		
		assertTrue(vol_i.kontroliTrajton(Trajto.VERBO_INFINITIVO));
		assertTrue(vol_i.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void volisEstasPreterito() {
		Vorto volis = new Vorto("volis");
		
		Analizaĵo vol_is = volis.preniAnalizaĵonLaŭDividaĈeno("vol|is");
		
		assertTrue(vol_is.kontroliTrajton(Trajto.VERBO_PRETERITO));
		assertTrue(vol_is.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void volasEstasPreterito() {
		Vorto volas = new Vorto("volas");
		
		Analizaĵo vol_as = volas.preniAnalizaĵonLaŭDividaĈeno("vol|as");
		
		assertTrue(vol_as.kontroliTrajton(Trajto.VERBO_PREZENCO));
		assertTrue(vol_as.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void volosEstasFuturo() {
		Vorto volos = new Vorto("volos");
		
		Analizaĵo vol_os = volos.preniAnalizaĵonLaŭDividaĈeno("vol|os");
		
		assertTrue(vol_os.kontroliTrajton(Trajto.VERBO_FUTURO));
		assertTrue(vol_os.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void volusEstasKondicionalo() {
		Vorto volus = new Vorto("volus");
		
		Analizaĵo vol_us = volus.preniAnalizaĵonLaŭDividaĈeno("vol|us");
		
		assertTrue(vol_us.kontroliTrajton(Trajto.VERBO_KONDICIONALO));
		assertTrue(vol_us.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void voluEstasVolitivo() {
		Vorto volu = new Vorto("volu");
		
		Analizaĵo vol_u = volu.preniAnalizaĵonLaŭDividaĈeno("vol|u");
		
		assertTrue(vol_u.kontroliTrajton(Trajto.VERBO_VOLITIVO));
		assertTrue(vol_u.kontroliTrajton(Trajto.VERBO));
	}
	
	@Test
	public void laEstasArtikolo() {
		Vorto la = new Vorto("la");
		
		Analizaĵo la_ = la.preniAnalizaĵonLaŭDividaĈeno("la");
		
		assertTrue(la_.kontroliTrajton(Trajto.ARTIKOLO));
	}
	
	@Test
	public void laNeEstuRekonataEnBola() {
		Vorto bola = new Vorto("bola");
		
		Analizaĵo bo_la = bola.preniAnalizaĵonLaŭDividaĈeno("bo|la");
		
		assertNull(bo_la);
	}
	
	@Test
	public void treEstasAdverbo() {
		Vorto tre = new Vorto("tre");
		
		Analizaĵo tre_ = tre.preniAnalizaĵonLaŭDividaĈeno("tre");
		
		assertTrue(tre_.kontroliTrajton(Trajto.ADVERBO));
	}
	
	@Test
	public void beleEstasAdverbo() {
		Vorto bele = new Vorto("bele");
		
		Analizaĵo bel_e = bele.preniAnalizaĵonLaŭDividaĈeno("bel|e");
		
		assertTrue(bel_e.kontroliTrajton(Trajto.ADVERBO));
	}
	
	@Test
	public void antaŭenEstasAdverboKajHavasAkuzativon() {
		Vorto antaŭen = new Vorto("antaŭen");
		
		Analizaĵo antaŭ_e_n = antaŭen.preniAnalizaĵonLaŭDividaĈeno("antaŭ|e|n");
		
		assertTrue(antaŭ_e_n.kontroliTrajton(Trajto.ADVERBO));
		assertTrue(antaŭ_e_n.kontroliTrajton(Trajto.AKUZATIVO));
	}
	
	@Test
	public void bopatrinoHavasNurUnuAnalizaĵon() {
		Vorto bopatrino = new Vorto("bopatrino");
		
		assertEquals(1, bopatrino.getEblajAnalizaĵoj().size());
	}
	
	@Test
	public void heEstasInterjekcio() {
		Vorto he = new Vorto("he");
		
		Analizaĵo he_ = he.preniAnalizaĵonLaŭDividaĈeno("he");
		
		assertTrue(he_.kontroliTrajton(Trajto.INTERJEKCIO));
	}
	
	@Test
	public void keEstasKonjunkcio() {
		Vorto ke = new Vorto("ke");
		
		Analizaĵo ke_ = ke.preniAnalizaĵonLaŭDividaĈeno("ke");
		
		assertTrue(ke_.kontroliTrajton(Trajto.KONJUNKCIO));
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		Vorto ĉia = new Vorto("ĉia");
		
		Analizaĵo ĉia_ = ĉia.preniAnalizaĵonLaŭDividaĈeno("ĉia");
		
		assertTrue(ĉia_.kontroliTrajton(Trajto.KORELATIVO));
	}
	
	@Test
	public void nenioEstasKorelativo() {
		Vorto nenio = new Vorto("nenio");
		
		Analizaĵo neni_o = nenio.preniAnalizaĵonLaŭDividaĈeno("neni|o");
		
		assertTrue(neni_o.kontroliTrajton(Trajto.KORELATIVO));
	}
	
	@Test
	public void kvarEstasNumeralo() {
		Vorto kvar = new Vorto("kvar");
		
		Analizaĵo kvar_ = kvar.preniAnalizaĵonLaŭDividaĈeno("kvar");
		
		assertTrue(kvar_.kontroliTrajton(Trajto.NUMERALO));
	}
	
	@Test
	public void dudekEstasNumeralo() {
		Vorto dudek = new Vorto("dudek");
		
		Analizaĵo du_dek = dudek.preniAnalizaĵonLaŭDividaĈeno("du|dek");
		
		assertTrue(du_dek.kontroliTrajton(Trajto.NUMERALO));
	}
	
	@Test
	public void unuaNeEstasNumeralo() {
		Vorto unua = new Vorto("unua");
		
		Analizaĵo unu_a = unua.preniAnalizaĵonLaŭDividaĈeno("unu|a");
		
		assertFalse(unu_a.kontroliTrajton(Trajto.NUMERALO));
	}
	
	@Test
	public void postEstasPrepozicio() {
		Vorto post = new Vorto("post");
		
		Analizaĵo post_ = post.preniAnalizaĵonLaŭDividaĈeno("post");
		
		assertTrue(post_.kontroliTrajton(Trajto.PREPOZICIO));
	}
	
	@Test
	public void maltransEstasPrepozicio() {
		Vorto maltrans = new Vorto("maltrans");
		
		Analizaĵo mal_trans = maltrans.preniAnalizaĵonLaŭDividaĈeno("mal|trans");
		
		assertTrue(mal_trans.kontroliTrajton(Trajto.PREPOZICIO));
	}
	
	@Test
	public void liEstasPronomo() {
		Vorto li = new Vorto("li");
		
		Analizaĵo li_ = li.preniAnalizaĵonLaŭDividaĈeno("li");
		
		assertTrue(li_.kontroliTrajton(Trajto.PRONOMO));
	}
	
	@Test
	public void zzzEstasSonimito() {
		Vorto zzz = new Vorto("zzz");
		
		Analizaĵo zzz_ = zzz.preniAnalizaĵonLaŭDividaĈeno("zzz");
		
		assertTrue(zzz_.kontroliTrajton(Trajto.SONIMITO));
	}
}
