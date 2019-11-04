package nl.sogyo.esperanto.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.*;

class DatumbazoTest {
	
	private static Datumbazo datumbazo;
	private static Connection konekto;
	
	@BeforeAll
	public static void iniciato() {
		datumbazo = new Datumbazo("TestDB");
		konekto = datumbazo.getKonekto();
	}
	
	@AfterAll
	public static void fini() {
		datumbazo.fermiKonekton();
	}
	
	@Test
	public void kreiTabelonNeĴetasEscepton() {
		assertDoesNotThrow(() -> {
			Statement ordono = konekto.createStatement();
			
			ordono.execute("CREATE TABLE tabelo (kolumno VARCHAR(5))");
			konekto.commit();
		});
	}
}
