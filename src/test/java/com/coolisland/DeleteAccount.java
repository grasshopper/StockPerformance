/**
 * 
 */
package com.coolisland;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;

/**
 * @author Silvio
 *
 */
public class DeleteAccount {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private boolean accountExists(AccountBO account) {
		// does account exist
		AccountDao dao = new AccountDao();
		AccountBO existingAccount = null;
		boolean found = false;

		try {
			existingAccount = dao.getAccount(account.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (existingAccount != null) {
			System.out.println("Account exists");
			found  = true;
		}
		
		return found;
	}

	@Test
	public void deleteAccount() {
		AccountBO account = new AccountBO();

		account.setId((long) 100);
		account.setName("Test Account");
		account.setParentId(null);

		AccountDao dao = new AccountDao();
		boolean result = false;

		// if the account does not exist, create it first
		if (!accountExists(account)) {
			CreateAccount accountCreator = new CreateAccount();
			
			accountCreator.createAccount();
		}

		// if the account still does not exist, we cannot continue
		if (!accountExists(account)) {
			fail("Cannot delete account because account does not exist. Tried to create account, but that failed too");
		}
		
		
		/*
		 * proceed with deleting the account
		 */
		try {
			result = dao.deleteAccount(account);
		} catch (SQLException e) {
			e.printStackTrace();
			
			System.out.println("Unable to delete account");
			result = false;
			fail("Failed to delete an account that exists");
		}

		if (result) {
			/*
			 * verify that account is really removed
			 */
			if (accountExists(account)) {
				System.out.println("Account still exists");
				fail("Failed to delete an account that exists");
			}
			else {
				System.out.println("Account was removed");
			}
		}		
	}
}
