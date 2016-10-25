/**
 * 
 */
package com.coolisland.trackmystocks.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coolisland.CreateAccount;
import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;

/**
 * @author Silvio
 *
 */
public class DeleteAccount {
	private static final Logger logger = LoggerFactory.getLogger(DeleteAccount.class);


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


	@Test
	public void deleteAccount() {
		AccountBO account = new AccountBO();

		account.setId((long) 100);
		account.setName("Test Account");
		account.setParentId(null);

		AccountDao dao = new AccountDao();
		int accountsDeleted = 0;

		// if the account does not exist, create it first
		if (!AccountUtilities.accountExists(account)) {
			CreateAccount accountCreator = new CreateAccount();
			
			accountCreator.createAccount();
		}

		// if the account still does not exist, we cannot continue
		if (!AccountUtilities.accountExists(account)) {
			fail("Cannot delete account because account does not exist. Tried to create account, but that failed too");
		}
		
		
		/*
		 * proceed with deleting the account
		 */
		try {
			accountsDeleted = dao.deleteAccount(account);
		} catch (SQLException e) {
			LogUtilities.logException(e);
			
			logger.debug("Unable to delete account");
			accountsDeleted = 0;
			fail("Failed to delete an account that exists");
		}

		// verify that we deleted 1 account
		assertEquals("Expected to delete 1 account", 1, accountsDeleted);
		
		if (accountsDeleted == 1) {
			/*
			 * verify that account is really removed
			 */
			if (AccountUtilities.accountExists(account)) {
				logger.debug("Account still exists");
				fail("Failed to delete an account that exists");
			}
			else {
				logger.debug("Account was removed");
			}
		}		
	}
}
