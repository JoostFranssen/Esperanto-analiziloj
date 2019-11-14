package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DatumbazKomunikiloTest {

	@Test
	public void laHavasUnuEnigon() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("la");
		
		assertEquals(1, rezulto.size());
	}
	
	@Test
	public void ĉarHavasDuEnigojn() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("ĉar");
		
		assertEquals(2, rezulto.size());
	}
	
	@Test
	public void ĉarRezultojEstasMalsamaj() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("ĉar");
		
		assertNotEquals(rezulto.get(0), rezulto.get(1));
	}
	
	@Test
	public void malplenaĈenoHavasNeniunEnigon() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("");
		
		assertEquals(0, rezulto.size());
	}
	
	@Test
	public void iHavasTriMalsamajnRezultojn() {
		Set<Vortero> rezulto = new HashSet<>(DatabaseCommunicator.getFromDatabase("i"));
		
		assertEquals(3, rezulto.size());
	}
}
