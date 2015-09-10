package com.coolisland.trackmystocks.yahoo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.utils.StringUtils;

public class PopulateHistoricalPrices {
	
	
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

				System.out.println("\tProcessed " + daysProcessed + " days of data");
				System.out.println();
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

		System.out.println("Populating historical prices for " + stockName + ", Symbol: " + tickerSymbol + ", Ticker ID: " + tickerId);
		try {
			Calendar nextQuoteDate;
			nextQuoteDate = getStartDateForTicker(tickerId);

			// we have to look back one day because the last quote date may not
			// have included the close price for that day. For example, if we
			// get the prices before the market closed

			// nextQuoteDate.add(Calendar.DAY_OF_MONTH, -1);

			Calendar today = Calendar.getInstance();

			if (today.after(nextQuoteDate)) {
				// nextQuoteDate.add(Calendar.MONTH, 1);
				// today.add(Calendar.MONTH, +1);

				fromMonth = StringUtils.intToString(nextQuoteDate.get(Calendar.MONTH), 2);
				fromDay = StringUtils.intToString(nextQuoteDate.get(Calendar.DAY_OF_MONTH), 2);
				fromYear = StringUtils.intToString(nextQuoteDate.get(Calendar.YEAR), 4);

				String toMonth = StringUtils.intToString(today.get(Calendar.MONTH), 2);
				String toDay = StringUtils.intToString(today.get(Calendar.DAY_OF_MONTH), 2);
				String toYear = StringUtils.intToString(today.get(Calendar.YEAR), 4);

				filePath = "C:\\temp\\YahooPrices\\" + tickerSymbol + "-" + toYear + toMonth + toDay + ".csv";

				HistoricalPricesFromYahoo prices = new HistoricalPricesFromYahoo();
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
						System.out.println("\t" + "No data found in " + filePath + " to be processed.");
					}
					
					while (yahooCsv.getNextCsvRow(quoteDataBean)) {
						System.out.print(".");

						historyDao.addTickerHistory(quoteDataBean);
						daysProcessed++;
					}
					System.out.println(" Processed " + daysProcessed + " days");
				} catch (Exception e) {
					System.out.println("An error occurred processing historical prices");
					System.out.println("Last quote date in DB: " + fromMonth + "/" + fromDay + "/" + fromYear);
					System.out.println("File being processed: " + filePath);
					System.out.println("Data being processed: " + quoteDataBean);
					
					e.printStackTrace();
				}
			} else {
				System.out.println("No updates necessary for " + stockName);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (daysProcessed > 0) {
			System.out.println();
		}
		
		return daysProcessed;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PopulateHistoricalPrices populate = new PopulateHistoricalPrices();

		populate.updateAllHistory();

	}

}
