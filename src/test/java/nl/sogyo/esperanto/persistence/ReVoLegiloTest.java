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
	private static final String POST = LOKA_PADO + "post.xml";
	
	private static ReVoReader aŭtoLegilo, sciiLegilo, fumoLegilo, homoLegilo, krediLegilo, perLegilo, ĉiaLegilo, kajLegilo, keLegilo, aĉLegilo, reLegilo, laLegilo, aLegilo, liLegilo, a1Legilo, aŭLegilo, ahaLegilo, hodiaŭLegilo, eĉLegilo, holaLegilo, zzzLegilo, postLegilo;
	
	@BeforeAll
	public static void iniciato() {
		aŭtoLegilo = new ReVoReader(AŬTO);
		sciiLegilo = new ReVoReader(SCII);
		fumoLegilo = new ReVoReader(FUMO);
		homoLegilo = new ReVoReader(HOMO);
		krediLegilo = new ReVoReader(KREDI);
		perLegilo = new ReVoReader(PER);
		ĉiaLegilo = new ReVoReader(ĈIA);
		kajLegilo = new ReVoReader(KAJ);
		keLegilo = new ReVoReader(KE);
		aĉLegilo = new ReVoReader(AĈ);
		reLegilo = new ReVoReader(RE);
		laLegilo = new ReVoReader(LA);
		aLegilo = new ReVoReader(A);
		liLegilo = new ReVoReader(LI);
		a1Legilo = new ReVoReader(A1);
		aŭLegilo = new ReVoReader(AŬ);
		ahaLegilo = new ReVoReader(AHA);
		hodiaŭLegilo = new ReVoReader(HODIAŬ);
		eĉLegilo = new ReVoReader(EĈ);
		holaLegilo = new ReVoReader(HOLA);
		zzzLegilo = new ReVoReader(ZZZ);
		postLegilo = new ReVoReader(POST);
	}
	
	@Test
	public void aŭtoHavasĜustanRadikon() {
		ReVoEntry enigo = aŭtoLegilo.getEnigo();
		
		assertEquals("aŭt", enigo.getVortero());
	}
	
	@Test
	public void aŭtoEstasRadiko() {
		ReVoEntry enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(VorterSpeco.RADIKO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭtiEstasNetransitiva() {
		ReVoEntry enigo = aŭtoLegilo.getEnigo();
		
		assertEquals(Transitiveco.NETRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test
	public void legiloFunkciasPerDosiero() {
		ReVoReader legilo = new ReVoReader(new File(AŬTO));
		ReVoEntry enigo = legilo.getEnigo();
		
		assertEquals(aŭtoLegilo.getEnigo(), enigo);
	}
	
	@Test
	public void sciiEstasTransitiva() {
		ReVoEntry enigo = sciiLegilo.getEnigo();
		
		assertEquals(Transitiveco.TRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu trovi kaj [tr] kaj [ntr] donas ĝustan transitivecon
	public void fumiEstasKajTransitivaKajNetransitiva() {
		ReVoEntry enigo = fumoLegilo.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void homoTransitivecoEstasNedifinita() {
		ReVoEntry enigo = homoLegilo.getEnigo();
		
		assertEquals(Transitiveco.NEDIFINITA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu la indikilo [x] donas ĝustan transitivecon
	public void krediEstasKajTransitivaKajNeTransitiva() {
		ReVoEntry enigo = krediLegilo.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void perEstasPrepozicio() {
		ReVoEntry enigo = perLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		ReVoEntry enigo = ĉiaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KORELATIVO, enigo.getVorterSpeco());
	}
	
	@Test
	public void kajEstasKonjunkcio() {
		ReVoEntry enigo = kajLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void keEstasKonjunkcio() {
		ReVoEntry enigo = keLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aĉEstasSufikso() {
		ReVoEntry enigo = aĉLegilo.getEnigo();
		
		assertEquals(VorterSpeco.SUFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void reEstasPrefikso() {
		ReVoEntry enigo = reLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PREFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void laEstasArtikolo() {
		ReVoEntry enigo = laLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ARTIKOLO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aEstasFinaĵo() {
		ReVoEntry enigo = aLegilo.getEnigo();
		
		assertEquals(VorterSpeco.FINAĴO, enigo.getVorterSpeco());
	}
	
	@Test
	public void liEstasPronomo() {
		ReVoEntry enigo = liLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PRONOMO, enigo.getVorterSpeco());
	}
	
	@Test
	public void a1EstasLitero() {
		ReVoEntry enigo = a1Legilo.getEnigo();
		
		assertEquals(VorterSpeco.LITERO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭEstasKonjunkcioNeAŭVorto() {
		ReVoEntry enigo = aŭLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ahaEstasInterjekcio() {
		ReVoEntry enigo = ahaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void hodiaŭEstasAdverbo() {
		ReVoEntry enigo = hodiaŭLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void eĉEstasAdverbo() {
		ReVoEntry enigo = eĉLegilo.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void holaEstasInterjekcio() {
		ReVoEntry enigo = holaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void zzzEstasSonimito() {
		ReVoEntry enigo = zzzLegilo.getEnigo();
		
		assertEquals(VorterSpeco.SONIMITO, enigo.getVorterSpeco());
	}
	
	@Test
	public void postEstasPrepozicio() {
		ReVoEntry enigo = postLegilo.getEnigo();
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
}
