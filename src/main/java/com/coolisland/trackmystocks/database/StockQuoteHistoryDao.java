package com.coolisland.trackmystocks.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

import com.coolisland.trackmystocks.beans.PriceBean;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class StockQuoteHistoryDao {

	// private static final String SELECT_STATMENT =
	// "SELECT * FROM STOCK_QUOTE_HISTORY";
	private static final String INSERT_STATMENT = "INSERT INTO STOCK_QUOTE_HISTORY ";
	
	private static final String INSERT_ALL_COLUMNS = "(TICKER_ID, QUOTE_DATE, LAST_TRADE_AMOUNT, "
			+ "LAST_TRADE_DATE_TIME, CHANGE_AMOUNT, OPEN_AMOUNT, DAY_HIGH_AMOUNT, DAY_LOW_AMOUNT, "
			+ "VOLUME, PREVIOUS_CLOSE, CHANGE_PERCENT, FIFTY_TWO_WEEK_RANGE, EARNING_PER_SHARE, "
			+ "PRICE_PER_EARNINGS, AVERAGE_DAILY_VOLUME) ";

	private static final String DELETE_STOCK_HISTORY = "delete from stock_quote_history "
			+ " where TICKER_ID - ?";
	
	private static final String SELECT_SIMPLE_200_DAY_AVG = "SELECT SUM(LAST_TRADE_AMOUNT) / 200 "
			+ "FROM (SELECT LAST_TRADE_AMOUNT FROM STOCK_QUOTE_HISTORY WHERE TICKER_ID = ? "
			+ "ORDER BY last_trade_date_time DESC LIMIT 200) AS SUBQUERY";

	private static final String SELECT_SIMPLE_200_DAY_AVG_FOR_DATE = "SELECT SUM(LAST_TRADE_AMOUNT) / 200 "
			+ "FROM (SELECT LAST_TRADE_AMOUNT FROM STOCK_QUOTE_HISTORY WHERE TICKER_ID = ? "
			+ "AND last_trade_date_time < ? "
			+ "ORDER BY last_trade_date_time DESC LIMIT 200) AS SUBQUERY";
	
	
	private static final String SELECT_LAST_TRADE_AMOUNT_VARIABLE = "SELECT LAST_TRADE_AMOUNT, LAST_TRADE_DATE_TIME "
			+ "FROM STOCK_QUOTE_HISTORY WHERE TICKER_ID = ? ORDER BY last_trade_date_time ASC LIMIT ?";

	// Getting the last quote date
	private static final String SELECT_LAST_QUOTE_STATEMENT = "SELECT MAX(QUOTE_DATE) FROM STOCK_QUOTE_HISTORY ";
	private static final String WHERE_LAST_QUOTE_STATEMENT = "WHERE TICKER_ID = ";

	// Getting the last quote date
	private static final String SELECT_NUMBER_HISTORICAL_PRICES = "SELECT COUNT(*) FROM STOCK_QUOTE_HISTORY WHERE TICKER_ID = ?";

	// Getting the last close date
	private static final String SELECT_LAST_CLOSE_STATMENT = "SELECT MAX(LAST_TRADE_DATE_TIME) FROM STOCK_QUOTE_HISTORY ";
	private static final String WHERE_LAST_CLOSE_STATEMENT = "WHERE TICKER_ID = ";

	private static final int TWO_HUNDRED_DAY_MOVING_AVG = 200;

	private static String INSERT_ALL_VALUES = "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final DataBaseManager dbManager = DataBaseManager.getInstance();

	public StockQuoteHistoryDao() throws SQLException {
	}

	public boolean addTickerHistory(StockQuoteHistoryBO quote) throws SQLException {
		int success = 0;
		String sql = INSERT_STATMENT + INSERT_ALL_COLUMNS + INSERT_ALL_VALUES;

		PreparedStatement pstmt = null;
		try {
			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setLong(1, quote.getTickerId());
			pstmt.setTimestamp(2, quote.getQuoteSqlDate());
			pstmt.setString(3, quote.getLastTradeAmountAsString());
			pstmt.setTimestamp(4, quote.getLastTradeSqlDateTime());
			pstmt.setString(5, quote.getChangeAmountAsString());
			pstmt.setString(6, quote.getOpenAmountAsString());
			pstmt.setString(7, quote.getDayHighAmountAsString());
			pstmt.setString(8, quote.getDayLowAmountAsString());
			pstmt.setString(9, quote.getVolumeAsString());
			pstmt.setString(10, quote.getPreviousCloseAsString());
			pstmt.setString(11, quote.getChangePercentAsString());
			pstmt.setString(12, quote.getFiftyTwoWeekRange());
			pstmt.setString(13, quote.getEarningsPerShareAsString());
			pstmt.setString(14, quote.getPricePerEarningsAsString());
			pstmt.setString(15, quote.getVolumeAsString());

			success = dbManager.executeInsert(pstmt);
		} catch (MySQLIntegrityConstraintViolationException e) {
			// this is ok
			// System.out.println("duplicate entry... this is ok");
		} catch (SQLException e) {
			if ("23000".equals(e.getSQLState())) {
				// duplicate entry... ignore and continue
				success = 1;
			}
			e.printStackTrace();
			System.out.println(quote.toString());
			System.out.println(pstmt);

			throw new SQLException(e.getMessage(), e.getCause());
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println(quote.toString());

			throw new NullPointerException(e.getMessage());
		} finally {
			// System.out.println(pstmt.toString());
		}

		return success > 0 ? true : false;
	}

	public Date getLastQuoteDate(Long tickerId) throws SQLException {
		Date lastDate = null;
		String sql = SELECT_LAST_QUOTE_STATEMENT + WHERE_LAST_QUOTE_STATEMENT;
		ResultSet result = null;

		sql += tickerId;

		// System.out.println("SQL statement: " + sql);

		result = dbManager.executeQuery(sql);

		if (result.first()) {
			lastDate = result.getDate(1);
		}

		System.out.println("Last quote date for ticker id " + tickerId + " is: " + lastDate);

		return lastDate;
	}

	public Double get200DaySimpleMovingAverage(BigDecimal tickerId) throws SQLException {
		Double average = null;
		String sql = SELECT_SIMPLE_200_DAY_AVG;
		ResultSet result = null;

		sql += tickerId;

		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setBigDecimal(1, tickerId);

		// System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

		if (result.first()) {
			average = result.getDouble(1);
		}

		return average;
	}
	
	
	/**
	 * Gets the moving average for a specified stock for a specified date 
	 * 
	 * @param tickerId
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public Double get200DaySimpleMovingAverageForDate(BigDecimal tickerId, java.util.Date date) throws SQLException {
//		String method = "get200DaySimpleMovingAverageForDate";
		
		Double average = null;
		String sql = SELECT_SIMPLE_200_DAY_AVG_FOR_DATE;
		ResultSet result = null;

//		System.out.println("Starting " + method);
//		System.out.println("tickerId: " + tickerId);
//		System.out.println("date: " + date);
		
		
		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setBigDecimal(1, tickerId);
		
		Date sqlDate = new Date(date.getTime());
		pstmt.setDate(2, sqlDate);

//		System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

//		System.out.println("result: " + result.toString());

		if (result.first()) {
			average = result.getDouble(1);
		}

		return average;
	}

	
	public List<PriceBean> getClosingPrices(Long tickerId, int numDays) throws SQLException {
		String sql = SELECT_LAST_TRADE_AMOUNT_VARIABLE;
		ResultSet result = null;
		List<PriceBean> closingPrices = new ArrayList<PriceBean>();

		PreparedStatement pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, tickerId);
		pstmt.setInt(2, numDays);

//		System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

		while (result.next()) {
			PriceBean price = new PriceBean(new Double(result.getDouble(1)).toString(), result.getDate(2));
			closingPrices.add(price);
		}

		return closingPrices;
	}

	public Date getLastCloseDate(Long tickerId) throws SQLException {
		Date lastDate = null;
		String sql = SELECT_LAST_CLOSE_STATMENT + WHERE_LAST_CLOSE_STATEMENT;
		ResultSet result = null;

		sql += tickerId;

		// System.out.println("SQL statement: " + sql);

		result = dbManager.executeQuery(sql);

		if (result.first()) {
			lastDate = result.getDate(1);
		}

		// System.out.println("Last close date for ticker id " + tickerId +
		// " is: " + lastDate);

		return lastDate;
	}

	
	public void deleletStockPriceHistory(Long tickerId) throws SQLException {
		String sql = DELETE_STOCK_HISTORY;

		PreparedStatement pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, tickerId);

//		System.out.println("SQL statement: " + pstmt.toString());

		boolean result = pstmt.execute();

		System.out.println("result: " + result);
		
		if (result == false) {
			SQLWarning warnings = pstmt.getWarnings();
			warnings = DataBaseManager.getInstance().getConnection().getWarnings();
			
			if (warnings != null) {
				System.out.println("Warnings: " + warnings);
				System.out.println(pstmt.toString());
			}
			
			System.out.println("Rows updated: " + pstmt.getUpdateCount());
		}
	}


	public int getNumberOfDays(Long tickerId) throws SQLException {
		String method = "getNumberOfDays";
		String sql = SELECT_NUMBER_HISTORICAL_PRICES;
		ResultSet result = null;
		int numberOfDays = 0;

//		System.out.println("Starting " + method);
//		System.out.println("tickerId: " + tickerId);
		
		
		PreparedStatement pstmt = null;
		pstmt = dbManager.prepareStatement(sql);

		// Set the values
		pstmt.setLong(1, tickerId);
		
//		System.out.println("SQL statement: " + pstmt.toString());

		result = pstmt.executeQuery();

//		System.out.println("result: " + result.toString());

		if (result.first()) {
			numberOfDays = result.getInt(1);
		}

		return numberOfDays;
	}
}
