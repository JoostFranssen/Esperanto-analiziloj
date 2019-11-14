package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Trajto;

class AnalizaĵoTest {

	@Test
	public void malplenaAnalizaĵoEstasNeValida() {
		Analizaĵo analizaĵo = new Analizaĵo();
		
		assertFalse(analizaĵo.isValid());
	}
	
	@Test
	public void belaEstasAdjektivo() {
		Vorto bela = new Vorto("bela");
		
		Analizaĵo bel_a = bela.getAnalizaĵoByString("bel|a");
		
		assertTrue(bel_a.checkTrajto(Trajto.ADJEKTIVO));
	}
	
	@Test
	public void ĝojoEstasSubstantivo() {
		Vorto ĝojo = new Vorto("ĝojo");
		
		Analizaĵo ĝoj_o = ĝojo.getAnalizaĵoByString("ĝoj|o");
		
		assertTrue(ĝoj_o.checkTrajto(Trajto.SUBSTANTIVO));
	}
	
	@Test
	public void ŝuojEstasPlurala() {
		Vorto ŝuoj = new Vorto("ŝuoj");
		
		Analizaĵo ŝu_o_j = ŝuoj.getAnalizaĵoByString("ŝu|o|j");
		
		assertTrue(ŝu_o_j.checkTrajto(Trajto.PLURALO));
	}
	
	@Test
	public void ŝuonHavasAkuzativon() {
		Vorto ŝuon = new Vorto("ŝuon");
		
		Analizaĵo ŝu_o_n = ŝuon.getAnalizaĵoByString("ŝu|o|n");
		
		assertTrue(ŝu_o_n.checkTrajto(Trajto.AKUZATIVO));
	}
	
	@Test
	public void ŝuojnEstasSubstantivoKajPluralaKajHavasAkuzativon() {
		Vorto ŝuojn = new Vorto("ŝuojn");
		
		Analizaĵo ŝu_o_j_n = ŝuojn.getAnalizaĵoByString("ŝu|o|j|n");
		
		assertTrue(ŝu_o_j_n.checkTrajto(Trajto.SUBSTANTIVO));
		assertTrue(ŝu_o_j_n.checkTrajto(Trajto.PLURALO));
		assertTrue(ŝu_o_j_n.checkTrajto(Trajto.AKUZATIVO));
	}
	
