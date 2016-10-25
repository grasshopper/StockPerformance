package com.coolisland.trackmystocks.beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;
import com.coolisland.trackmystocks.database.StockQuoteHistoryDao;


public class StockPriceHistoryBean extends StockBO {
	List<PriceBean> movingAverage = new ArrayList<PriceBean>();
	
	public StockPriceHistoryBean(String stockId, int numDays) {
		// initialize StockBO
		try {
			StockDao dao = new StockDao();
			StockBO stock = dao.getStockTickerById(stockId);
			
			if (stock == null) {
				System.out.println("Did not find stock record for stockID: " + stockId);
			} else {
				this.load(stock);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// initialize the moving average
		movingAverage.clear();
		try {
			StockQuoteHistoryDao dao = new StockQuoteHistoryDao();
			List<PriceBean> closingPrices = dao.getClosingPrices(new Long(stockId), numDays);
			movingAverage.addAll(closingPrices);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the movingAverage
	 */
	public List<PriceBean> getMovingAverage() {
		return movingAverage;
	}

	/**
	 * @param movingAverage the movingAverage to set
	 */
	public void setMovingAverage(List<PriceBean> movingAverage) {
		this.movingAverage = movingAverage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockPriceHistoryBean [movingAverage=" + movingAverage + "]";
	}
}
