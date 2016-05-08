package com.coolisland.trackmystocks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataBaseManager {
	private static final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);
	private boolean autoCommit = true;
	
	private static class DataBaseManagerSingleton {
		public static final DataBaseManager instance = new DataBaseManager();
	}

	private static java.sql.Connection conn = null;
	private final static String password = "passw0rd";
	private final static String url = "jdbc:mysql://localhost/stocks";

	private final static String userName = "stock_app";

	public static DataBaseManager getInstance() {
		return DataBaseManagerSingleton.instance;
	}

	private DataBaseManager() {
		try {
			conn = getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				logger.debug("Database connection terminated");
			} catch (Exception e) { /* ignore close errors */
			}
		}
	}

	/**
	 * 
	 * @param statement
	 * @return the number of rows updated
	 * @throws SQLException
	 */
	public int executeInsert(PreparedStatement statement) throws SQLException {
		 String method = "executeInsert";
		 logger.trace("Starting " + method);

		// boolean success = false;
		//
		// success = statement.execute();
		// conn.commit();
		//
		// return success;

		int rowsUpdated = 0;

		try {
			statement.execute();
		} catch (SQLException e) {
			logger.error("Error executing insert statement");
			logger.error(statement.toString());
			e.printStackTrace();
		}

		
		ResultSet resultSet = null;
		try {
			resultSet = statement.getResultSet();
			if (resultSet != null) {
				logger.debug("ResultSet: " + resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		try {
			rowsUpdated = statement.getUpdateCount();
			if (rowsUpdated != 1) {
				logger.debug("getUpdateCount(): " + rowsUpdated);
			}

			SQLWarning warnings = statement.getWarnings();
			if (warnings != null) {
				logger.warn("Warnings: " + warnings);
				logger.warn(statement.toString());
				logger.warn("Rows updated: " + rowsUpdated);
			}
		} catch (SQLException e) {
			logger.error(statement.toString());
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		if (!autoCommit) {
			try {
				conn.commit();
			} catch (SQLException e) {
				if (1062 == e.getErrorCode()) {
					conn.rollback();
				} else {
					logger.error("SQL State: " + e.getSQLState());
					logger.error("Error update record(s).");
					logger.error("SQL: " + statement.toString());
	
					e.printStackTrace();
					throw new SQLException(e.getMessage(), e.getCause());
				}
			}
		}

		// logger.trace("Exiting " + method + ". " + rowsUpdated +
		// " rows were updated");
		return rowsUpdated;
	}


	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet resultSet = null;

		try {
			Statement s = conn.createStatement();

			resultSet = s.executeQuery(sql);

			if (resultSet == null) {
				logger.debug("SQL statement: " + sql);
				logger.debug("No Results Found");
			}

		} catch (SQLException e) {
			logger.error("SQL statement: " + sql);
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		} finally {
			if (!autoCommit) {
				conn.commit();
			}
		}

		return resultSet;
	}

	/**
	 * 
	 * @param pstmt
	 * @return number of rows updated
	 * @throws SQLException
	 */
	public int executeUpdate(PreparedStatement pstmt) throws SQLException {
		int rowsUpdated = -1;

		try {
			int result = pstmt.executeUpdate();
			logger.debug("result: " + result);

			ResultSet resultSet = pstmt.getResultSet();
			if (resultSet != null) {
				logger.debug("ResultSet: " + resultSet);
			}

			rowsUpdated = pstmt.getUpdateCount();
			logger.debug("getUpdateCount(): " + rowsUpdated);

			SQLWarning warnings = pstmt.getWarnings();
			if (warnings != null) {
				logger.warn("Warnings: " + warnings);
			}
		} catch (SQLException e) {
			logger.error("Error update record(s).");
			logger.error("SQL: " + pstmt.toString());

			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		} finally {
			if (!autoCommit) {
				conn.commit();
			}
		}

		return rowsUpdated;
	}

	public Connection getConnection() throws SQLException {
		if (conn == null) {
			openConnection();
		}

		// is the connection alive
		if (!isAlive()) {
			openConnection();
		}

		return conn;
	}

	private boolean isAlive() {
		final String sql = "SELECT 1";
		Statement s = null;
		boolean alive = false;

		if (conn == null) {
			return alive;
		}

		try {
			s = conn.createStatement();
			ResultSet resultSet = s.executeQuery(sql);

			if (resultSet != null) {
				alive = true;
			}
		} catch (SQLException e) {
			if (s != null) {
				logger.error(s.toString());
			} else {
				logger.error("Statement is null");
			}
			
			e.printStackTrace();
			alive = false;
		}

		return alive;
	}

	private void openConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
			// logger.trace("Database connection established");

			isAlive();

//			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			autoCommit = true;
		} catch (Exception e) {
			logger.error("Cannot connect to database server");
			logger.error("url: " + url);
			logger.error("userName: " + userName);
			logger.error("password: " + password);
			
			throw new SQLException(e.getMessage(), e.getCause());
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement preparedStmt = null;

		try {
			preparedStmt = conn.prepareStatement(sql);

		} catch (SQLException e) {
			if (sql != null) {
				logger.error("sql: " + sql);
			} else {
				logger.error("sql is null");
			}

			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		if (preparedStmt != null) {
			logger.trace("Statement: " + preparedStmt.toString());
		} else {
			logger.trace("Statement is null");
		}
		
		return preparedStmt;
	}
}
