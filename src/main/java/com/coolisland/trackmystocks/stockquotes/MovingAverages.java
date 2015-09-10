package com.coolisland.trackmystocks.stockquotes;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coolisland.trackmystocks.beans.PriceBean;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;
import com.coolisland.trackmystocks.utils.StringUtils;

public class MovingAverages {
	private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;

	private static final int ROWS_PER_PAGE = 15;
	private int rowsPrinted = 0;
	private static final int NUMBER_OF_DAYS_BACK = 100;

	private static final int DECIMAL_PT_DIGITS = 2;
	private static final int DATE_LEN = 10;

	private static final int OWNERSHIP_COL_LEN = 5;
	private static final String OWNERSHIP_FORMAT = "%-" + OWNERSHIP_COL_LEN
			+ "s ";

	private static final int ACCOUNT_COL_LEN = 28;
	private static final String ACCOUNT_FORMAT = "%-" + ACCOUNT_COL_LEN + "s ";

	private static final int STOCK_NAME_COL_LEN = 70;
	private static final String STOCK_NAME_FORMAT = "%-" + STOCK_NAME_COL_LEN
			+ "s ";

	private static final int STOCK_SYMBOL_COL_LEN = 6;
	private static final String STOCK_SYMBOL_FORMAT = " %-"
			+ STOCK_SYMBOL_COL_LEN + "s ";

	private static final int MOVING_AVG_LABEL_COL_LEN = 4;
	private static final String MOVING_AVG_DAY_LABEL_FORMAT = "%-"
			+ MOVING_AVG_LABEL_COL_LEN + "s ";

	private static final int WHOLE_NUM_LEN = 6;
	private static final String MOVING_AVG_200_DAY_FORMAT = "%" + WHOLE_NUM_LEN
			+ "." + DECIMAL_PT_DIGITS + "f ";
	private static final int MOVING_AVG_COL_LEN = MOVING_AVG_LABEL_COL_LEN
			+ WHOLE_NUM_LEN + 1;
	private static final String MOVING_AVG_HEADER_FORMAT = "%-"
			+ MOVING_AVG_COL_LEN + "." + MOVING_AVG_COL_LEN + "s ";

	private static final String CLOSE_PRICE_FORMAT = " %" + WHOLE_NUM_LEN + "."
			+ DECIMAL_PT_DIGITS + "f ";
	private static final int CLOSE_PRICE_COL_LEN = WHOLE_NUM_LEN + 1;
	private static final String CLOSE_PRICE_HEADER_FORMAT = "%-"
			+ CLOSE_PRICE_COL_LEN + "s ";

	private static final String CLOSE_DATE_FORMAT = " %" + DATE_LEN + "s";
	private static final int CLOSE_DATE_COL_LEN = DATE_LEN + 1;
	private static final String CLOSE_DATE_HEADER_FORMAT = "%-"
			+ CLOSE_DATE_COL_LEN + "s ";

	private static final int POSITION_COL_LEN = 8;
	private static final String POSITION_MSG_FORMAT = " %" + POSITION_COL_LEN
			+ "s ";
	private static final String POSITION_MSG_HEADER_FORMAT = " %-"
			+ POSITION_COL_LEN + "s ";

	private static final int EXPLANATION_COL_LEN = 18;
	private static final String EXPLANATION_MSG_FORMAT = " %"
			+ EXPLANATION_COL_LEN + "s ";
	private static final String EXPLANATION_MSG_HEADER_LABEL_FORMAT = " %-"
			+ EXPLANATION_COL_LEN + "s ";

	private static final int MOVING_AVG_DIRECTION_COL_LEN = NUMBER_OF_DAYS_BACK;
	private static final String MOVING_AVG_DIRECTION_FORMAT = " %"
			+ MOVING_AVG_DIRECTION_COL_LEN + "s ";
	private static final String MOVING_AVG_DIRECTION_HEADER_FORMAT = " %-"
			+ MOVING_AVG_DIRECTION_COL_LEN + "s ";

