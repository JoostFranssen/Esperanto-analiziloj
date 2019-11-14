package nl.sogyo.esperanto.domain.vortanalizilo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DatabaseCommunicatorTest {

	@Test
	public void laHasOneEntry() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("la");
		
		assertEquals(1, rezulto.size());
	}
	
	@Test
	public void ĉarHasTwoEntries() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("ĉar");
		
		assertEquals(2, rezulto.size());
	}
	
	@Test
	public void ĉarResultsAreDifferent() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("ĉar");
		
		assertNotEquals(rezulto.get(0), rezulto.get(1));
	}
	
	@Test
	public void emptyStringHasNoEntries() {
		List<Vortero> rezulto = DatabaseCommunicator.getFromDatabase("");
		
		assertEquals(0, rezulto.size());
	}
	
	@Test
	public void iHasThreeDifferentEntries() {
		Set<Vortero> rezulto = new HashSet<>(DatabaseCommunicator.getFromDatabase("i"));
		
		assertEquals(3, rezulto.size());
	}
}
