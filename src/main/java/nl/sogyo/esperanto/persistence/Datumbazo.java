package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.API.Transitiveco;
import nl.sogyo.esperanto.API.VorterSpeco;

/**
 * La datumbazo de ReVo-vorteroj.
 * @author jfranssen
 *
 */
public class Datumbazo {
	public static final String NOMO = "ReVo";
	private static final String DATUMBAZO_DOSIERUJO_PADO = "src/main/resources/datumbazo/";
	private static final File DATUMBAZO_DOSIERUJO = new File(DATUMBAZO_DOSIERUJO_PADO); 
	private static final String DATUMBAZEJO = DATUMBAZO_DOSIERUJO_PADO + NOMO;
	
	private Connection konekto;
	private static String tipo = "";
	
	private static final String TABELTITOLO = "vorteroj";
	private static final String[] KOLUMNOJ = new String[] {"vortero", "speco", "transitiveco"};
	private static final String KOLUMNOJ_ĈENO = Arrays.toString(KOLUMNOJ).replaceAll("(\\[|\\])", "");
	private static final File REVO_DOSIERUJO = new File("src/main/resources/ReVo/");
	
	private static final Datumbazo REVO_DATUMBAZO = new Datumbazo();
	
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if(konekto != null) {
					fermiKonekton();
				}
			}
		}));
	}
	
	/**
	 * Testa metodo por havi aliron al la datumbazo post ĝia iniciatiĝo
	 * @param args
	 */
	public static void main(String[] args) {
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {"--url",  String.format("jdbc:hsqldb:%s:%s", tipo, tipo.equals("mem") ? NOMO : DATUMBAZEJO)});
	}
	
	/**
	 * Iniciatas la datumbazon el la persistigita dosiero en DATUMBAZEJO aŭ, se tiu dosiero ne ekzistas, kreas la datumbazon el la dosieroj de ReVo (ĉi tio estas malrapida)
	 */
	private Datumbazo() {
		try {
			if(DATUMBAZO_DOSIERUJO.exists()) {
				konekto = DriverManager.getConnection("jdbc:hsqldb:file:" + DATUMBAZEJO + ";files_readonly=true", "SA", "");
				tipo = "file";
			} else {
				konekto = DriverManager.getConnection("jdbc:hsqldb:mem:" + NOMO, "SA", "");
				tipo = "mem";
				kreiDatumbazonElReVoDosieroj();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Donas la ununura ekzemplero de la datumbazo
	 * @return la datumbazon
	 */
	public static Datumbazo getReVoDatumbazo() {
		return REVO_DATUMBAZO;
	}
	
	/**
	 * Por serĉi la datumbazon laŭ vortero
	 * @param vortero literĉeno, kiu reprezentas vorteron
	 * @return liston da {@code IVortero}-ekzempleroj (fake {@code ReVoEnigo}) kiuj havas vorteron kongruantan kun {@code vortero}
	 */
	public List<IVortero> preniElDatumbazo(String vortero) {
		List<IVortero> vorteroj = new ArrayList<>();
		ResultSet rezulto = null;
		try {
			PreparedStatement ordono = konekto.prepareStatement(String.format("SELECT %1$s, %2$s, %3$s FROM " + TABELTITOLO + " WHERE %1$s LIKE ?;", (Object[])KOLUMNOJ));
			ordono.setString(1, vortero);
			rezulto = ordono.executeQuery();
			while(rezulto.next()) {
				vorteroj.add(new ReVoEnigo(rezulto.getString(1), VorterSpeco.valueOf(rezulto.getString(2)), Transitiveco.valueOf(rezulto.getString(3))));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rezulto.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return vorteroj;
	}
	
	/**
	 * Kreas la datumbazon el la ReVo-dosieroj
	 */
	private void kreiDatumbazonElReVoDosieroj() {
		try {
			Statement ordono = konekto.createStatement();
			ordono.execute(String.format("CREATE TABLE %s (%s, %s, %s, %s);", TABELTITOLO, "id INTEGER IDENTITY PRIMARY KEY", KOLUMNOJ[0] + " VARCHAR(32)", KOLUMNOJ [1] + " VARCHAR(32)", KOLUMNOJ[2] + " VARCHAR(32)"));
			
			for(File dosiero : REVO_DOSIERUJO.listFiles()) {
				if(dosiero.isFile()) {
					ReVoEnigo enigo = (new ReVoLegilo(dosiero)).getEnigo();
					if(!enigo.getVortero().isEmpty() && enigo.getVorterSpeco() != null && enigo.getTransitiveco() != null) {
						enigiEnTabelon(enigo);
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Enmetas novan enigon en la tabelon
	 * @param enigo konkreta objekto de {@code IVortero}
	 */
	void enigiEnTabelon(IVortero enigo) {
		try {
			PreparedStatement ordono = konekto.prepareStatement(String.format("INSERT INTO %s (%s) VALUES (?, ?, ?);", TABELTITOLO, KOLUMNOJ_ĈENO));
			ordono.setString(1, enigo.getVortero());
			ordono.setString(2, enigo.getVorterSpeco().toString());
			ordono.setString(3, enigo.getTransitiveco().toString());
			ordono.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fermas la konekton al la datumbazo kaj, se necesas, persistigas ĝin al dosiero
	 */
	public void fermiKonekton() {
		try {
			if(!DATUMBAZO_DOSIERUJO.exists()) {
				DATUMBAZO_DOSIERUJO.mkdirs();
    			Statement ordono = konekto.createStatement();
    			ordono.execute(String.format("SCRIPT '%s';", DATUMBAZEJO + ".script"));
			}
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
