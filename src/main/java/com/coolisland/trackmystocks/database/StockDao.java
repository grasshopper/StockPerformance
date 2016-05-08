package com.coolisland.trackmystocks.database;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StockDao {
	private static final Logger logger = LoggerFactory.getLogger(StockDao.class);

	public enum Ownership {
		BUY(true), SELL(false);

		private boolean value;

		private Ownership(boolean newValue) {
			value = newValue;
		}

		public boolean getBoolean() {
			return value;
		}

		@Override
		public String toString() {
			switch (this) {
			case BUY:
				logger.debug("Buy");
				break;
			case SELL:
				logger.debug("Sell");
				break;
			}
			return super.toString();
		}
	};

	private static final int INDEX_TICKER_TYPE = 3;

	// private static final String SELECT_SIMPLE_TICKERS_STMT =
	// "SELECT T.ID, T.SYMBOL, T.NAME, T.EXCHANGE, T.TRACK, T.OWN, T.TICKER_TYPE_ID, T.ACCOUNT_ID "
	// + "FROM TICKER T ";

	private static final String INSERT_TICKER_STMT = "INSERT INTO TICKER "
			+ " (id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url) "
			+ " VALUES "
			+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String ORDER_BY_OWN_ACCOUNT = "ORDER BY OWN DESC, ACCOUNT_ID";

	private static final String ORDER_BY_TICKER_ID = "ORDER BY T.ID ASC";

	private static final String SELECT_MAX_TICKER_ID_STMT = "select max(id) from ticker";

	private static final String SELECT_TICKER_STMT = "SELECT T.ID, T.SYMBOL, T.NAME, T.EXCHANGE, T.TRACK, T.OWN, T.ACCOUNT_ID, T.TICKER_TYPE_ID "
			+ "FROM TICKER T INNER JOIN ACCOUNT A ON A.ID = T.ACCOUNT_ID ";

	private static final String SELECT_TICKERS = "SELECT * FROM TICKER ORDER BY OWN DESC, ACCOUNT_ID";

	private static final String UPDATE_BY_ACCOUNT_AND_STOCK = "WHERE ACCOUNT_ID = ? AND ID = ? ";

	private static final String UPDATE_OWNERSHIP = "SET OWN = ? ";

	private static final String UPDATE_TICKERS_STMT = "UPDATE TICKER ";

	private static final String WHERE_ACCOUNT_OWNED = "WHERE T.OWN = TRUE AND T.ACCOUNT_ID = ? ";

	private static final String WHERE_ACCOUNT = "WHERE T.ACCOUNT_ID = ? ";

	private static final String WHERE_ID = "WHERE T.ID = ";

	private static final String WHERE_NOT_OWNED_BY_ACCOUNT = "WHERE T.OWN = FALSE AND T.ACCOUNT_ID = ? ";

	private static final String WHERE_NOT_TICKER_TYPE = "WHERE TICKER_TYPE_ID = ? ";

	private static final String WHERE_SYMBOL = "WHERE SYMBOL = ";

	private static final String WHERE_TICKER_TYPE = "WHERE TICKER_TYPE_ID = ? ";
	
	private static final String WHERE_TRACK_TICKER = "WHERE TRACK = TRUE ";

	private static final String SELECT_FOR_OWNED_ACCOUNT = SELECT_TICKER_STMT + WHERE_ACCOUNT_OWNED + ORDER_BY_TICKER_ID;

	private static final String SELECT_FOR_ACCOUNT = SELECT_TICKER_STMT + WHERE_ACCOUNT + ORDER_BY_TICKER_ID;

	private static final String SELECT_TICKER_FOR_ID = SELECT_TICKER_STMT + WHERE_ID;

	private static final String SELECT_TICKER_FOR_SYMBOL = SELECT_TICKER_STMT + WHERE_SYMBOL;

	private static final String SELECT_TICKERS_TO_TRACK = SELECT_TICKER_STMT + WHERE_TRACK_TICKER
			+ ORDER_BY_OWN_ACCOUNT;

	private static final String SELECT_UNOWNED_TICKERS_FOR_ACCOUNT = SELECT_TICKER_STMT + WHERE_NOT_OWNED_BY_ACCOUNT
			+ ORDER_BY_TICKER_ID;

	private static final String DELETE_BY_ID_TICKER_STMT = "DELETE FROM TICKER WHERE  ID = ?";

	private final DataBaseManager dbManager = DataBaseManager.getInstance();

	public StockDao() throws SQLException {
	}

	public StockBO createStock(StockBO stock) throws Exception {
		String method = "createStock";
		logger.debug("Starting " + method);
		
		PreparedStatement pstmt = null;
		int recInserted = 0;
		
		try {
			String sql = INSERT_TICKER_STMT;

			pstmt = dbManager.prepareStatement(sql);

			// Set the values
//			+ " (id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url) "
			Long id = getNextSequenceId();
			stock.setId(id);
			pstmt.setLong(1, stock.getId());
			pstmt.setString(2, stock.getSymbol());
			pstmt.setString(3, stock.getName());
			pstmt.setString(4, stock.getExchange());
			pstmt.setBoolean(5, stock.getTrack());
			pstmt.setBoolean(6, stock.getOwn());
			pstmt.setLong(7, stock.getAccountId());
			pstmt.setInt(8, stock.getTickerTypeId().intValue());
			pstmt.setString(9, stock.getGraphUrl());
			
//			logger.debug("SQL statement: " + pstmt.toString());

			recInserted = dbManager.executeInsert(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}
		
		if (recInserted > 0) {
			logger.debug("Exiting " + method + ". Record was inserted: " + stock);
		} else {
			logger.debug("Exiting " + method + ". Failed to insert record: " + stock);
			logger.debug("SQL statement: " + pstmt.toString());

			throw new Exception("Unable to insert record: " + stock);
		}
		
		return stock;
	}

	public List<StockBO> getAllNonIndexStockTickers() throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();

		PreparedStatement pstmt = null;
		try {
			ResultSet result = null;

			String sql = SELECT_TICKER_STMT + WHERE_NOT_TICKER_TYPE;

			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setInt(1, INDEX_TICKER_TYPE);

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					StockBO tickerBo = new StockBO(result);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("SQL statement: " + pstmt);

			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}

	public List<StockBO> getIndexStockTickers() throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();

		PreparedStatement pstmt = null;
		try {
			ResultSet result = null;

			String sql = SELECT_TICKER_STMT + WHERE_TICKER_TYPE;

			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setInt(1, INDEX_TICKER_TYPE);

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					StockBO tickerBo = new StockBO(result);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug("SQL statement: " + pstmt.toString());

			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}

	private Long getNextSequenceId()  throws SQLException {
		String method = "getNextSequenceId";
		logger.debug("Starting " + method);
		
		ResultSet result = null;
		PreparedStatement pstmt = null;
		Long nextId = new Long(-1L);

		try {
			String sql = SELECT_MAX_TICKER_ID_STMT;

			pstmt = dbManager.prepareStatement(sql);

//			logger.debug("SQL statement: " + pstmt.toString());

			result = pstmt.executeQuery();

			if (result != null) {
				while (result.next()) {
					nextId = result.getLong(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		nextId++;
		logger.debug("Exiting " + method + " with id: " + nextId);
		
		return nextId;
	}

	/**
	 * get all owned stocks for the specified account
	 * 
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public List<StockBO> getOwnedStockTickersForAccount(int accountId) throws SQLException {
		String sql = SELECT_FOR_OWNED_ACCOUNT;

		return getStockTickers(accountId, sql);
	}

	public List<StockBO> getStockTickersForAccount(int accountId) throws SQLException {
		String sql = SELECT_FOR_ACCOUNT;

		return getStockTickers(accountId, sql);
	}


	private List<StockBO> getStockTickers(int accountId, String sql) throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();
		ResultSet result = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setInt(1, accountId);

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					StockBO tickerBo = new StockBO(result);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug("SQL statement: " + pstmt.toString());

			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}
	
	
	public StockBO getStockTickerById(String stockId) throws SQLException {
		StockBO tickerBo = null;

		try {
			String query = SELECT_TICKER_FOR_ID + "'" + stockId + "'";

			ResultSet rs = DataBaseManager.getInstance().executeQuery(query);

			if (rs != null) {
				if (rs.first()) {
					tickerBo = new StockBO(rs);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerBo;
	}

	public StockBO getStockTickerBySymbol(String tickerSymbol) throws SQLException {
		StockBO tickerBo = null;

		try {
			String query = SELECT_TICKER_FOR_SYMBOL + "'" + tickerSymbol + "'";

//			logger.debug("query: " + query);
			
			ResultSet rs = DataBaseManager.getInstance().executeQuery(query);

			if (rs != null) {
//				logger.debug("rs: " + rs);
				
				if (rs.first()) {
					tickerBo = new StockBO(rs);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerBo;
	}

	public List<StockBO> getStockTickers() throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();

		try {
			ResultSet rs = DataBaseManager.getInstance().executeQuery(SELECT_TICKERS);

			if (rs != null) {
				// Fetch each row from the result set
				while (rs.next()) {
					StockBO tickerBo = new StockBO(rs);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}

	public List<StockBO> getStockTickersToTrack() throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();

		try {
			ResultSet rs = DataBaseManager.getInstance().executeQuery(SELECT_TICKERS_TO_TRACK);

			if (rs != null) {
				// Fetch each row from the result set
				while (rs.next()) {
					StockBO tickerBo = new StockBO(rs);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}

	public List<StockBO> getUnownedStockTickersForAccount(int accountId) throws SQLException {
		List<StockBO> tickerList = new ArrayList<StockBO>();
		ResultSet result = null;
		PreparedStatement pstmt = null;

		try {
			String sql = SELECT_UNOWNED_TICKERS_FOR_ACCOUNT;

			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setInt(1, accountId);

//			logger.debug("SQL statement: " + pstmt.toString());

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					StockBO tickerBo = new StockBO(result);
					tickerList.add(tickerBo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerList;
	}

	public void udateOwnership(int accountId, int stockId, Ownership buySell) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			String sql = UPDATE_TICKERS_STMT + UPDATE_OWNERSHIP + UPDATE_BY_ACCOUNT_AND_STOCK;

			pstmt = dbManager.prepareStatement(sql);

			// Set the values
			pstmt.setBoolean(1, buySell.getBoolean());
			pstmt.setBigDecimal(2, new BigDecimal(accountId));
			pstmt.setBigDecimal(3, new BigDecimal(stockId));

			dbManager.executeUpdate(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}
	}

	public boolean deleteStock(StockBO stock) throws SQLException {
		String method = "deleteStock";
		logger.debug("Starting " + method);

		PreparedStatement pstmt = null;

		String sql = DELETE_BY_ID_TICKER_STMT;

		pstmt = dbManager.prepareStatement(sql);

		pstmt.setInt(1, stock.getId().intValue());

		logger.debug("SQL statement: " + pstmt.toString());

		boolean result = pstmt.execute();

		logger.debug("result: " + result);

		if (result == false) {
			SQLWarning warnings = pstmt.getWarnings();
			warnings = DataBaseManager.getInstance().getConnection()
					.getWarnings();

			if (warnings != null) {
				logger.debug("Warnings: " + warnings);
				logger.debug(pstmt.toString());
			}

			logger.debug("Rows updated: " + pstmt.getUpdateCount());
		}

		return result;

	}

}
