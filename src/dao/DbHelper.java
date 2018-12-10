package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbHelper {
	private String database;

	public DbHelper() {
		this.database = "";
	}

	public DbHelper(String database) {
		this.database = database;
	}

	public Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/" + this.database;
			conn = DriverManager.getConnection(url, "root", "123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConn(Connection conn, Statement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}