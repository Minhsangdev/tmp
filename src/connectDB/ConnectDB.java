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
		String url = "";
		String user = "sa";
		String password = "sapassword";
		
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
	public static void disconncet() {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
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
