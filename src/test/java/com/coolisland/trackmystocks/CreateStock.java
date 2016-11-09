/**
 * 
 */
package com.coolisland.trackmystocks;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.utils.AccountUtilities;
import com.coolisland.trackmystocks.utils.StockUtilities;

/**
 * @author Grasshopper
 *
 */
public class CreateStock {
	private AccountBO account = null;
	private StockBO stock = null;
	
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
		StockUtilities.deleteTestStock(stock );
		
		AccountUtilities.deleteTestAccount(account);
	}

	
	private boolean deleteStock(StockBO stock) {
		StockDao dao = null;
		boolean result = false;
		
		try {
			dao = new StockDao();
		} catch (SQLException e) {
			fail("Unable to delete stock");
		}
		
		try {
			dao.deleteStock(stock);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("Unable to delete stock");
		}
		
		return result;
	}

	@Test
	public void addStock() {
		boolean deleteResult = false;
		
		account = AccountUtilities.createTestAccount();

		try {
			stock = StockUtilities.createTestStock(account);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to add stock to account");
		}
		
		deleteResult = deleteStock(stock);

		assertTrue("Failed to remove stock from account", deleteResult);
		
		AccountUtilities.deleteTestAccount(account);
	}

	
}

