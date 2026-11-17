package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public static Connection con  = null;
	private static ConnectDB instance = new ConnectDB();
	
	public static ConnectDB getInstance() {
		return instance;
	}
	
	public void connect() {
String url = "jdbc:sqlserver://localhost:1433;"
		           + "databaseName=QLBH;"
		           + "encrypt=true;"
		           + "trustServerCertificate=true;";
		String user = "sa";
		String password = System.getenv("QLBH_DB_PASSWORD");
		
		try {
			if(con == null || con.isClosed()) {
				con = DriverManager.getConnection(url, user, password);				
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
	public static void disconnect() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally {
				con = null;
			}
		}
	}
	
	public static Connection getConnection() {
		if(con == null) {
			instance.connect();
		}
		return con;
	}

}
