package com.coolisland.trackmystocks.stockquotes;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.coolisland.CreateAccount;
import com.coolisland.CreateStock;
import com.coolisland.DeleteAccount;
import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.yahoo.PopulateHistoricalPrices;

public class MovingAveragesTester {

	private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tearDown();
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		StockDao stockDao = null;
		StockBO stock = null;
		StockQuoteHistoryDao historyDao = null;
		AccountDao actDao = new AccountDao();
		
		// fetch the test stock
		try {
			stockDao = new StockDao();
			
			stock  = stockDao.getStockTickerBySymbol("Const Price");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (stock != null) {
			/*
			 * delete stock historical prices
			 */
			try {
				historyDao = new StockQuoteHistoryDao();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				historyDao.deleletStockPriceHistory(stock.getId());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
				fail("Failed to clean up stock prices");
			}
			
			/*
			 * delete stock
			 */
			try {
				stockDao = new StockDao();
				
				stock  = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				stockDao.deleteStock(stock);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * delete the account
		 */
		AccountBO testAccount = actDao.getAccount(CreateAccount.TEST_ACCOUNT_NAME);
		if (testAccount != null) {
			actDao.deleteAccount(testAccount);
		}
	}
	
	
	@Test
	public void setupPriceHistoryForNewStock() {
		// create account
		CreateStock stockCreator = new CreateStock();
		AccountBO account = stockCreator.createAccount();
		
		StockBO stock = null;
		// add stock to account
		try {
			stock  = stockCreator.createStock(account);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Failed to Create Stock");
		}
		
		// fetch the test stock
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();
			
			stock  = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Could find the test stock");
		}

		
		// populate the historical prices
		PopulateHistoricalPrices historicalPrices = new PopulateHistoricalPrices();
		
		int numRecordsPopulated = historicalPrices.populateStockHistory(stock.getId(), stock.getSymbol(), stock.getName());
		
		assertTrue("Failed to populate historical prices", numRecordsPopulated > 0);
		
		/*
		 * clean up
		 */
		// delete stock historical prices
		StockQuoteHistoryDao historyDao = null;
		try {
			historyDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail("Failed to get DAO");
		}
		
		try {
			historyDao.deleletStockPriceHistory(stock.getId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			fail("Failed to clean up stock prices");
		}
		
		// delete stock
		try {
			stockDao.deleteStock(stock);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail("Failed to clean up stock by deleting it");
		}
		
		// delete account
		AccountDao accountDao = new AccountDao();
		try {
			accountDao.deleteAccount(account);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail("Failed to clean up Account by deleting it");
		}
	}
	
	
	@Test
	public void setupAllPriceHistoryToSameValue() {
		// create account
		CreateStock stockCreator = new CreateStock();
		AccountBO account = stockCreator.createAccount();
		
		// add stock to account
		try {
			stockCreator.createStock(account);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// fetch the test stock
		StockBO stock = null;
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();
			
			stock  = stockDao.getStockTickerBySymbol(stockCreator.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (stock == null) {
			fail("Could not find test account");
		}
		
		// populate the historical prices
		PopulateHistoricalPrices prices = new PopulateHistoricalPrices();
		StockQuoteHistoryBO quote = new StockQuoteHistoryBO();
		BigDecimal price = new BigDecimal(10.00);
		BigDecimal zero = new BigDecimal(0);
		String fiftyTwoWeekRanzeroge = null;
		StockQuoteHistoryDao pricesDao = null;
		Date startPriceDate = prices.getHistoryStartDate();
		Date today = new Date();

		try {
			pricesDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		quote.setChangeAmount(zero);
		quote.setChangePercent(zero);
		quote.setDailyVolume(zero);
		quote.setDayHighAmount(price);
		quote.setDayLowAmount(price);
		quote.setEarningsPerShare(price);
		quote.setFiftyTwoWeekRange(fiftyTwoWeekRanzeroge);
		quote.setLastTradeAmount(price);
		quote.setOpenAmount(price);
		quote.setPreviousClose(price);
		quote.setPricePerEarnings(zero);
		quote.setTickerId(stock.getId());
		quote.setVolume(zero);
		
		Date priceDate = new Date(startPriceDate.getTime());
		while (priceDate.before(today)) {
			quote.setLastTradeDateTime(priceDate);
			quote.setQuoteDate(priceDate);

			try {
				pricesDao.addTickerHistory(quote);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			priceDate.setTime(priceDate.getTime() + MILLISECONDS_IN_DAY);
		}
		
		/*
		 * get the average moving average
		 */
		Double movingAverage = null;
		try {
			movingAverage = pricesDao.get200DaySimpleMovingAverage(new BigDecimal(stock.getId()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("200 Day Moving Average: " + movingAverage);
		
	}
	
	
	@Test
	public void updateMovingAverages() throws Exception {
		// TODO auto-generated by JUnit Helper.
//		MovingAverages target = new MovingAverages();
//		Long tickerId = null;
//		MovingAverageValues avgValues = null;
//		target.updateMovingAverage(tickerId, avgValues);
	}

	@Test
	public void getAll200DayAverages() throws Exception {
//		MovingAverages target = new MovingAverages();
//		target.getAll200DayAverages();
	}

}
