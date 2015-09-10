/**
 * 
 */
package com.coolisland;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

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
public class AccountTypes {
	private static final int NUM_ACCOUNTS = 10;
	static boolean databaseAvailable = false;
	
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
	public void allAccounts() {
		AccountDao dao = new AccountDao();
		List<AccountBO> allAccounts = null;
		
		try {
			allAccounts = dao.getAllAccounts();
		} catch (SQLException e) {
			fail("Unable to retrieve accounts");
		}
		
		assertNotNull("No accounts available", allAccounts);
		
//		for (AccountBO act : allAccounts) {
//			System.out.println(act.getName());
//		}
		
		assertEquals(NUM_ACCOUNTS, allAccounts.size());
	}

	@Test
	public void primaryAccount() {
		AccountDao dao = new AccountDao();
		List<AccountBO> primaryAccounts = null;
		
		try {
			primaryAccounts = dao.getPrimaryAccounts();
		} catch (SQLException e) {
			fail("Unable to retrieve accounts");
		}
		
		assertNotNull("No accounts available", primaryAccounts);
		
//		for (AccountBO act : primaryAccounts) {
//			System.out.println(act.getName());
//		}
		
		assertTrue(primaryAccounts.size() > 4);
	}


	@Test
	public void getAccountSilvio() {
		AccountDao dao = new AccountDao();
		AccountBO silvioryAccount =	 null;
		String accountName =  "Silvio GWRS 401K";
		
		try {
			silvioryAccount = dao.getAccount(accountName);
		} catch (SQLException e) {
			fail("Unable to retrieve account");
		}
		
		assertNotNull("No accounts available", silvioryAccount);
		
//		for (AccountBO act : primaryAccounts) {
//			System.out.println(act.getName());
//		}
		
		assertEquals(accountName, silvioryAccount.getName());
	}

	@Test
	public void getAccountAngela() {
		AccountDao dao = new AccountDao();
		AccountBO angelaAccount =	 null;
		String accountName =  "Angela Level 3 401K";
		
		try {
			angelaAccount = dao.getAccount(accountName);
		} catch (SQLException e) {
			fail("Unable to retrieve account");
		}
		
		assertNotNull("No accounts available", angelaAccount);
		
//		for (AccountBO act : primaryAccounts) {
//			System.out.println(act.getName());
//		}
		
		assertEquals(accountName, angelaAccount.getName());
	}
}
