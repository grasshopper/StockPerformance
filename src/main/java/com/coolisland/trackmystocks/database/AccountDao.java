/**
 * 
 */
package com.coolisland.trackmystocks.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Silvio
 * 
 */
public class AccountDao {
	private static final String SELECT_ACCOUNT_STATMENT = "SELECT * FROM ACCOUNT WHERE NAME = ?";
	private static final String SELECT_PRIMARY_STATMENT = "SELECT * FROM ACCOUNT WHERE PARENT_ID IS NULL";
	private static final String SELECT_STATMENT = "SELECT * FROM ACCOUNT";
	private static final String CREATE_ACCOUNT_STATMENT = "INSERT INTO ACCOUNT (ID, NAME, PARENT_ID) VALUES (?, ?, ?)";
	private static final String DELETE_ACCOUNT_STATMENT = "DELETE FROM ACCOUNT WHERE ID = ?";
	private static final String SELECT_ACCOUNT_BY_ID_STATMENT = "SELECT * FROM ACCOUNT WHERE ID = ?";

	private final DataBaseManager dbManager = DataBaseManager.getInstance();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AccountDao accountDao = new AccountDao();

		List<AccountBO> allAccounts;
		try {
			allAccounts = accountDao.getAllAccounts();

			for (AccountBO account : allAccounts) {
				System.out.println(account.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param actName
	 * @return
	 * @throws SQLException
	 */
	public AccountBO getAccount(String actName) throws SQLException {
		String method = "getAccount";
		System.out.println("Starting " + method);
		System.out.println("actName: " + actName);

		AccountBO account = null;

		String sql = SELECT_ACCOUNT_STATMENT;
		ResultSet result = null;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setString(1, actName);

//		System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

		while (result.next()) {
			account = new AccountBO(result);
		}

		return account;
	}

	public List<AccountBO> getAllAccounts() throws SQLException {
		List<AccountBO> accounts = new ArrayList<AccountBO>();

		String sql = SELECT_STATMENT;
		ResultSet result = null;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		result = pstmt.executeQuery();

		while (result.next()) {
			AccountBO account = new AccountBO(result);
			accounts.add(account);
		}

		return accounts;
	}

	public List<AccountBO> getPrimaryAccounts() throws SQLException {
		List<AccountBO> accounts = new ArrayList<AccountBO>();

		String sql = SELECT_PRIMARY_STATMENT;
		ResultSet result = null;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		result = pstmt.executeQuery();

		while (result.next()) {
			AccountBO account = new AccountBO(result);
			accounts.add(account);
		}

		return accounts;
	}

	public boolean createAccount(AccountBO newAccount) throws SQLException {
		String method = "createAccount";
		System.out.println("Starting " + method);
		System.out.println("New Account: " + newAccount);

		String sql = CREATE_ACCOUNT_STATMENT;
		boolean result = false;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, newAccount.getId());
		pstmt.setString(2, newAccount.getName());
		pstmt.setBigDecimal(3, newAccount.getParentId());

//		System.out.println("SQL statement: " + pstmt.toString());

		int recInserted = dbManager.executeInsert(pstmt);

		System.out.println("records inserted: " + recInserted);

		if (recInserted != 1) {
			result = false;
		} else {
			result = true;
		}

		return result;
	}

	public boolean deleteAccount(AccountBO newAccount) throws SQLException {
		String method = "deleteAccount";
		System.out.println("Starting " + method);
		System.out.println("Account to delete: " + newAccount);

		String sql = DELETE_ACCOUNT_STATMENT;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, newAccount.getId());

//		System.out.println("SQL statement: " + pstmt.toString());

		boolean result = pstmt.execute();

		System.out.println("result: " + result);
		
		if (result == false) {
			SQLWarning warnings = pstmt.getWarnings();
			warnings = DataBaseManager.getInstance().getConnection().getWarnings();
			
			if (warnings != null) {
				System.out.println("Warnings: " + warnings);
				System.out.println(pstmt.toString());
			}
			
			System.out.println("Rows updated: " + pstmt.getUpdateCount());
		}

		return result;
	}

	public String getAccountName(Long accountId) throws SQLException {
//		String method = "getAccountName";
//		System.out.println("Starting " + method);
//		System.out.println("Account ID: " + accountId);

		AccountBO account = null;

		String sql = SELECT_ACCOUNT_BY_ID_STATMENT;
		ResultSet result = null;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, accountId);

//		System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

		while (result.next()) {
			account = new AccountBO(result);
		}

		return account.getName();
	}
	
}
