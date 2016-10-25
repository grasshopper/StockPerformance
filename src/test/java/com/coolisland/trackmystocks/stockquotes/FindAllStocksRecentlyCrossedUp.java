package com.coolisland.trackmystocks.stockquotes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.utils.AccountUtilities;



public class FindAllStocksRecentlyCrossedUp {

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


	
	private void recentMovingAverageCrossedUp(StockBO stock) {
		MovingAverages movingAverages = new MovingAverages();
		
		List<Date> movingAverageCrossedDates = movingAverages.getAll200DaySimpleMovingAverageCrossedUpRecent(stock.getId());
		
		System.out.println("Days when the 200 Day Moving Average were crossed: ");
		if (movingAverageCrossedDates != null) {
			for (Date day : movingAverageCrossedDates) {
				System.out.println("\t" + day);
			}
		}
	}
	
	@Test
	public void findRecentMovingAverageCrossedUp() {
		Collection<AccountBO> accounts = AccountUtilities.getAllPrimaryAccounts();

		assertNotNull("No Primary Accounts Found", accounts);
		assertTrue("No Primary Accounts Found", accounts.size() > 0);

		// for each account, iterate over all its stocks to find any that have crossed the moving average going up recently.
		for (AccountBO account : accounts) {
			try {
				StockDao dao = new StockDao();
				
				List<StockBO> stocks = dao.getStockTickersForAccount(account.getId().intValue());
				
				for (StockBO stock : stocks) {
					recentMovingAverageCrossedUp(stock);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



}
