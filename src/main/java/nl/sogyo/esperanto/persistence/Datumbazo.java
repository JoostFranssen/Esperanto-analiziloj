package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class Datumbazo {
	private String nomo;
	private Connection konekto;
	
	private static final String TABELTITOLO = "vorteroj";
	private static final String[] KOLUMNOJ = new String[] {"vortero", "speco", "transitiveco"};
	private static final String KOLUMNOJ_ĈENO = Arrays.toString(KOLUMNOJ).replaceAll("(\\[|\\])", "");
	private static final File REVO_DOSIERUJO = new File("src/main/resources/ReVo/");
	
	public static void main(String[] args) {
		Datumbazo datumbazo = new Datumbazo("ReVo");
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {"--url",  "jdbc:hsqldb:mem:" + datumbazo.getNomo()});
	}
	
	public Datumbazo(String nomo) {
		this.nomo = nomo;
		try {
			konekto = DriverManager.getConnection("jdbc:hsqldb:mem:" + this.nomo, "SA", "");
			
			Statement ordono = konekto.createStatement();
			ordono.execute(String.format("CREATE TABLE %s (%s, %s, %s, %s);", TABELTITOLO, "id INTEGER IDENTITY PRIMARY KEY", KOLUMNOJ[0] + " VARCHAR(32)", KOLUMNOJ [1] + " VARCHAR(32)", KOLUMNOJ[2] + " VARCHAR(32)"));
			
			for(File dosiero : REVO_DOSIERUJO.listFiles()) {
				if(dosiero.isFile()) {
					ReVoEnigo enigo = (new ReVoLegilo(dosiero)).getEnigo();
					try {
						enigiEnTabelon(enigo.getVortero(), enigo.getVorterSpeco().toString(), enigo.getTransitiveco().toString());
					} catch(NullPointerException e) {
						//transsalti kelkajn vortojn, kiuj ne estas determineblaj
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void enigiEnTabelon(String... valoroj) {
		try {
			PreparedStatement ordono = konekto.prepareStatement(String.format("INSERT INTO %s (%s) VALUES (?, ?, ?);", TABELTITOLO, KOLUMNOJ_ĈENO));
			for(int i = 0; i < valoroj.length; i++) {
				ordono.setString(i + 1, valoroj[i]);
			}
			ordono.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getKonekto() {
		return konekto;
	}
	
	public String getNomo() {
		return nomo;
	}
	
	public void fermiKonekton() {
		try {
			konekto.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() {
		fermiKonekton();
	}
}
