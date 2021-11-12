package it.uniroma2.pjdm.didyouplayed.db.dao.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public abstract class JdbcDAO {

	protected Connection conn;

	public JdbcDAO(Connection conn) throws SQLException {
		this.conn = conn;
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
			while (enumDrivers.hasMoreElements()) {
				Driver driver = enumDrivers.nextElement();
				DriverManager.deregisterDriver(driver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
