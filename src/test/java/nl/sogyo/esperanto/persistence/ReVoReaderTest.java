package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

class ReVoReaderTest {
	
	private static final String LOCAL_PATH = "src/test/resources/ReVo/";
	private static final String AŬTO = LOCAL_PATH + "auxt.xml";
	private static final String SCII = LOCAL_PATH + "sci.xml";
	private static final String FUMO = LOCAL_PATH + "fum.xml";
	private static final String HOMO = LOCAL_PATH + "hom.xml";
	private static final String KREDI = LOCAL_PATH + "kred.xml";
	private static final String PER = LOCAL_PATH + "per.xml";
	private static final String ĈIA = LOCAL_PATH + "cxia.xml";
	private static final String KAJ = LOCAL_PATH + "kaj.xml";
	private static final String KE = LOCAL_PATH + "ke.xml";
	private static final String AĈ = LOCAL_PATH + "acx.xml";
	private static final String RE = LOCAL_PATH + "re.xml";
	private static final String LA = LOCAL_PATH + "la.xml";
	private static final String A = LOCAL_PATH + "a.xml";
	private static final String LI = LOCAL_PATH + "li.xml";
	private static final String A1 = LOCAL_PATH + "a1.xml";
	private static final String AŬ = LOCAL_PATH + "au.xml";
	private static final String AHA = LOCAL_PATH + "aha.xml";
	private static final String HODIAŬ = LOCAL_PATH + "hodiau.xml";
	private static final String EĈ = LOCAL_PATH + "ecx.xml";
	private static final String HOLA = LOCAL_PATH + "hola.xml";
	private static final String ZZZ = LOCAL_PATH + "zzz.xml";
	private static final String POST = LOCAL_PATH + "post.xml";
	
	private static ReVoReader aŭtoReader, sciiReader, fumoReader, homoReader, krediReader, perReader, ĉiaReader, kajReader, keReader, aĉReader, reReader, laReader, aReader, liReader, a1Reader, aŭReader, ahaReader, hodiaŭReader, eĉReader, holaReader, zzzReader, postReader;
	
	@BeforeAll
	public static void iniciato() {
		aŭtoReader = new ReVoReader(AŬTO);
		sciiReader = new ReVoReader(SCII);
		fumoReader = new ReVoReader(FUMO);
		homoReader = new ReVoReader(HOMO);
		krediReader = new ReVoReader(KREDI);
		perReader = new ReVoReader(PER);
		ĉiaReader = new ReVoReader(ĈIA);
		kajReader = new ReVoReader(KAJ);
		keReader = new ReVoReader(KE);
		aĉReader = new ReVoReader(AĈ);
		reReader = new ReVoReader(RE);
		laReader = new ReVoReader(LA);
		aReader = new ReVoReader(A);
		liReader = new ReVoReader(LI);
		a1Reader = new ReVoReader(A1);
		aŭReader = new ReVoReader(AŬ);
		ahaReader = new ReVoReader(AHA);
		hodiaŭReader = new ReVoReader(HODIAŬ);
		eĉReader = new ReVoReader(EĈ);
		holaReader = new ReVoReader(HOLA);
		zzzReader = new ReVoReader(ZZZ);
		postReader = new ReVoReader(POST);
	}
	
	@Test
	public void aŭtoHavasĜustanRadikon() {
		ReVoEntry enigo = aŭtoReader.getEnigo();
		
		assertEquals("aŭt", enigo.getVortero());
	}
	
	@Test
	public void aŭtoEstasRadiko() {
		ReVoEntry enigo = aŭtoReader.getEnigo();
		
		assertEquals(VorterSpeco.RADIKO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭtiEstasNetransitiva() {
		ReVoEntry enigo = aŭtoReader.getEnigo();
		
		assertEquals(Transitiveco.NETRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test
	public void ReaderFunkciasPerDosiero() {
		ReVoReader Reader = new ReVoReader(new File(AŬTO));
		ReVoEntry enigo = Reader.getEnigo();
		
		assertEquals(aŭtoReader.getEnigo(), enigo);
	}
	
	@Test
	public void sciiEstasTransitiva() {
		ReVoEntry enigo = sciiReader.getEnigo();
		
		assertEquals(Transitiveco.TRANSITIVA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu trovi kaj [tr] kaj [ntr] donas ĝustan transitivecon
	public void fumiEstasKajTransitivaKajNetransitiva() {
		ReVoEntry enigo = fumoReader.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void homoTransitivecoEstasNedifinita() {
		ReVoEntry enigo = homoReader.getEnigo();
		
		assertEquals(Transitiveco.NEDIFINITA, enigo.getTransitiveco());
	}
	
	@Test //testas, ĉu la indikilo [x] donas ĝustan transitivecon
	public void krediEstasKajTransitivaKajNeTransitiva() {
		ReVoEntry enigo = krediReader.getEnigo();
		
		assertEquals(Transitiveco.AMBAŬ, enigo.getTransitiveco());
	}
	
	@Test
	public void perEstasPrepozicio() {
		ReVoEntry enigo = perReader.getEnigo();
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		ReVoEntry enigo = ĉiaReader.getEnigo();
		
		assertEquals(VorterSpeco.KORELATIVO, enigo.getVorterSpeco());
	}
	
	@Test
	public void kajEstasKonjunkcio() {
		ReVoEntry enigo = kajReader.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void keEstasKonjunkcio() {
		ReVoEntry enigo = keReader.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aĉEstasSufikso() {
		ReVoEntry enigo = aĉReader.getEnigo();
		
		assertEquals(VorterSpeco.SUFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void reEstasPrefikso() {
		ReVoEntry enigo = reReader.getEnigo();
		
		assertEquals(VorterSpeco.PREFIKSO, enigo.getVorterSpeco());
	}
	
	@Test
	public void laEstasArtikolo() {
		ReVoEntry enigo = laReader.getEnigo();
		
		assertEquals(VorterSpeco.ARTIKOLO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aEstasFinaĵo() {
		ReVoEntry enigo = aReader.getEnigo();
		
		assertEquals(VorterSpeco.FINAĴO, enigo.getVorterSpeco());
	}
	
	@Test
	public void liEstasPronomo() {
		ReVoEntry enigo = liReader.getEnigo();
		
		assertEquals(VorterSpeco.PRONOMO, enigo.getVorterSpeco());
	}
	
	@Test
	public void a1EstasLitero() {
		ReVoEntry enigo = a1Reader.getEnigo();
		
		assertEquals(VorterSpeco.LITERO, enigo.getVorterSpeco());
	}
	
	@Test
	public void aŭEstasKonjunkcioNeAŭVorto() {
		ReVoEntry enigo = aŭReader.getEnigo();
		
		assertEquals(VorterSpeco.KONJUNKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ahaEstasInterjekcio() {
		ReVoEntry enigo = ahaReader.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void hodiaŭEstasAdverbo() {
		ReVoEntry enigo = hodiaŭReader.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void eĉEstasAdverbo() {
		ReVoEntry enigo = eĉReader.getEnigo();
		
		assertEquals(VorterSpeco.ADVERBO, enigo.getVorterSpeco());
	}
	
	@Test
	public void holaEstasInterjekcio() {
		ReVoEntry enigo = holaReader.getEnigo();
		
		assertEquals(VorterSpeco.INTERJEKCIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void zzzEstasSonimito() {
		ReVoEntry enigo = zzzReader.getEnigo();
		
		assertEquals(VorterSpeco.SONIMITO, enigo.getVorterSpeco());
	}
	
	@Test
	public void postEstasPrepozicio() {
		ReVoEntry enigo = postReader.getEnigo();
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
}
