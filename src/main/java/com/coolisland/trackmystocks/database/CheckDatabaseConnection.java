/**
 * 
 */
package com.coolisland.trackmystocks.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Grasshopper
 *
 */
public class CheckDatabaseConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CheckDatabaseConnection dbConnTest = new CheckDatabaseConnection();

		dbConnTest.run();
	}

	public void run() {
		testConnection();
	}

	public boolean testConnection() {
		boolean dbAvailable = false;
		String sql = "select sysdate() from dual";

		try {
			DataBaseManager dbManager = DataBaseManager.getInstance();

			ResultSet result = null;
			PreparedStatement pstmt = null;

			pstmt = dbManager.prepareStatement(sql);

			// System.out.println("SQL statement: " + pstmt.toString());

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					try {
						for (int ndx = 1; ; ndx++) {
							String value = result.getString(ndx);
							// System.out.println(value);
							// System.out.println("Database connection is good");
						}
					} catch (Exception e) {
					}
				}
			}
			
			dbAvailable = true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error connecting to database");
		}

		return dbAvailable;
	}

}
