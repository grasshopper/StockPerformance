/**
 * 
 */
package com.coolisland;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.sellbuy.ExchangeType;
import com.coolisland.trackmystocks.sellbuy.StockType;

/**
 * @author Silvio
 *
 */
public class CreateStock {
	private static final ExchangeType EXCHANGE = ExchangeType.AMEX;
	private static final Long STOCK_ID = null;
	private static final String STOCK_LABEL = null;
	private static final String STOCK_VALUE = null;
	private static final String STOCK_NAME = "Microsoft";
	private static final Boolean OWNERSHIP = new Boolean(true);
	public static final String STOCK_SYMBOL = "MSFT";
	private static final StockType STOCK_TYPE = StockType.ETF;
	
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
		DeleteAccount tester = new DeleteAccount();
		tester.deleteAccount();
	}

	public AccountBO createAccount() {
		AccountBO account = null;
		
		CreateAccount tester = new CreateAccount();
		tester.createAccount();
		
		try {
			AccountDao dao = new AccountDao();
			account = dao.getAccount("Test Account");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to verify account creation");
		}
		
		if (account == null) {
			fail("Failed to create Account");
		}
		
		return account;
	}


	private void deleteAccount(AccountBO account) {
		DeleteAccount tester = new DeleteAccount();
		tester.deleteAccount();
		
		try {
			AccountDao dao = new AccountDao();
			account = dao.getAccount("Test Account");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to verify account creation");
		}
		
		if (account != null) {
			fail("Failed to delete Account");
		}
	}

	
	public StockBO createStock(AccountBO account) throws Exception {
		StockDao dao = null;
		
		try {
			dao = new StockDao();
		} catch (SQLException e) {
			fail("Unable to create stock");
		}
		
		StockBO stock = new StockBO();
		stock.setAccountId(account.getId());
		stock.setExchange(EXCHANGE);
		stock.setId(STOCK_ID);
		stock.setItemLabel(STOCK_LABEL);
		stock.setItemValue(STOCK_VALUE);
		stock.setName(STOCK_NAME);
		stock.setOwn(OWNERSHIP);
		stock.setSymbol(STOCK_SYMBOL);
		stock.setTickerTypeId(STOCK_TYPE);
		stock.setTrack(true);
		
		stock = dao.createStock(stock);
		
		return stock;
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
		
		AccountBO account = createAccount();

		StockBO stock = null;
		try {
			stock = createStock(account);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failed to add stock to account");
		}
		
		deleteResult = deleteStock(stock);

		assertTrue("Failed to remove stock from account", deleteResult);
		
		deleteAccount(account);
	}

	
}

