package nl.sogyo.esperanto.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import nl.sogyo.esperanto.API.IVortero;

public class DatabaseResourcePlacer {
	private static final String TABLE_TITLE = "vorteroj";
	private static final String[] COLUMNS = new String[] {"vortero", "speco", "transitiveco"};
	private static final String COLUMNS_STRING = Arrays.toString(COLUMNS).replaceAll("(\\[|\\])", "");
	private static final String NAME = "ReVo";
	
	private static final String databaseFolder = "datumbazo/";
	private static final String resourceMainFolderPathString = "src/main/resources/";
	private static final String resourceTestFolderPathSting = "src/test/resources/";
	private static final File resourceMainDatabaseFolder = new File(resourceMainFolderPathString + databaseFolder);
	private static final File resourceTestDatabaseFolder = new File(resourceTestFolderPathSting + databaseFolder);
	private static final File REVO_FOLDER = new File(NAME);
	
	private static Connection connection;
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:mem:" + NAME + ";sql.ignore_case=true", "SA", "");
			createDatabaseFromReVoFiles();
			persistDatabaseToFile(resourceMainDatabaseFolder);
			persistDatabaseToFile(resourceTestDatabaseFolder);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private static void createDatabaseFromReVoFiles() throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute(String.format("CREATE TABLE %s (%s, %s, %s, %s);", TABLE_TITLE, "id INTEGER IDENTITY PRIMARY KEY", COLUMNS[0] + " VARCHAR(32)", COLUMNS[1] + " VARCHAR(32)", COLUMNS[2] + " VARCHAR(32)"));
		
		File[] files = REVO_FOLDER.listFiles();
		int current = 0;
		int total = files.length;
		
		for(File file : files) {
			if(file.isFile()) {
				ReVoEntry entry = (new ReVoReader(file)).getEnigo();
				if(!entry.getVortero().isEmpty() && entry.getVorterSpeco() != null && entry.getTransitiveco() != null) {
					putIntoTable(entry);
				}
			}
			current++;
			float percentage = 100 * (float)current/total;
			System.out.print(String.format("Creating database \r%5.1f%% █%s%s█ %" + String.valueOf(total).length() + "d/%d", percentage, "█".repeat((int)percentage / 2), " ".repeat(50 - (int)percentage / 2), current, total));
		}
	}
	
	private static void putIntoTable(IVortero entry) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s (%s) VALUES (?, ?, ?);", TABLE_TITLE, COLUMNS_STRING));
		statement.setString(1, entry.getVortero());
		statement.setString(2, entry.getVorterSpeco().toString());
		statement.setString(3, entry.getTransitiveco().toString());
		statement.execute();
	}
	
	private static void persistDatabaseToFile(File path) throws SQLException {
		if(!path.exists()) {
			path.mkdirs();
		}
		for(File file : path.listFiles((dir, fname) -> fname.startsWith(NAME))) {
			if(!file.isDirectory()) {
				file.delete();
			}
		}
		Statement statement = connection.createStatement();
		statement.execute(String.format("SCRIPT '%s';", path.getPath() + "\\" + NAME + ".script"));
	}
}
