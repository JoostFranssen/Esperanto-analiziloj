package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

class ReVoLegiloTest {
	
	private static final String LOKA_PADO = "src/test/resources/ReVo/";
	private static final String AŬTO = LOKA_PADO + "auxt.xml";
	private static final String SCII = LOKA_PADO + "sci.xml";
	private static final String FUMO = LOKA_PADO + "fum.xml";
	private static final String HOMO = LOKA_PADO + "hom.xml";
	private static final String KREDI = LOKA_PADO + "kred.xml";
	private static final String PER = LOKA_PADO + "per.xml";
	private static final String ĈIA = LOKA_PADO + "cxia.xml";
	private static final String KAJ = LOKA_PADO + "kaj.xml";
	private static final String KE = LOKA_PADO + "ke.xml";
	private static final String AĈ = LOKA_PADO + "acx.xml";
	private static final String EK = LOKA_PADO + "ek.xml";
	private static final String LA = LOKA_PADO + "la.xml";
	private static final String A = LOKA_PADO + "a.xml";
	private static final String LI = LOKA_PADO + "li.xml";
	
	private static ReVoLegilo aŭtoLegilo, sciiLegilo, fumoLegilo, homoLegilo, krediLegilo, perLegilo, ĉiaLegilo, kajLegilo, keLegilo, aĉLegilo, ekLegilo, laLegilo, aLegilo, liLegilo;
	
	@BeforeAll
	public static void iniciato() {
		aŭtoLegilo = new ReVoLegilo(AŬTO);
		sciiLegilo = new ReVoLegilo(SCII);
		fumoLegilo = new ReVoLegilo(FUMO);
		homoLegilo = new ReVoLegilo(HOMO);
		krediLegilo = new ReVoLegilo(KREDI);
		perLegilo = new ReVoLegilo(PER);
		ĉiaLegilo = new ReVoLegilo(ĈIA);
		kajLegilo = new ReVoLegilo(KAJ);
		keLegilo = new ReVoLegilo(KE);
		aĉLegilo = new ReVoLegilo(AĈ);
		ekLegilo = new ReVoLegilo(EK);
		laLegilo = new ReVoLegilo(LA);
		aLegilo = new ReVoLegilo(A);
		liLegilo = new ReVoLegilo(LI);
	}
	
	@Test
	public void aŭtoHavasĜustanRadikon() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals("aŭt", enigo.getVortero());
	}
	
	@Test
	public void aŭtoEstasRadiko() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(VorterSpeco.RADIKO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭtiEstasNetransitiva() {
		ReVoEnigo enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(Transitiveco.NETRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test
	public void sciiEstasTransitiva() {
		ReVoEnigo enigo = sciiLegilo.getEnigo();
		
		assertEquals(Transitiveco.TRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu trovi kaj [tr] kaj [ntr] donas ĝustan transitivecon
	public void fumiEstasKajTransitivaKajNetransitiva() {
		ReVoEnigo enigo = fumoLegilo.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void homoTransitivecoEstasNedifinita() {
		ReVoEnigo enigo = homoLegilo.getEnigo();
		
		assertEquals(Transitiveco.NEDIFINITA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu la indikilo [x] donas ĝustan transitivecon
	public void krediEstasKajTransitivaKajNeTransitiva() {
		ReVoEnigo enigo = krediLegilo.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void perEstasPrepozicio() {
		ReVoEnigo enigo = perLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		ReVoEnigo enigo = ĉiaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KORELATIVO, enigo.getVorterSpeco());
	}
	
	@Test
	public void kajEstasKonjunkcio() {
		ReVoEnigo enigo = kajLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void keEstasKonjunkcio() {
		ReVoEnigo enigo = keLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aĉEstasSufikso() {
		ReVoEnigo enigo = aĉLegilo.getEnigo();
		
		assertEquals(VorterSpeco.SUFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ekEstasPreikso() {
		ReVoEnigo enigo = ekLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PREFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void laEstasArtikolo() {
		ReVoEnigo enigo = laLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ARTIKOLO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aEstasFinaĵo() {
		ReVoEnigo enigo = aLegilo.getEnigo();
		
		assertEquals(VorterSpeco.FINAĴO, enigo.getVorterSpeco());
	}
	
	@Test
	public void liEstasPronomo() {
		ReVoEnigo enigo = liLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PRONOMO, enigo.getVorterSpeco());
	}
}