	private static final String MOVING_AVG_FORMAT = OWNERSHIP_FORMAT
			+ ACCOUNT_FORMAT + STOCK_NAME_FORMAT + STOCK_SYMBOL_FORMAT
			+ MOVING_AVG_DAY_LABEL_FORMAT + MOVING_AVG_200_DAY_FORMAT
			+ CLOSE_PRICE_FORMAT + CLOSE_DATE_FORMAT + POSITION_MSG_FORMAT
			+ EXPLANATION_MSG_FORMAT + MOVING_AVG_DIRECTION_FORMAT;

	private static final String HEADER_FORMAT = OWNERSHIP_FORMAT
			+ ACCOUNT_FORMAT + STOCK_NAME_FORMAT + STOCK_SYMBOL_FORMAT
			+ MOVING_AVG_HEADER_FORMAT + CLOSE_PRICE_HEADER_FORMAT
			+ CLOSE_DATE_HEADER_FORMAT + POSITION_MSG_HEADER_FORMAT
			+ EXPLANATION_MSG_HEADER_LABEL_FORMAT
			+ MOVING_AVG_DIRECTION_HEADER_FORMAT;

	private static final String MOVING_AVG_200_DAY_NO_INFO_LABEL_FORMAT = "%-80s ";
	private static final String MOVING_AVG_NO_INFO_FORMAT = OWNERSHIP_FORMAT
			+ ACCOUNT_FORMAT + STOCK_NAME_FORMAT + STOCK_SYMBOL_FORMAT
			+ MOVING_AVG_200_DAY_NO_INFO_LABEL_FORMAT;
	private static final int TWO_HUNDRED_DAY_MOVING_AVG = 200;

	private static final int RECENT_MOVING_AVERAGE_CROSSED_DAYS = 30;

