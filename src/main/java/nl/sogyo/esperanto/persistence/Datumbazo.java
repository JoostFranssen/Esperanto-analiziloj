package nl.sogyo.esperanto.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Datumbazo {
	private String nomo;
	private Connection konekto;
	
	public static void main(String[] args) {
		new Datumbazo("ReVo");
		org.hsqldb.util.DatabaseManager.main(new String[] { "--url",  "jdbc:hsqldb:mem:ReVo", "--noexit" });
	}
	
	public Datumbazo(String nomo) {
		this.nomo = nomo;
		try {
			konekto = DriverManager.getConnection("jdbc:hsqldb:mem:" + this.nomo, "SA", "");
			
			Statement ordono = konekto.createStatement();
//			ordono.execute("CREATE TABLE radikoj (id INT PRIMARY KEY, radiko VARCHAR(50))");
//			konekto.commit();
		} catch(Exception e) {
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
