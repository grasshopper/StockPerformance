package com.coolisland.trackmystocks.yahoo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.utils.StringUtils;

public class PopulateHistoricalPrices {
	private static final Logger logger = LoggerFactory.getLogger(StockQuoteHistoryDao.class);

	public java.util.Date getHistoryStartDate() {
		Calendar cal = Calendar.getInstance();

		cal.set(2010, 1, 1);

		return cal.getTime();
	}

	private StockQuoteHistoryBO createStockQuoteHistoryBO(Long tickerId) throws SQLException {
		StockQuoteHistoryBO historyBo = new StockQuoteHistoryBO();

		historyBo.setTickerId(tickerId);

		return historyBo;
	}

	/**
	 * 
	 * @param stockTicker
	 * @return
	 * @throws Exception
	 */
	private Calendar getStartDateForTicker(Long tickerId) throws Exception {

		Calendar nextQuoteDate = null;
		boolean firstTimeQuote = false;

		try {
			Calendar cal = Calendar.getInstance();

			// get the last quote date
			StockQuoteHistoryDao history = new StockQuoteHistoryDao();

			java.util.Date lastQuotedDate = history.getLastQuoteDate(tickerId);

			if (lastQuotedDate == null) {
				// never retrieved historical data for this stock before
				lastQuotedDate = getHistoryStartDate();
				firstTimeQuote = true;
			}

			cal.setTime(lastQuotedDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			// cal.set(Calendar.HOUR_OF_DAY, 0);
			// cal.set(Calendar.MINUTE, 0);
			// cal.set(Calendar.SECOND, 0);
			// cal.set(Calendar.MILLISECOND, 0);

			if (!firstTimeQuote) {
				cal.setTime(lastQuotedDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				// cal.set(Calendar.HOUR_OF_DAY, 0);
				// cal.set(Calendar.MINUTE, 0);
				// cal.set(Calendar.SECOND, 0);
				// cal.set(Calendar.MILLISECOND, 0);
			}

			// System.out.println("Last quote date: " +
			// lastQuotedDate.toString());
			// System.out.println("Tommorrow: " + sqlTommorow.toString());

			nextQuoteDate = cal;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nextQuoteDate;
	}

	public Long getStockTickerId(String stockTicker) throws SQLException, Exception {
		// get the the ticker id
		StockDao ticker = new StockDao();

		StockBO tickerBo = ticker.getStockTickerBySymbol(stockTicker);
		if (tickerBo == null) {
			throw new Exception("Unable to find ticker " + stockTicker);
		}
		Long tickerId = tickerBo.getId();
		if (tickerId == null) {
			throw new Exception("Unable to find ticker " + stockTicker);
		}
		return tickerId;
	}

	/**
	 * 
	 */
	public void updateAllHistory() {
		try {
			StockDao tickers = new StockDao();

			List<StockBO> listTickerBo = tickers.getStockTickersToTrack();
			for (StockBO tickerBo : listTickerBo) {
				// System.out.println("Stock name: " + tickerBo.getName());

				int daysProcessed = populateStockHistory(tickerBo.getId(), tickerBo.getSymbol(), tickerBo.getName());

				logger.debug("\tProcessed " + daysProcessed + " days of data");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int populateStockHistory(Long tickerId, String tickerSymbol, String stockName) {
		int daysProcessed = 0;
		String fromMonth = "";
		String fromDay = "";
		String fromYear = "";
		String filePath = "";

		logger.debug("Populating historical prices for " + stockName + ", Symbol: " + tickerSymbol + ", Ticker ID: "
				+ tickerId);

		try {
			Calendar nextQuoteDate;
			nextQuoteDate = getStartDateForTicker(tickerId);

			// we have to look back one day because the last quote date may not
			// have included the close price for that day. For example, if we
			// get the prices before the market closed
			nextQuoteDate.add(Calendar.DAY_OF_MONTH, -1);

			Calendar today = Calendar.getInstance();

			if (today.after(nextQuoteDate)) {
				fromMonth = StringUtils.intToString(nextQuoteDate.get(Calendar.MONTH), 2);
				fromDay = StringUtils.intToString(nextQuoteDate.get(Calendar.DAY_OF_MONTH), 2);
				fromYear = StringUtils.intToString(nextQuoteDate.get(Calendar.YEAR), 4);

				String toMonth = StringUtils.intToString(today.get(Calendar.MONTH), 2);
				String toDay = StringUtils.intToString(today.get(Calendar.DAY_OF_MONTH), 2);
				String toYear = StringUtils.intToString(today.get(Calendar.YEAR), 4);

				// get historical prices for the stock
				HistoricalPricesFromYahoo prices = new HistoricalPricesFromYahoo();

/*
				 List<String[]> historicalPrices = prices.getSingleStockPrices(tickerSymbol, nextQuoteDate.getTime(), today.getTime());
				
				 addPricesToDatabase(historicalPrices, tickerId, tickerSymbol, stockName);
*/

				filePath = "C:\\temp\\YahooPrices\\" + tickerSymbol + "-" + toYear + toMonth + toDay + ".csv";

				prices.saveHistoricalPricesToFile(tickerSymbol, fromMonth, fromDay, fromYear, toMonth, toDay, toYear,
						filePath);
				ParseYahooCsvFileQuotes yahooCsv = new ParseYahooCsvFileQuotes(filePath);

				StockQuoteHistoryBO quoteDataBean = null;

				try {
					// create an initialized history business object
					try {
						quoteDataBean = createStockQuoteHistoryBO(tickerId);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					StockQuoteHistoryDao historyDao = new StockQuoteHistoryDao();

					if (!yahooCsv.hasData()) {
						logger.debug("\t" + "No data found in " + filePath + " to be processed.");
					}

					while (yahooCsv.getNextCsvRow(quoteDataBean)) {
						System.out.print(".");

						historyDao.addTickerHistory(quoteDataBean);
						daysProcessed++;
					}
					logger.debug(" Processed " + daysProcessed + " days");
				} catch (Exception e) {
					logger.error("An error occurred processing historical prices");
					logger.error("Last quote date in DB: " + fromMonth + "/" + fromDay + "/" + fromYear);
					logger.error("File being processed: " + filePath);
					logger.error("Data being processed: " + quoteDataBean);

					e.printStackTrace();
				}
			} else {
				logger.info("No updates necessary for " + stockName);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (daysProcessed > 0) {
			logger.debug("Processed " + daysProcessed + " days of of data");
		}

		return daysProcessed;
	}

	
/*	
	public int BrokepopulateStockHistory(Long tickerId, String tickerSymbol, String stockName) {
		int daysProcessed = 0;
		// String fromMonth = "";
		// String fromDay = "";
		// String fromYear = "";
		// String filePath = "";

		logger.debug("Populating historical prices for " + stockName + ", Symbol: " + tickerSymbol + ", Ticker ID: "
				+ tickerId);
		try {
			Calendar nextQuoteDate;
			nextQuoteDate = getStartDateForTicker(tickerId);

			// we have to look back one day because the last quote date may not
			// have included the close price for that day. For example, if we
			// get the prices before the market closed

			// nextQuoteDate.add(Calendar.DAY_OF_MONTH, -1);

			Calendar today = Calendar.getInstance();

			if (today.after(nextQuoteDate)) {
				// fromMonth =
				// StringUtils.intToString(nextQuoteDate.get(Calendar.MONTH),
				// 2);
				// fromDay =
				// StringUtils.intToString(nextQuoteDate.get(Calendar.DAY_OF_MONTH),
				// 2);
				// fromYear =
				// StringUtils.intToString(nextQuoteDate.get(Calendar.YEAR), 4);
				//
				// String toMonth =
				// StringUtils.intToString(today.get(Calendar.MONTH), 2);
				// String toDay =
				// StringUtils.intToString(today.get(Calendar.DAY_OF_MONTH), 2);
				// String toYear =
				// StringUtils.intToString(today.get(Calendar.YEAR), 4);
				//
				// get historical prices for the stock
				HistoricalPricesFromYahoo prices = new HistoricalPricesFromYahoo();

				List<String[]> historicalPrices = prices.getLatestStockPrice(tickerSymbol, nextQuoteDate.getTime(),
						today.getTime());

				addPricesToDatabase(historicalPrices, tickerId, tickerSymbol, stockName);

				// filePath = "C:\\temp\\YahooPrices\\" + tickerSymbol + "-" +
				// toYear + toMonth + toDay + ".csv";
				//
				//
				// prices.saveHistoricalPricesToFile(tickerSymbol, fromMonth,
				// fromDay, fromYear, toMonth, toDay, toYear,
				// filePath);
				// ParseYahooCsvFileQuotes yahooCsv = new
				// ParseYahooCsvFileQuotes(filePath);
				//
				// StockQuoteHistoryBO quoteDataBean = null;
				//
				// try {
				// // create an initialized history business object
				// try {
				// quoteDataBean = createStockQuoteHistoryBO(tickerId);
				// } catch (SQLException e1) {
				// e1.printStackTrace();
				// }
				//
				// StockQuoteHistoryDao historyDao = new StockQuoteHistoryDao();
				//
				// if (!yahooCsv.hasData()) {
				// logger.debug("\t" + "No data found in " + filePath +
				// " to be processed.");
				// }
				//
				// while (yahooCsv.getNextCsvRow(quoteDataBean)) {
				// System.out.print(".");
				//
				// historyDao.addTickerHistory(quoteDataBean);
				// daysProcessed++;
				// }
				// logger.debug(" Processed " + daysProcessed + " days");
				// } catch (Exception e) {
				// logger.error("An error occurred processing historical
				// prices");
				// logger.error("Last quote date in DB: " + fromMonth + "/" +
				// fromDay + "/" + fromYear);
				// logger.error("File being processed: " + filePath);
				// logger.error("Data being processed: " + quoteDataBean);
				//
				// e.printStackTrace();
				// }
			} else {
				logger.info("No updates necessary for " + stockName);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (daysProcessed > 0) {
			logger.debug("Processed " + daysProcessed + " days of of data");
		}

		return daysProcessed;
	}
*/
	
/*
	private void addPricesToDatabase(List<String[]> historicalPrices, Long tickerId, String tickerSymbol,
			String stockName) {

//		StockQuoteHistoryBO quoteDataBean = new StockQuoteHistoryBO();

		// logging
		if (historicalPrices != null) {
			int size = historicalPrices.size();
			for (int index = 0; index < size; index++) {
				String[] stockPricesArray = historicalPrices.get(index);
				StringBuffer msg = new StringBuffer();
				for (String stockPrice : stockPricesArray) {
					msg.append(stockPrice + " ");
				}
				logger.debug(msg.toString());
			}
		}
		// logging

		// try {
		// // create an initialized history business object
		// try {
		// quoteDataBean = createStockQuoteHistoryBO(tickerId);
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		//
		// StockQuoteHistoryDao historyDao = new StockQuoteHistoryDao();
		//
		// if (!yahooCsv.hasData()) {
		// logger.debug("\t" + "No data found in " + filePath +
		// " to be processed.");
		// }
		//
		// while (yahooCsv.getNextCsvRow(quoteDataBean)) {
		// System.out.print(".");
		//
		// historyDao.addTickerHistory(quoteDataBean);
		// daysProcessed++;
		// }
		// logger.debug(" Processed " + daysProcessed + " days");
		// } catch (Exception e) {
		// logger.error("An error occurred processing historical prices");
		// logger.error("Last quote date in DB: " + fromMonth + "/" +
		// fromDay + "/" + fromYear);
		// logger.error("File being processed: " + filePath);
		// logger.error("Data being processed: " + quoteDataBean);
		//
		// e.printStackTrace();
	}
*/
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PopulateHistoricalPrices populate = new PopulateHistoricalPrices();

		populate.updateAllHistory();

	}

}