	@Test
	public void voliEstasInfinitivo() {
		Vorto voli = new Vorto("voli");
		
		Analizaĵo vol_i = voli.getAnalizaĵoByString("vol|i");
		
		assertTrue(vol_i.checkTrajto(Trajto.VERBO_INFINITIVO));
		assertTrue(vol_i.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void volisEstasPreterito() {
		Vorto volis = new Vorto("volis");
		
		Analizaĵo vol_is = volis.getAnalizaĵoByString("vol|is");
		
		assertTrue(vol_is.checkTrajto(Trajto.VERBO_PRETERITO));
		assertTrue(vol_is.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void volasEstasPreterito() {
		Vorto volas = new Vorto("volas");
		
		Analizaĵo vol_as = volas.getAnalizaĵoByString("vol|as");
		
		assertTrue(vol_as.checkTrajto(Trajto.VERBO_PREZENCO));
		assertTrue(vol_as.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void volosEstasFuturo() {
		Vorto volos = new Vorto("volos");
		
		Analizaĵo vol_os = volos.getAnalizaĵoByString("vol|os");
		
		assertTrue(vol_os.checkTrajto(Trajto.VERBO_FUTURO));
		assertTrue(vol_os.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void volusEstasKondicionalo() {
		Vorto volus = new Vorto("volus");
		
		Analizaĵo vol_us = volus.getAnalizaĵoByString("vol|us");
		
		assertTrue(vol_us.checkTrajto(Trajto.VERBO_KONDICIONALO));
		assertTrue(vol_us.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void voluEstasVolitivo() {
		Vorto volu = new Vorto("volu");
		
		Analizaĵo vol_u = volu.getAnalizaĵoByString("vol|u");
		
		assertTrue(vol_u.checkTrajto(Trajto.VERBO_VOLITIVO));
		assertTrue(vol_u.checkTrajto(Trajto.VERBO));
	}
	
	@Test
	public void laEstasArtikolo() {
		Vorto la = new Vorto("la");
		
		Analizaĵo la_ = la.getAnalizaĵoByString("la");
		
		assertTrue(la_.checkTrajto(Trajto.ARTIKOLO));
	}
	
	@Test
	public void laNeEstuRekonataEnBola() {
		Vorto bola = new Vorto("bola");
		
		Analizaĵo bo_la = bola.getAnalizaĵoByString("bo|la");
		
		assertNull(bo_la);
	}
	
	@Test
	public void treEstasAdverbo() {
		Vorto tre = new Vorto("tre");
		
		Analizaĵo tre_ = tre.getAnalizaĵoByString("tre");
		
		assertTrue(tre_.checkTrajto(Trajto.ADVERBO));
	}
	
	@Test
	public void beleEstasAdverbo() {
		Vorto bele = new Vorto("bele");
		
		Analizaĵo bel_e = bele.getAnalizaĵoByString("bel|e");
		
		assertTrue(bel_e.checkTrajto(Trajto.ADVERBO));
	}
	
	@Test
	public void antaŭenEstasAdverboKajHavasAkuzativon() {
		Vorto antaŭen = new Vorto("antaŭen");
		
		Analizaĵo antaŭ_e_n = antaŭen.getAnalizaĵoByString("antaŭ|e|n");
		
		assertTrue(antaŭ_e_n.checkTrajto(Trajto.ADVERBO));
		assertTrue(antaŭ_e_n.checkTrajto(Trajto.AKUZATIVO));
	}
	
	@Test
	public void bopatrinoHavasNurUnuAnalizaĵon() {
		Vorto bopatrino = new Vorto("bopatrino");
		
		assertEquals(1, bopatrino.getPossibleAnalizaĵoj().size());
	}
	
	@Test
	public void heEstasInterjekcio() {
		Vorto he = new Vorto("he");
		
		Analizaĵo he_ = he.getAnalizaĵoByString("he");
		
		assertTrue(he_.checkTrajto(Trajto.INTERJEKCIO));
	}
	
	@Test
	public void keEstasKonjunkcio() {
		Vorto ke = new Vorto("ke");
		
		Analizaĵo ke_ = ke.getAnalizaĵoByString("ke");
		
		assertTrue(ke_.checkTrajto(Trajto.KONJUNKCIO));
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		Vorto ĉia = new Vorto("ĉia");
		
		Analizaĵo ĉia_ = ĉia.getAnalizaĵoByString("ĉia");
		
		assertTrue(ĉia_.checkTrajto(Trajto.KORELATIVO));
	}
	
	@Test
	public void nenioEstasKorelativo() {
		Vorto nenio = new Vorto("nenio");
		
		Analizaĵo neni_o = nenio.getAnalizaĵoByString("neni|o");
		
		assertTrue(neni_o.checkTrajto(Trajto.KORELATIVO));
	}
	
	@Test
	public void kvarEstasNumeralo() {
		Vorto kvar = new Vorto("kvar");
		
		Analizaĵo kvar_ = kvar.getAnalizaĵoByString("kvar");
		
		assertTrue(kvar_.checkTrajto(Trajto.NUMERALO));
	}
	
	@Test
	public void dudekEstasNumeralo() {
		Vorto dudek = new Vorto("dudek");
		
		Analizaĵo du_dek = dudek.getAnalizaĵoByString("du|dek");
		
		assertTrue(du_dek.checkTrajto(Trajto.NUMERALO));
	}
	
	@Test
	public void unuaNeEstasNumeralo() {
		Vorto unua = new Vorto("unua");
		
		Analizaĵo unu_a = unua.getAnalizaĵoByString("unu|a");
		
		assertFalse(unu_a.checkTrajto(Trajto.NUMERALO));
	}
	
	@Test
	public void postEstasPrepozicio() {
		Vorto post = new Vorto("post");
		
		Analizaĵo post_ = post.getAnalizaĵoByString("post");
		
		assertTrue(post_.checkTrajto(Trajto.PREPOZICIO));
	}
	
	@Test
	public void maltransEstasPrepozicio() {
		Vorto maltrans = new Vorto("maltrans");
		
		Analizaĵo mal_trans = maltrans.getAnalizaĵoByString("mal|trans");
		
		assertTrue(mal_trans.checkTrajto(Trajto.PREPOZICIO));
	}
	
	@Test
	public void liEstasPronomo() {
		Vorto li = new Vorto("li");
		
		Analizaĵo li_ = li.getAnalizaĵoByString("li");
		
		assertTrue(li_.checkTrajto(Trajto.PRONOMO));
	}
	
	@Test
	public void zzzEstasSonimito() {
		Vorto zzz = new Vorto("zzz");
		
		Analizaĵo zzz_ = zzz.getAnalizaĵoByString("zzz");
		
		assertTrue(zzz_.checkTrajto(Trajto.SONIMITO));
	}
	
	@Test
	public void sciiEstasTransitiva() {
		Vorto scii = new Vorto("scii");
		
		Analizaĵo sci_i = scii.getAnalizaĵoByString("sci|i");
		
		assertTrue(sci_i.checkTrajto(Trajto.VERBO_TRANSITIVA));
	}
	
	@Test
	public void boligiEstasTransitiva() {
		Vorto boligi = new Vorto("boligi");
		
		Analizaĵo bol_ig_i = boligi.getAnalizaĵoByString("bol|ig|i");
		
		assertTrue(bol_ig_i.checkTrajto(Trajto.VERBO_TRANSITIVA));
	}
	
	@Test
	public void respondiEstasTransitiva() {
		Vorto respondi = new Vorto("respondi");
		
		Analizaĵo respond_i = respondi.getAnalizaĵoByString("respond|i");
		
		assertTrue(respond_i.checkTrajto(Trajto.VERBO_TRANSITIVA));
	}
	
	@Test
	public void transiriEstasTransitiva() {
		Vorto transiri = new Vorto("transiri");
		
		Analizaĵo trans_ir_i = transiri.getAnalizaĵoByString("trans|ir|i");
		
		assertTrue(trans_ir_i.checkTrajto(Trajto.VERBO_TRANSITIVA));
	}
	
	@Test
	public void eniNeEstasTransitiva() {
		Vorto transi = new Vorto("transi");
		
		Analizaĵo trans_i = transi.getAnalizaĵoByString("trans|i");
		
		assertFalse(trans_i.checkTrajto(Trajto.VERBO_TRANSITIVA));
	}
	
	@Test
	public void iriEstasNetransitiva() {
		Vorto iri = new Vorto("iri");
		
		Analizaĵo ir_i = iri.getAnalizaĵoByString("ir|i");
		
		assertTrue(ir_i.checkTrajto(Trajto.VERBO_NETRANSITIVA));
	}
	
	@Test
	public void sciiĝiEstasNetransitiva() {
		Vorto sciiĝi = new Vorto("sciiĝi");
		
		Analizaĵo sci_iĝ_i = sciiĝi.getAnalizaĵoByString("sci|iĝ|i");
		
		assertTrue(sci_iĝ_i.checkTrajto(Trajto.VERBO_NETRANSITIVA));
	}
	
	@Test
	public void paŝigiNeEstasNetransitiva() {
		Vorto paŝigi = new Vorto("paŝigi");
		
		Analizaĵo paŝ_ig_i = paŝigi.getAnalizaĵoByString("paŝ|ig|i");
		
		assertFalse(paŝ_ig_i.checkTrajto(Trajto.VERBO_NETRANSITIVA));
	}
	
	@Test
	public void re_al_iĝ_iEstasMalpliAltaOlReal_iĝ_i() {
		Vorto realiĝi = new Vorto("realiĝi");
		
		Analizaĵo re_al_iĝ_i = realiĝi.getAnalizaĵoByString("re|al|iĝ|i");
		Analizaĵo real_iĝ_i = realiĝi.getAnalizaĵoByString("real|iĝ|i");
		Comparator<Analizaĵo> komparilo = Analizaĵo.getComparator();
		
		assertTrue(komparilo.compare(real_iĝ_i, re_al_iĝ_i) < 0);
	}
	
	@Test
	public void transiriNeEstasNetransitiva() {
		Vorto transiri = new Vorto("transiri");
		
		Analizaĵo trans_ir_i = transiri.getAnalizaĵoByString("trans|ir|i");
		
		assertFalse(trans_ir_i.checkTrajto(Trajto.VERBO_NETRANSITIVA));
	}
	
	@Test
	public void si_n_gard_aEstasRekonata() {
		Vorto singarda = new Vorto("singarda");
		
		Analizaĵo si_n_gard_a = singarda.getAnalizaĵoByString("si|n|gard|a");
		
		assertNotNull(si_n_gard_a);
	}
}
