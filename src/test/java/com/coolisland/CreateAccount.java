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
public class CreateAccount {
	public static final String TEST_ACCOUNT_NAME = "Test Account";
	AccountBO account = null;
	
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
		if (account != null) {
			DeleteAccount delete = new DeleteAccount();
			delete.deleteAccount();
			
			account = null;
		}
	}

	
	@Test
	public void createAccount() {
		account = new AccountBO();

		account.setId((long) 100);
		account.setName(TEST_ACCOUNT_NAME);
		account.setParentId(null);

		AccountDao dao = new AccountDao();
		boolean result = false;

		try {
			result = dao.createAccount(account);
		} catch (SQLException e) {
			e.printStackTrace();
			
			System.out.println("Unable to create account");
			fail("Failed to create account... maybe account already exists");
			result = false;
		}

		if (result) {
			// verify account was created
			AccountBO createdAccount = null;
			try {
				createdAccount = dao.getAccount(account.getName());
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Failed to verify account creation");
			}
			
			if (createdAccount == null) {
				fail("Failed to create Account");
			}
			
		} else {
			/*
			 *  failed to create account. Why?
			 */
			
			// does account already exist?
			AccountBO existingAccount = null;
			try {
				existingAccount = dao.getAccount(account.getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (existingAccount != null) {
				System.out.println("Account already existed");
				System.out.println(existingAccount.toString());
			}
			else {
				fail("Failed to create Account and Account does NOT already exist");
			}
		}
	}

}
