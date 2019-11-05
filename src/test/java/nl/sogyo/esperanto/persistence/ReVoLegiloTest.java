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
	
	private static ReVoLegilo aŭtoLegilo, sciiLegilo, fumoLegilo, homoLegilo, krediLegilo, perLegilo, ĉiaLegilo;
	
	@BeforeAll
	public static void iniciato() {
		aŭtoLegilo = new ReVoLegilo(AŬTO);
		sciiLegilo = new ReVoLegilo(SCII);
		fumoLegilo = new ReVoLegilo(FUMO);
		homoLegilo = new ReVoLegilo(HOMO);
		krediLegilo = new ReVoLegilo(KREDI);
		perLegilo = new ReVoLegilo(PER);
		ĉiaLegilo = new ReVoLegilo(ĈIA);
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
		
		System.out.println(enigo);
		
		assertEquals(VorterSpeco.PREPOZICIO, enigo.getVorterSpeco());
	}
	
	@Test
	public void ĉiaEstasKorelativo() {
		ReVoEnigo enigo = ĉiaLegilo.getEnigo();
		
		assertEquals(VorterSpeco.KORELATIVO, enigo.getVorterSpeco());
	}
}
