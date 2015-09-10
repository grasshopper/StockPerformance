/**
 * 
 */
package com.coolisland.trackmystocks.stockquotes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coolisland.CreateAccount;
import com.coolisland.CreateStock;
import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.yahoo.PopulateHistoricalPrices;

/**
 * @author Silvio
 *
 */
public class FindMovingAverageCrossedUp {

	private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;
	private static final int NUM_DAYS_IN_PERIOD = 50;


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
		StockDao stockDao = null;
		StockBO stock = null;
		StockQuoteHistoryDao historyDao = null;
		AccountDao actDao = new AccountDao();
		
		// fetch the test stock
		try {
			stockDao = new StockDao();
			
			stock  = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
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

	
	private AccountBO createAccount() {
		CreateStock actCreator = new CreateStock();
		
		AccountBO accountBo = actCreator.createAccount();
		
		return accountBo;
	}
	
	
	private StockBO addStockToAccount(AccountBO account) throws Exception {
		CreateStock stockCreator = new CreateStock();
		
		StockBO stock = stockCreator.createStock(account);
		
		return stock;
	}
	
	private void addHistoricalPricesToStock(StockBO stock) {
		
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
		quote.setFiftyTwoWeekRange(fiftyTwoWeekRanzeroge);
		quote.setPricePerEarnings(zero);
		quote.setTickerId(stock.getId());
		quote.setVolume(zero);
		
		Date priceDate = new Date(startPriceDate.getTime());
		
		/*
		 * add 100 days of escalating price. $1 up each day.
		 * 
		 * Followed by 100 days for descalating price. $1 down each day.
		 */
		int priceDirection = 1;
		while (priceDate.before(today)) {
			int numDays = 0;
			while (numDays < NUM_DAYS_IN_PERIOD) {
				quote.setDayHighAmount(price);
				quote.setDayLowAmount(price);
				quote.setEarningsPerShare(price);
				quote.setLastTradeAmount(price);
				quote.setOpenAmount(price);
				quote.setPreviousClose(price);
				quote.setLastTradeDateTime(priceDate);
				quote.setQuoteDate(priceDate);
				quote.getLastTradeAmount();

				try {
					pricesDao.addTickerHistory(quote);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				priceDate.setTime(priceDate.getTime() + MILLISECONDS_IN_DAY);
				numDays++;
				price = price.add(new BigDecimal(priceDirection));
			}
			
			// reverse price direction
			priceDirection *= -1;
		}
	}
	
	
	private void findMovingAverageCrossedUp(StockBO stock) {
		MovingAverages movingAverages = new MovingAverages();
		
		List<Date> movingAverageCrossedDates = movingAverages.getAll200DaySimpleMovingAverageCrossedUp(stock.getId());
		
		System.out.println("Days when the 200 Day Moving Average were crossed: ");
		if (movingAverageCrossedDates != null) {
			for (Date day : movingAverageCrossedDates) {
				System.out.println("\t" + day);
			}
		}
	}
	
	@Test
	public void findMovingAverageCrossedUp() {
		AccountBO account = createAccount();
		
		assertNotNull("Failed to create account", account);
		
		StockBO stock = null;
		try {
			stock = addStockToAccount(account);
		} catch (Exception e) {
			e.printStackTrace();
			
			fail("Failed to add stock to account");
		}
		
		addHistoricalPricesToStock(stock);

		findMovingAverageCrossedUp(stock);
	}

}
