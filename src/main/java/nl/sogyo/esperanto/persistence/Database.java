package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.net.URL;
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
public class Database {
	public static final String NAME = "ReVo";
	
	private URL databaseFolderPath;
	private String databaseFolderPathString;
	private File databaseFolder;
	private String databaseLocation;
	
	{
		setFileNames();
	}
	
	private Connection connection;
	private static String type = "";
	
	private static final String TABLE_TITLE = "vorteroj";
	private static final String[] COLUMNS = new String[] {"vortero", "speco", "transitiveco"};
	private static final String COLUMNS_STRING = Arrays.toString(COLUMNS).replaceAll("(\\[|\\])", "");
	private final File REVO_FOLDER = new File(getClass().getClassLoader().getResource("ReVo").getPath());
	
	private static final Database REVO_DATABASE = new Database();
	
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { if(connection != null) closeConnection(); }));
	}
	
	private void setFileNames() {
		databaseFolderPath = getClass().getClassLoader().getResource("datumbazo/");
		databaseFolderPathString = (databaseFolderPath != null ? databaseFolderPath.getPath() : "");
		databaseFolder = new File(databaseFolderPathString);
		databaseLocation = databaseFolderPathString + NAME;
		
		System.out.println("!!!!!" + databaseFolderPathString);
	}
	
	/**
	 * Testa metodo por havi aliron al la datumbazo post ĝia iniciatiĝo
	 * @param args
	 */
	public void startDatabaseInterface() {
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {"--url",  String.format("jdbc:hsqldb:%s:%s", type, type.equals("mem") ? NAME : databaseLocation)});
	}
	
	/**
	 * Iniciatas la datumbazon el la persistigita dosiero en DATABASE_LOCATION aŭ, se tiu dosiero ne ekzistas, kreas la datumbazon el la dosieroj de ReVo (ĉi tio estas malrapida)
	 */
	private Database() {
		try {
			if(databaseFolderPath != null) {
				System.out.println("!!!!! FOUND DATABASE FOLDER");
				connection = DriverManager.getConnection("jdbc:hsqldb:file:" + databaseLocation + ";files_readonly=true;sql.ignore_case=true", "SA", "");
				type = "file";
			} else {
				System.out.println("!!!!! DID NOT FIND DATABASE FOLDER");
				connection = DriverManager.getConnection("jdbc:hsqldb:mem:" + NAME + ";sql.ignore_case=true", "SA", "");
				type = "mem";
				createDatabaseFromReVoFiles();
				persistDatabaseToFile();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Donas la ununuran ekzempleron de la datumbazo
	 * @return la datumbazon
	 */
	public static Database getReVoDatabase() {
		return REVO_DATABASE;
	}
	
	/**
	 * Por serĉi la datumbazon laŭ vortero
	 * @param vortero literĉeno, kiu reprezentas vorteron
	 * @return liston da {@code IVortero}-ekzempleroj (fakte {@code ReVoEntry}), kiuj havas vorteron kongruantan kun {@code vortero}
	 */
	public List<IVortero> getFromDatabase(String vortero) {
		List<IVortero> vorteroj = new ArrayList<>();
		ResultSet result = null;
		try {
			PreparedStatement statement = connection.prepareStatement(String.format("SELECT %1$s, %2$s, %3$s FROM " + TABLE_TITLE + " WHERE %1$s LIKE ?;", (Object[])COLUMNS));
			statement.setString(1, vortero);
			result = statement.executeQuery();
			while(result.next()) {
				vorteroj.add(new ReVoEntry(result.getString(1), VorterSpeco.valueOf(result.getString(2)), Transitiveco.valueOf(result.getString(3))));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return vorteroj;
	}
	
	/**
	 * Kreas la datumbazon el la ReVo-dosieroj
	 */
	private void createDatabaseFromReVoFiles() {
		try {
			Statement statement = connection.createStatement();
			statement.execute(String.format("CREATE TABLE %s (%s, %s, %s, %s);", TABLE_TITLE, "id INTEGER IDENTITY PRIMARY KEY", COLUMNS[0] + " VARCHAR(32)", COLUMNS [1] + " VARCHAR(32)", COLUMNS[2] + " VARCHAR(32)"));
			
			for(File file : REVO_FOLDER.listFiles()) {
				if(file.isFile()) {
					ReVoEntry entry = (new ReVoReader(file)).getEnigo();
					if(!entry.getVortero().isEmpty() && entry.getVorterSpeco() != null && entry.getTransitiveco() != null) {
						putIntoTable(entry);
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Enmetas novan enigon en la tabelon
	 * @param entry konkreta objekto de {@code IVortero}
	 */
	void putIntoTable(IVortero entry) {
		try {
			PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s (%s) VALUES (?, ?, ?);", TABLE_TITLE, COLUMNS_STRING));
			statement.setString(1, entry.getVortero());
			statement.setString(2, entry.getVorterSpeco().toString());
			statement.setString(3, entry.getTransitiveco().toString());
			statement.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fermas la konekton al la datumbazo kaj persistigas ĝin al dosiero
	 */
	public void closeConnection() {
		try {
			persistDatabaseToFile();
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private void persistDatabaseToFile() throws SQLException {
		if(databaseFolderPath == null || !databaseFolder.exists()) {
			(new File(getClass().getClassLoader().getResource(".").getPath() + "/datumbazo/")).mkdirs();
			setFileNames();
			Statement statement = connection.createStatement();
			statement.execute(String.format("SCRIPT '%s';", databaseLocation + ".script"));
		}
	}
	
	@Override
	protected void finalize() {
		closeConnection();
	}
}
