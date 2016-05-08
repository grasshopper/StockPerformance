package com.coolisland.trackmystocks.stockquotes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coolisland.CreateAccount;
import com.coolisland.CreateStock;
import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.utils.StringUtils;
import com.coolisland.trackmystocks.yahoo.PopulateHistoricalPrices;

import au.com.bytecode.opencsv.CSVReader;

public class MovingAveragesTester {

	private static final Logger logger = LoggerFactory.getLogger(MovingAveragesTester.class);

	private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

	private int addMicrosoftRealPricesAsHistoricalPrices(StockQuoteHistoryDao pricesDao, StockBO stock) {
		final String msftHistoricalPricesFile = new String(
				"C:\\Development\\workspaces\\workspace-sts\\TrackMyStockWithoutSpring\\src\\test\\java\\com\\coolisland\\trackmystocks\\stockquotes\\MicrosoftHistoricalPrices.txt");
		final String DATE_FORMAT = "yyyy-MM-dd";
		CSVReader reader = null;
		int row = 0;

		try {
			reader = new CSVReader(new FileReader(msftHistoricalPricesFile));
		} catch (FileNotFoundException e) {
			logger.error("ERROR: unable to open file: " + msftHistoricalPricesFile);
			e.printStackTrace();
		}

		StockQuoteHistoryBO quoteDataBean = new StockQuoteHistoryBO();

		String[] nextLine;

		try {
			nextLine = reader.readNext();

			if (nextLine == null) {
				fail("no data found in " + msftHistoricalPricesFile);
			}

			// nextLine[] is an array of values from the line
			while (nextLine != null) {
				row++;

				int columnIndex = 0;
				String quoteDayStr = nextLine[columnIndex++];
				java.util.Calendar temp = StringUtils.dateString2Calendar(quoteDayStr,
						new SimpleDateFormat(DATE_FORMAT));
				Date quoteDay = new Date(temp.getTimeInMillis());
				String column2 = nextLine[columnIndex++];

				quoteDataBean.setTickerId(stock.getId());
				quoteDataBean.setLastTradeDateTime(quoteDay);
				quoteDataBean.setQuoteDate(quoteDay);
				quoteDataBean.setLastTradeAmount(column2);

//				logger.debug(quoteDataBean.toString());

				// add the price we just read to the database
				try {
					pricesDao.addTickerHistory(quoteDataBean);
				} catch (SQLException e) {
					logger.error(quoteDataBean.toString());

					fail("Failed to add price to database.");
				}

				nextLine = reader.readNext();
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to read price history and/or create bean");
		}

		return row;
	}

	@Test
	public void getAll200DayAverages() throws Exception {
		// MovingAverages target = new MovingAverages();
		// target.getAll200DayAverages();
	}

	@Test
	public void microsoftRealPrices() throws Exception {
		// create test stock
		CreateStock stockCreator = new CreateStock();

		// create test account
		AccountBO account = stockCreator.createAccount();

		// add test stock to test account
		try {
			stockCreator.createStock(account);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// fetch the test stock
		StockBO stock = null;
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();

			stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (stock == null) {
			fail("Could not find test stock");
		}

		StockQuoteHistoryDao pricesDao = null;
		try {
			pricesDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Unable to create StockQuoteHistoryDao");
		}

		int numberHistoricalPrices = addMicrosoftRealPricesAsHistoricalPrices(pricesDao, stock);

		System.out.println("Number of prices inserted " + numberHistoricalPrices);
		assertTrue("No historical prices inserted", numberHistoricalPrices > 1);

		/*
		 * get the average moving average
		 */
		Double movingAverage = null;
		try {
			movingAverage = pricesDao.get200DaySimpleMovingAverage(new BigDecimal(stock.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to get 200 day moving average");
		}

		System.out.println("200 Day Moving Average: " + movingAverage);
		
		float truncatedMovingAverage = Math.round(movingAverage.floatValue() * 100) / 100.0f;
		assertTrue("200 Day moving average", 49.66f == truncatedMovingAverage);
	}

	@Test
	public void risingPrice() throws Exception {
		// create test stock
		CreateStock stockCreator = new CreateStock();

		// create test account
		AccountBO account = stockCreator.createAccount();

		// add test stock to test account
		try {
			stockCreator.createStock(account);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// fetch the test stock
		StockBO stock = null;
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();

			stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (stock == null) {
			fail("Could not find test stock");
		}

		StockQuoteHistoryDao pricesDao = null;
		try {
			pricesDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Unable to create StockQuoteHistoryDao");
		}

		float startPrice = 10.00f;
		float increment = 1.0f;
		int numberHistoricalPrices = setHistoryPriceRising(pricesDao, stock, startPrice, increment);

		System.out.println("Number of prices inserted " + numberHistoricalPrices);
		assertTrue("No historical prices inserted", numberHistoricalPrices > 1);

		/*
		 * get the average moving average
		 */
		Double movingAverage = null;
		try {
			movingAverage = pricesDao.get200DaySimpleMovingAverage(new BigDecimal(stock.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to get 200 day moving average");
		}

		System.out.println("200 Day Moving Average: " + movingAverage);

		// data setup
		List<Double> prices = new ArrayList<Double>();
		float todayPrice = startPrice;
		for (int i = 0; i < numberHistoricalPrices; i++) {
			Double todayPriceDouble = new Double(todayPrice);
			prices.add(todayPriceDouble);
			todayPrice += increment;
		}
		
		// add up the data for last 200 days
		int startIndex = prices.size() - 200;
		Double sum = new Double(0);
		for (int index = startIndex; index < prices.size(); index++) {
			sum = sum.doubleValue() + prices.get(index).doubleValue();
		}
		logger.debug("sum of last 200 prices: " + sum.doubleValue());
		
		// average
		Double average = sum.doubleValue() / new Double(200).doubleValue();
		logger.debug("average of last 200 prices: " + average.doubleValue());
		
		
		/*
		float priceLastDay = startPrice + (numberHistoricalPrices * increment);
		float priceLastDayMinus200 = priceLastDay - (200 * increment);
		float sumOfLast200Days = priceLastDayMinus200;
		for (int day = 0; day < 200; day++) {
			sumOfLast200Days = sumOfLast200Days + sumOfLast200Days + increment;
		}
		float expectedMovingAvg200Day = sumOfLast200Days / 200;
		System.out.println("Expcected 200 day moving average: " + expectedMovingAvg200Day + "\n");
		*/

		assertEquals("200 Day moving average", average, movingAverage.floatValue(), 0.1f);
	}

	private void setHistoryPrice(StockQuoteHistoryDao pricesDao, StockBO stock, String priceToUse) {
		// populate the historical prices
		PopulateHistoricalPrices prices = new PopulateHistoricalPrices();
		StockQuoteHistoryBO quote = new StockQuoteHistoryBO();
		BigDecimal price = new BigDecimal(priceToUse);
		BigDecimal zero = new BigDecimal(0);
		String fiftyTwoWeekRange = null;
		Date startPriceDate = prices.getHistoryStartDate();
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + MILLISECONDS_IN_DAY);

		quote.setTickerId(stock.getId());
		quote.setChangeAmount(zero);
		quote.setChangePercent(zero);
		quote.setDailyVolume(zero);
		quote.setDayHighAmount(price);
		quote.setDayLowAmount(price);
		quote.setEarningsPerShare(price);
		quote.setFiftyTwoWeekRange(fiftyTwoWeekRange);
		quote.setLastTradeAmount(price);
		quote.setOpenAmount(price);
		quote.setPreviousClose(price);
		quote.setPricePerEarnings(zero);
		quote.setVolume(zero);

		Date priceDate = new Date(startPriceDate.getTime());
		while (priceDate.before(tomorrow)) {
			quote.setLastTradeDateTime(priceDate);
			quote.setQuoteDate(priceDate);

			try {
				pricesDao.addTickerHistory(quote);

			} catch (SQLException e) {
				e.printStackTrace();
				fail("Failed to add ticker price to ticker history");
			}

			priceDate.setTime(priceDate.getTime() + MILLISECONDS_IN_DAY);
		}
	}

	private int setHistoryPriceRising(StockQuoteHistoryDao pricesDao, StockBO stock, float startPrice,
			float increment) {
		int numPricesAdded = 0;

		PopulateHistoricalPrices prices = new PopulateHistoricalPrices();
		StockQuoteHistoryBO quote = new StockQuoteHistoryBO();
		BigDecimal price = new BigDecimal(startPrice);
		BigDecimal zero = new BigDecimal(0);
		String fiftyTwoWeekRange = null;
		Date startPriceDate = prices.getHistoryStartDate();
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + MILLISECONDS_IN_DAY);

		quote.setTickerId(stock.getId());
		quote.setChangeAmount(zero);
		quote.setChangePercent(zero);
		quote.setDailyVolume(zero);
		quote.setDayHighAmount(price);
		quote.setDayLowAmount(price);
		quote.setEarningsPerShare(price);
		quote.setFiftyTwoWeekRange(fiftyTwoWeekRange);
		quote.setLastTradeAmount(price);
		quote.setOpenAmount(price);
		quote.setPreviousClose(price);
		quote.setPricePerEarnings(zero);
		quote.setVolume(zero);

		Date priceDate = new Date(startPriceDate.getTime());
		while (priceDate.before(tomorrow)) {
			numPricesAdded++;

			quote.setLastTradeDateTime(priceDate);
			quote.setQuoteDate(priceDate);

			try {
				pricesDao.addTickerHistory(quote);

			} catch (SQLException e) {
				e.printStackTrace();
				fail("Failed to add ticker price to ticker history");
			}

			priceDate.setTime(priceDate.getTime() + MILLISECONDS_IN_DAY);
			BigDecimal previousPrice = quote.getLastTradeAmount();
			BigDecimal nextPrice = previousPrice.add(new BigDecimal(increment));
			quote.setDayHighAmount(nextPrice);
			quote.setDayLowAmount(nextPrice);
			quote.setEarningsPerShare(nextPrice);
			quote.setLastTradeAmount(nextPrice);
			quote.setOpenAmount(nextPrice);
			quote.setPreviousClose(previousPrice);
		}

		return numPricesAdded;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tearDown();
	}

	@Test
	public void setupAllPriceHistoryToSameValue() {
		// create test stock
		CreateStock stockCreator = new CreateStock();

		// create test account
		AccountBO account = stockCreator.createAccount();

		// add test stock to test account
		try {
			stockCreator.createStock(account);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// fetch the test stock
		StockBO stock = null;
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();

			stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (stock == null) {
			fail("Could not find test stock");
		}

		StockQuoteHistoryDao pricesDao = null;
		try {
			pricesDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Unable to create StockQuoteHistoryDao");
		}

		setHistoryPrice(pricesDao, stock, "10.00");

		/*
		 * get the average moving average
		 */
		Double movingAverage = null;
		try {
			movingAverage = pricesDao.get200DaySimpleMovingAverage(new BigDecimal(stock.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Failed to get 200 day moving average");
		}

		System.out.println("200 Day Moving Average: " + movingAverage);
	}

	@Test
	public void setupPriceHistoryForNewStock() {
		// create account
		CreateStock stockCreator = new CreateStock();
		AccountBO account = stockCreator.createAccount();

		StockBO stock = null;
		// add stock to account
		try {
			stock = stockCreator.createStock(account);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Failed to Create Stock");
		}

		// fetch the test stock
		StockDao stockDao = null;
		try {
			stockDao = new StockDao();

			stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Could find the test stock");
		}
		System.out.println("Test stock " + stock.getName() + " created");

		// populate the historical prices
		PopulateHistoricalPrices historicalPrices = new PopulateHistoricalPrices();

		int numRecordsPopulated = historicalPrices.populateStockHistory(stock.getId(), stock.getSymbol(),
				stock.getName());

		assertTrue("Failed to populate historical prices", numRecordsPopulated > 0);

		/*
		 * clean up
		 */
		// delete stock historical prices
		StockQuoteHistoryDao historyDao = null;
		try {
			historyDao = new StockQuoteHistoryDao();
		} catch (SQLException e) {
			e.printStackTrace();

			fail("Failed to get DAO");
		}

		try {
			historyDao.deleletStockPriceHistory(stock.getId());
		} catch (SQLException e1) {
			e1.printStackTrace();

			fail("Failed to clean up stock prices");
		}

		// delete stock
		try {
			stockDao.deleteStock(stock);
		} catch (SQLException e) {
			e.printStackTrace();

			fail("Failed to clean up stock by deleting it");
		}

		// delete account
		AccountDao accountDao = new AccountDao();
		try {
			accountDao.deleteAccount(account);
		} catch (SQLException e) {
			e.printStackTrace();

			fail("Failed to clean up Account by deleting it");
		}
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

			stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
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
				e1.printStackTrace();

				fail("Failed to clean up stock prices");
			}

			/*
			 * delete stock
			 */
			try {
				stockDao = new StockDao();

				stock = stockDao.getStockTickerBySymbol(CreateStock.STOCK_SYMBOL);
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
	public void updateMovingAverages() throws Exception {
		// TODO auto-generated by JUnit Helper.
		// MovingAverages target = new MovingAverages();
		// Long tickerId = null;
		// MovingAverageValues avgValues = null;
		// target.updateMovingAverage(tickerId, avgValues);
	}

}