	/**
	 * 
	 */
	private void printHeading() {

		final String colSeperatorChar = "_";
		final String ownership = "Own";
		final String account = "Account";
		final String stockName = "Stock / ETF Name";
		final String stockSymbol = "Ticker";
		final String movingAverage = "Average";
		final String closePrice = "Close";
		final String closeDate = "Date";
		final String movingAveragePosition = "Position";
		final String explanation = "Explanation";
		final String movingAverageHistory = "Moving Average History";

		System.out.println();
		String output = String.format(HEADER_FORMAT, ownership, account,
				stockName, stockSymbol, movingAverage, closePrice, closeDate,
				movingAveragePosition, explanation, movingAverageHistory);
		System.out.println(output);

		final String accountColSeperator = String.format(
				"%1$-" + ACCOUNT_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String ownershipColSeperator = String.format(
				"%1$-" + OWNERSHIP_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String stockNameColSeperator = String.format(
				"%1$-" + STOCK_NAME_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String stockTickerColSeperator = String.format(
				"%1$-" + STOCK_SYMBOL_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String movingAverageColSeperator = String.format(
				"%1$-" + MOVING_AVG_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String closePriceColSeperator = String.format(
				"%1$-" + CLOSE_PRICE_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String closeDateColSeperator = String.format(
				"%1$-" + CLOSE_DATE_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String recommendationColSeperator = String.format(
				"%1$-" + POSITION_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String explanationColSeperator = String.format(
				"%1$-" + EXPLANATION_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);
		final String movingAverageHistoryColSeperator = String.format(
				"%1$-" + MOVING_AVG_DIRECTION_COL_LEN + "s", " ").replace(" ",
				colSeperatorChar);

		output = String.format(HEADER_FORMAT, ownershipColSeperator,
				accountColSeperator, stockNameColSeperator,
				stockTickerColSeperator, movingAverageColSeperator,
				closePriceColSeperator, closeDateColSeperator,
				recommendationColSeperator, explanationColSeperator,
				movingAverageHistoryColSeperator);
		System.out.println(output);
	}

	public void updateMovingAverage(Long tickerId,
			MovingAverageValues avgValues) {
		
		List<PriceBean> prices = new ArrayList<PriceBean>();
		StockQuoteHistoryDao historyDao;
		int days = TWO_HUNDRED_DAY_MOVING_AVG;

		try {
			historyDao = new StockQuoteHistoryDao();

			prices = historyDao.getClosingPrices(tickerId, days);

			if (prices.size() > 0) {
				double movingAverage200 = 0;
				for (PriceBean price : prices) {
					movingAverage200 += new Double(price.getPrice()).doubleValue();
				}
				movingAverage200 /= days;

				avgValues.setMovingAverage(movingAverage200);
				avgValues.setClosePrice(new Double(prices.get(0).getPrice()).doubleValue());

				avgValues.simpleMovingAverageDirectionEval();

				if (avgValues.getClosePrice() < movingAverage200) {
					avgValues.setMovingAverageRecommendation("SELL!");
				} else if (avgValues.getClosePrice() > movingAverage200) {
					avgValues.setMovingAverageRecommendation("Above");
				} else {
					avgValues.setMovingAverageRecommendation("CROSSING!");
				}

				avgValues.setDataFound(true);
			} else {
				avgValues.setDataFound(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Double average(List<PriceBean> prices, int from, int to) {
		Double average = null;
		double tempAverage = 0;
		int numDays = to - from;

//		System.out.println("Calculating average moving price from: " + from + " to: " + to);
		
		if (prices.size() < (to - 1) || prices.size() < from || from < 0 || from > to || numDays < 0) {
			return average;
		}

		
//		System.out.println("From day: " + prices.get(from) + ", To Day: " + prices.get(to));
		
		for (int ndx = from; ndx < to; ndx++) {
			tempAverage += new Double(prices.get(ndx).getPrice()).doubleValue();
		}

		tempAverage /= numDays;

		return average = new Double(tempAverage);
	}

	
	public List<java.util.Date> getAll200DaySimpleMovingAverageCrossedUpRecent(Long tickerId) {
		return null;
	}
	
	
	public List<java.util.Date> getAll200DaySimpleMovingAverageCrossedUp(Long tickerId) {
		List<java.util.Date> datesClosingPriceCrossedMovingAverage = new ArrayList<java.util.Date>();
		
		StockQuoteHistoryDao historyDao = null;
		try {
			historyDao = new StockQuoteHistoryDao();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return datesClosingPriceCrossedMovingAverage;
		}

		/*
		 * how many days of prices do we have?
		 */
		int numHistoricalDays = 0;
		try {
			numHistoricalDays = historyDao.getNumberOfDays(tickerId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("Number of days with prices: " + numHistoricalDays);
		
		/*
		 * get all the closing prices
		 */
		List<PriceBean> allClosingPrices = new ArrayList<PriceBean>();
		try {
			allClosingPrices = historyDao.getClosingPrices(tickerId, numHistoricalDays);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("Retrieved prices for " + allClosingPrices.size() + " days");
		
		/*
		 * calculate the moving average
		 */
		List<PriceBean> averages = new ArrayList<PriceBean>();
		for (int day = numHistoricalDays - 1; day > TWO_HUNDRED_DAY_MOVING_AVG; day--) {
			Double movingAvg = average(allClosingPrices, day - TWO_HUNDRED_DAY_MOVING_AVG, day);
			
//			System.out.println("day: " + day + ", numHistoricalDays: " + numHistoricalDays + ", movingAvg: " + movingAvg + ", ClosingPrices[day]: " + allClosingPrices.get(day));
			
			PriceBean average = new PriceBean(movingAvg.toString(), allClosingPrices.get(day).getPriceDate());
			averages.add(average);
		}
		
//		System.out.println("Number of moving averages found: " + averages.size());
		
		/*
		 * find the dates when the close price crosses the moving average
		 */
		if(!averages.isEmpty()) {
			int numPrices = averages.size();
			Double previousClosePrice = new Double(-1);
			Double previousAveragePrice = new Double(-1);
			
			
			for (int index = 0; index < numPrices; index++) {
				String closingPriceStr = allClosingPrices.get(index).getPrice();
				String averagePriceStr = averages.get(index).getPrice();
				
				Double closingPrice = new Double(closingPriceStr);
				Double averagePrice = new Double(averagePriceStr);
				
//				System.out.println("index: " + index + ", date: " + allClosingPrices.get(index).getPriceDate() 
//						+ ", close price: " + closingPrice + ", average price: " + averagePrice 
//						+ ", previous day closing price: " + previousClosePrice 
//						+ ", previous day average price: " + previousAveragePrice);
				
				if ((previousClosePrice < previousAveragePrice)
						&& (closingPrice >= averagePrice)) {
					
					// close price just crossed moving average
					datesClosingPriceCrossedMovingAverage.add(allClosingPrices.get(index).getPriceDate());
					
//					System.out.println("CLOSED PRICE HAS CROSSED MOVING AVERAGE!!!!");
				}
				
				// update previous to today's
				previousAveragePrice = averagePrice;
				previousClosePrice = closingPrice;
			}
		}
		
		return datesClosingPriceCrossedMovingAverage;
	}


	public List<java.util.Date> get200DaySimpleMovingAverageCrossedUp(Long tickerId, int daysInPast) {
		List<java.util.Date> datesClosingPriceCrossedMovingAverage = new ArrayList<java.util.Date>();
		
		StockQuoteHistoryDao historyDao = null;
		try {
			historyDao = new StockQuoteHistoryDao();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return datesClosingPriceCrossedMovingAverage;
		}

		/*
		 * how many days of prices do we have?
		 */
		int numHistoricalDays = 0;
		try {
			numHistoricalDays = historyDao.getNumberOfDays(tickerId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("Number of days with prices: " + numHistoricalDays);
		
		/*
		 * get the closing prices for daysInPast
		 */
		List<PriceBean> allClosingPrices = new ArrayList<PriceBean>();
		try {
			allClosingPrices = historyDao.getClosingPrices(tickerId, numHistoricalDays);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		System.out.println("Retrieved prices for " + allClosingPrices.size() + " days");
		
		/*
		 * calculate the moving average
		 */
		List<PriceBean> averages = new ArrayList<PriceBean>();
		for (int day = numHistoricalDays - 1; day > TWO_HUNDRED_DAY_MOVING_AVG; day--) {
			Double movingAvg = average(allClosingPrices, day - TWO_HUNDRED_DAY_MOVING_AVG, day);
			
//			System.out.println("day: " + day + ", numHistoricalDays: " + numHistoricalDays + ", movingAvg: " + movingAvg + ", ClosingPrices[day]: " + allClosingPrices.get(day));
			
			PriceBean average = new PriceBean(movingAvg.toString(), allClosingPrices.get(day).getPriceDate());
			averages.add(average);
		}
		
//		System.out.println("Number of moving averages found: " + averages.size());
		
		/*
		 * find the dates when the close price crosses the moving average within the last daysInPast
		 */
		if(!averages.isEmpty()) {
			int numPrices = averages.size();
			Double previousClosePrice = new Double(-1);
			Double previousAveragePrice = new Double(-1);
			java.util.Date dayInPast = new java.util.Date();
			dayInPast.setTime(dayInPast.getTime() - daysInPast * MILLISECONDS_IN_DAY);
			
			
			for (int index = 0; index < numPrices; index++) {
				String closingPriceStr = allClosingPrices.get(index).getPrice();
				String averagePriceStr = averages.get(index).getPrice();
				
				Double closingPrice = new Double(closingPriceStr);
				Double averagePrice = new Double(averagePriceStr);
				java.util.Date closePriceDate = allClosingPrices.get(index).getPriceDate();
				
				
//				System.out.println("index: " + index + ", date: " + allClosingPrices.get(index).getPriceDate() 
//						+ ", close price: " + closingPrice + ", average price: " + averagePrice 
//						+ ", previous day closing price: " + previousClosePrice 
//						+ ", previous day average price: " + previousAveragePrice);
				
				if ((previousClosePrice < previousAveragePrice)
						&& (closingPrice >= averagePrice) && closePriceDate.compareTo(dayInPast) >= 0) {
					
					// close price just crossed moving average
					datesClosingPriceCrossedMovingAverage.add(closePriceDate);
					
//					System.out.println("CLOSED PRICE HAS CROSSED MOVING AVERAGE!!!!");
				}
				
				// update previous to today's
				previousAveragePrice = averagePrice;
				previousClosePrice = closingPrice;
			}
		}
		
		return datesClosingPriceCrossedMovingAverage;
	}
	

	
	/**
	 * 
	 * @param tickerId
	 * @param stockSymbol
	 * @param stockName
	 * @param numberDays
	 * @return
	 */
	private List<Double> getMovingAverageHistory(Long tickerId,
			int numberDaysBack) {
		numberDaysBack++;
		List<PriceBean> prices = new ArrayList<PriceBean>();
		StockQuoteHistoryDao historyDao;
		int movingAvgDays = TWO_HUNDRED_DAY_MOVING_AVG;
		int days = movingAvgDays + numberDaysBack;
		List<Double> averages = new ArrayList<Double>();

		try {
			historyDao = new StockQuoteHistoryDao();

			prices = historyDao.getClosingPrices(tickerId, days);

			for (int day = 0; day < numberDaysBack; day++) {
				Double movingAvg = average(prices, day, day + TWO_HUNDRED_DAY_MOVING_AVG + 1);
				averages.add(movingAvg);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return averages;
	}


	private Date getLastClosePrice(Long tickerId) {
		StockQuoteHistoryDao historyDao;
		Date closeDate = null;

		try {
			historyDao = new StockQuoteHistoryDao();

			closeDate = historyDao.getLastCloseDate(tickerId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return closeDate;
	}

	/**
	 * 
	 * @param averages
	 */
	private String getMovingAverageHistorySummation(List<Double> averages) {
		StringBuffer summationOut = new StringBuffer();
		StringBuffer differenceOut = new StringBuffer();

		if (averages == null || averages.size() < 1 || averages.get(0) == null) {
			return summationOut.toString();
		}

		// get first price
		double previousAvg = averages.get(0);

		for (int ndx = 1; ndx < averages.size(); ndx++) {
			double current = 0;

			if (averages.get(ndx) == null) {
				summationOut.append(" ");
			} else {
				current = averages.get(ndx);
			}

			// get the difference between the current and the previous price
			double diff = previousAvg - current;
			String numFormat = "%" + WHOLE_NUM_LEN + "." + DECIMAL_PT_DIGITS
					+ "f ";
			String diffStr = String.format(numFormat, diff);
			double diffDouble = new Double(diffStr).doubleValue();

			if (diffDouble > 0) {
				summationOut.append(MovingAverageValues.Direction.UP);
			} else if (diffDouble < 0) {
				summationOut.append(MovingAverageValues.Direction.DOWN);
			} else {
				summationOut.append(MovingAverageValues.Direction.NO_CHANGE);
			}

			/*
			 * if (current < previousAvg) {
			 * summationOut.append(MovingAverageValues.Direction.UP); } else if
			 * (current > previousAvg) {
			 * summationOut.append(MovingAverageValues.Direction.DOWN); } else {
			 * summationOut.append(MovingAverageValues.Direction.NO_CHANGE); }
			 */

			differenceOut.append(String.format(numFormat, diffDouble));
		}

		// summationOut.append("\n");
		// summationOut.append(differenceOut.toString());

		return summationOut.toString();
	}

	/**
	 * 
	 */
	public void getAll200DayAverages() {
		try {
			StockDao tickers = new StockDao();

			List<StockBO> listTickerBo = tickers.getStockTickersToTrack();

			rowsPrinted = ROWS_PER_PAGE;
			for (StockBO tickerBo : listTickerBo) {
				// Print Column Header?
				if (rowsPrinted >= ROWS_PER_PAGE) {
					printHeading();
					rowsPrinted = 0;
				}
				rowsPrinted++;

				MovingAverageValues values = new MovingAverageValues();
				values.setStockName(tickerBo.getName());
				values.setTickerSymbol(tickerBo.getSymbol());

				List<Double> movingAverageHistory = getMovingAverageHistory(
						tickerBo.getId(), NUMBER_OF_DAYS_BACK);
				String historyStr = getMovingAverageHistorySummation(movingAverageHistory);
				values.setMovingAverageHistory(historyStr);

				updateMovingAverage(tickerBo.getId(), values);

				Date closePrice = getLastClosePrice(tickerBo.getId());

				String output;
				String own = tickerBo.getOwn() ? "Yes" : "No";

				if (values.isDataFound()) {
					List<java.util.Date> movingAverageCrossedDates = getAll200DaySimpleMovingAverageCrossedUp(tickerBo.getId());
					List<java.util.Date> recentMovingAverageCrossedDates = get200DaySimpleMovingAverageCrossedUp(tickerBo.getId(), RECENT_MOVING_AVERAGE_CROSSED_DAYS);
					
					output = getStockReport(tickerBo, values, movingAverageCrossedDates, recentMovingAverageCrossedDates, closePrice, own);
				} else {
					String stockName = values.getStockName();
					stockName = StringUtils.truncateString(stockName,
							STOCK_NAME_COL_LEN);

					Long account = tickerBo.getAccountId();

					output = String.format(MOVING_AVG_NO_INFO_FORMAT, own,
							account, stockName, values.getTickerSymbol(),
							"No prices found!");
				}

				System.out.println(output);
//				System.out
//						.println("http://finance.yahoo.com/q/ta?s=SPZ12.CME+Basic+Tech.+Analysis");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getStockReport(StockBO tickerBo, MovingAverageValues movingAvgValues,  
			List<java.util.Date> movingAverageCrossedDates, List<java.util.Date> recentMovingAvgValues,
			Date closePrice, String own) {
		String output = null;
		String stockName = movingAvgValues.getStockName();

		if (tickerBo == null) {
			return output;
		}
		
		if (movingAvgValues == null) {
			return output;
		}
		
		if (stockName.length() > 60) {
			stockName = StringUtils.truncateString(stockName,
					STOCK_NAME_COL_LEN);
		}

		Long accountId = tickerBo.getAccountId();
		String accountName = getAccountName(accountId);

		output = String.format(MOVING_AVG_FORMAT, own, accountName,
				stockName, movingAvgValues.getTickerSymbol(), "200",
				movingAvgValues.getMovingAverage(), movingAvgValues.getClosePrice(),
				closePrice.toString(),
				movingAvgValues.getMovingAverageRecommendation(),
				movingAvgValues.getDirection(),
				movingAvgValues.getMovingAverageHistory());
		
		if (movingAverageCrossedDates != null && movingAverageCrossedDates.size() > 0) {
			output += "\n"; 
			output += "^"; 
			for (java.util.Date day : movingAverageCrossedDates) {
				output += "\t" + day;
			}
		}
		
		if (recentMovingAvgValues != null && recentMovingAvgValues.size() > 0) {
			output += "\n"; 
			output += "!^=======> "; 
			for (java.util.Date day : movingAverageCrossedDates) {
				output += "\t" + day;
			}
		}
		
		return output;
	}

	private String getAccountName(Long accountId) {
		AccountDao dao = new AccountDao();
		
		String accountName = null;
		try {
			accountName = dao.getAccountName(accountId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MovingAveragesTester averages = new MovingAveragesTester();

		try {
			averages.getAll200DayAverages();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
