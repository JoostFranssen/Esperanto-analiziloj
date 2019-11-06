package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

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
	private static final String RE = LOKA_PADO + "re.xml";
	private static final String LA = LOKA_PADO + "la.xml";
	private static final String A = LOKA_PADO + "a.xml";
	private static final String LI = LOKA_PADO + "li.xml";
	private static final String A1 = LOKA_PADO + "a1.xml";
	private static final String AŬ = LOKA_PADO + "au.xml";
	private static final String AHA = LOKA_PADO + "aha.xml";
	private static final String HODIAŬ = LOKA_PADO + "hodiau.xml";
	private static final String EĈ = LOKA_PADO + "ecx.xml";
	private static final String HOLA = LOKA_PADO + "hola.xml";
	private static final String ZZZ = LOKA_PADO + "zzz.xml";
	
	private static ReVoLegilo aŭtoLegilo, sciiLegilo, fumoLegilo, homoLegilo, krediLegilo, perLegilo, ĉiaLegilo, kajLegilo, keLegilo, aĉLegilo, reLegilo, laLegilo, aLegilo, liLegilo, a1Legilo, aŭLegilo, ahaLegilo, hodiaŭLegilo, eĉLegilo, holaLegilo, zzzLegilo;
	
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
		reLegilo = new ReVoLegilo(RE);
		laLegilo = new ReVoLegilo(LA);
		aLegilo = new ReVoLegilo(A);
		liLegilo = new ReVoLegilo(LI);
		a1Legilo = new ReVoLegilo(A1);
		aŭLegilo = new ReVoLegilo(AŬ);
		ahaLegilo = new ReVoLegilo(AHA);
		hodiaŭLegilo = new ReVoLegilo(HODIAŬ);
		eĉLegilo = new ReVoLegilo(EĈ);
		holaLegilo = new ReVoLegilo(HOLA);
		zzzLegilo = new ReVoLegilo(ZZZ);
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
	public void legiloFunkciasPerDosiero() {
		ReVoLegilo legilo = new ReVoLegilo(new File(AŬTO));
		ReVoEnigo enigo = legilo.getEnigo();
		
		assertEquals(aŭtoLegilo.getEnigo(), enigo);
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
	public void reEstasPrefikso() {
		ReVoEnigo enigo = reLegilo.getEnigo();
		
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
	
	@Test
	public void a1EstasLitero() {
		ReVoEnigo enigo = a1Legilo.getEnigo();
		
		assertEquals(VorterSpeco.LITERO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭEstasKonjunkcioNeAŭVorto() {
		ReVoEnigo enigo = aŭLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ahaEstasInterjekcio() {
		ReVoEnigo enigo = ahaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void hodiaŭEstasAdverbo() {
		ReVoEnigo enigo = hodiaŭLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void eĉEstasAdverbo() {
		ReVoEnigo enigo = eĉLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void holaEstasInterjekcio() {
		ReVoEnigo enigo = holaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void zzzEstasSonimito() {
		ReVoEnigo enigo = zzzLegilo.getEnigo();
		
		assertEquals(VorterSpeco.SONIMITO, enigo.getVorterSpeco());
	}
}
