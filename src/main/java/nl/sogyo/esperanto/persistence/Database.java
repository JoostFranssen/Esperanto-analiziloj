package nl.sogyo.esperanto.persistence;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	{
		setFileNames();
	}
	
	private Connection connection;
	private static String type = "";
	
	static final String TABLE_TITLE = "vorteroj";
	static final String[] COLUMNS = new String[] {"vortero", "speco", "transitiveco"};
	static final String COLUMNS_STRING = Arrays.toString(COLUMNS).replaceAll("(\\[|\\])", "");
	
	private static final Database REVO_DATABASE = new Database();
	
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { if(connection != null) closeConnection(); }));
	}
	
	private void setFileNames() {
		databaseFolderPath = getClass().getClassLoader().getResource("datumbazo/");
	}
	
	/**
	 * Testa metodo por havi aliron al la datumbazo post ĝia iniciatiĝo
	 * @param args
	 */
	public void startDatabaseInterface() {
		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {"--url",  String.format("jdbc:hsqldb:%s:%s", type, type.equals("mem") ? NAME : "datumbazo/" + NAME)});
	}
	
	/**
	 * Iniciatas la datumbazon el la persistigita dosiero en DATABASE_LOCATION aŭ, se tiu dosiero ne ekzistas, kreas la datumbazon el la dosieroj de ReVo (ĉi tio estas malrapida)
	 */
	private Database() {
		try {
			if(databaseFolderPath != null) {
				connection = DriverManager.getConnection("jdbc:hsqldb:res:datumbazo/" + NAME + ";sql.ignore_case=true", "SA", "");
				type = "res";
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
	 * Fermas la konekton al la datumbazo kaj persistigas ĝin al dosiero
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() {
		closeConnection();
	}
}
