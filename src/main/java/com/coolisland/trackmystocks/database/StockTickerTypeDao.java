package com.coolisland.trackmystocks.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockTickerTypeDao {
	private final DataBaseManager dbManager = DataBaseManager.getInstance();

	final static String SELECT_STOCK_TICKER_TYPES = "SELECT ID, TYPE, DESCRIPTION FROM TICKER_TYPE";

	public StockTickerTypeDao() throws SQLException {
	}

	
	public List<StockTickerTypeBO> getStockTickerTypes() throws SQLException {
		List<StockTickerTypeBO> tickerTypeBoList = new ArrayList<StockTickerTypeBO>();

		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			pstmt = dbManager.prepareStatement(SELECT_STOCK_TICKER_TYPES);

			result = pstmt.executeQuery();

			if (result != null) {
				// Fetch each row from the result set
				while (result.next()) {
					StockTickerTypeBO tickerTypeBo = new StockTickerTypeBO(result);
					tickerTypeBoList.add(tickerTypeBo);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}

		return tickerTypeBoList;
	}


}
