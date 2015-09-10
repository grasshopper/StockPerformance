package com.coolisland.trackmystocks.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.coolisland.trackmystocks.utils.MovingAveragePeriod;

public class StockPriceHistoryListBean {
	HashMap<String, StockPriceHistoryBean> stockPriceHistoryMap = new HashMap<String, StockPriceHistoryBean>();
	Date historyFromDate = null;
	Date historyToDate = null;
	MovingAveragePeriod movingAverage = null;

	public StockPriceHistoryListBean() {
		Calendar cal = new GregorianCalendar();

		historyToDate = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		historyFromDate = cal.getTime();
	}

	/**
	 * 
	 * @param stockIds
	 * @param newMovingAvg
	 */
	public void update(List<String> stockIds, MovingAveragePeriod newMovingAvg) {
		String method = "update";
		System.out.println("Starting " + method);
		
		boolean refreshData = false; 
		
		if ((movingAverage == null)
				|| (newMovingAvg.getId() != movingAverage.getId())) {
			// new number of days to use in moving average.
			// we'll have to re-generate the moving average data
			initMovingAverage(newMovingAvg);
			
			refreshData = true;
		}
		
		// remove stocks from the map that are not in the stocksIds list
		removeOldStocksFromList(stockIds);

		// add new stocks to the map that we will track
		List<String> stocksToAddToMap = addNewStocksToList(stockIds);
		
		System.out.println("stocksToAddToMap: " + stocksToAddToMap);
		
		// get historical prices for the new stocks being tracked
		for (String tickerId : stocksToAddToMap) {
			System.out.println("NOT USING THE SELECTED PERIOD...");
			
			StockPriceHistoryBean priceHistory = new StockPriceHistoryBean(tickerId, newMovingAvg.getId());
			
			System.out.println("priceHistory: " + priceHistory);
			
			stockPriceHistoryMap.put(tickerId, priceHistory);
		}
		
		if (refreshData) {
			refreshHistory();
		}

		
		System.out.println("Exiting " + method);
	}

	private void refreshHistory() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Remove stocks from stockPriceHistoryMap that we are no longer tracking
	 * 
	 * @param stockIds
	 */
	private void removeOldStocksFromList(List<String> stockIds) {
		Iterator<Entry<String, StockPriceHistoryBean>> it = stockPriceHistoryMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, StockPriceHistoryBean> pairs = it.next();

			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			// are we still tracking this stock
			boolean stillTracking = false;
			for (String trackStockId : stockIds) {
				if (trackStockId.equals(pairs.getKey())) {
					stillTracking = true;
					break;
				}
			}
			if (!stillTracking) {
				System.out.println("Removing stock from list: "
						+ pairs.getKey() + " - " + pairs.getValue());
				it.remove();
			}
		}
	}

	private List<String> addNewStocksToList(List<String> stockIds) {
		List<String> newStockIdsToTrack = new ArrayList<String>();

		for (String trackStockId : stockIds) {
			boolean alreadyTracking = false;
			if (stockPriceHistoryMap.containsKey(trackStockId)) {
				alreadyTracking = true;
			}

			if (!alreadyTracking) {
				newStockIdsToTrack.add(trackStockId);

				// add the stock to the map with no history
				stockPriceHistoryMap.put(trackStockId, null);
			}
		}

		return newStockIdsToTrack;
	}

	private void initMovingAverage(MovingAveragePeriod newMovingAvg) {
		movingAverage = newMovingAvg;
	}


	/**
	 * @return the historyFromDate
	 */
	public Date getHistoryFromDate() {
		return historyFromDate;
	}

	/**
	 * @param historyFromDate the historyFromDate to set
	 */
	public void setHistoryFromDate(Date historyFromDate) {
		this.historyFromDate = historyFromDate;
	}

	/**
	 * @return the historyToDate
	 */
	public Date getHistoryToDate() {
		return historyToDate;
	}

	/**
	 * @param historyToDate the historyToDate to set
	 */
	public void setHistoryToDate(Date historyToDate) {
		this.historyToDate = historyToDate;
	}

	/**
	 * @return the movingAverage
	 */
	public MovingAveragePeriod getMovingAverage() {
		return movingAverage;
	}

	/**
	 * @param movingAverage the movingAverage to set
	 */
	public void setMovingAverage(MovingAveragePeriod movingAverage) {
		this.movingAverage = movingAverage;
	}

	
	/**
	 * @return the stockPriceHistoryMap
	 */
	public HashMap<String, StockPriceHistoryBean> getStockPriceHistoryMap() {
		return stockPriceHistoryMap;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getStockIds() {
		String method = "getStockIds";
		System.out.println("Starting " + method);
		
		List<String> stockIds = new ArrayList<String>();
		
		if (stockPriceHistoryMap != null && stockPriceHistoryMap.size() > 0) {
			Iterator<Entry<String, StockPriceHistoryBean>> it = stockPriceHistoryMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, StockPriceHistoryBean> pairs = it.next();

				stockIds.add(pairs.getKey());
			}
		}
		
		System.out.println("Returning stockIds: " + stockIds);
		System.out.println("Exiting " + method);
		
		return stockIds;
	}
	
	/**
	 * 
	 * @param stockId
	 * @return
	 */
	public StockPriceHistoryBean getStockPriceHistory(String stockId) {
		String method = "getStockPriceHistory";
		System.out.println("Starting " + method + " with param: " + stockId);
		
		StockPriceHistoryBean stockPriceHistory = null;
		
		if (stockPriceHistoryMap.containsKey(stockId)) {
			stockPriceHistory = stockPriceHistoryMap.get(stockId);
		}
		
		System.out.println("Returning: " + stockPriceHistory);
		System.out.println("Exiting " + method);
		
		return stockPriceHistory;
	}
	
	
	@Override
	public String toString() {
		StringBuffer out = new StringBuffer();
		
		if (stockPriceHistoryMap == null || stockPriceHistoryMap.size() < 1) {
			out.append("No Stocks Identified for Price History");
			out.append("\n");
		} else {
			out.append("Stocks Identified for Price History: ");
			out.append("\n");
			
			Iterator<Entry<String, StockPriceHistoryBean>> it = stockPriceHistoryMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, StockPriceHistoryBean> pairs = it.next();

				out.append("\t");
				out.append(pairs.getKey() + " : " + pairs.getValue());
				out.append("\n");
			}
		}

		out.append("historyFromDate: " + historyFromDate);
		out.append("\n");
		out.append("historyToDate: " + historyToDate);
		out.append("\n");
		
		if (movingAverage != null) {
			out.append("movingAverage: " + movingAverage.toString());
		} else {
			out.append("movingAverage has not been initialized");
		}
		out.append("\n");

		return out.toString();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getAsJsonObject() {
		JSONObject stockPriceHistory = new JSONObject();

		stockPriceHistory.put("Start Date", this.historyFromDate);
		stockPriceHistory.put("End Date", this.historyToDate);
		
		Set<String> keys = stockPriceHistoryMap.keySet();
		if (keys != null && keys.size() > 0) {
			// we have stocks prices
			JSONArray stocks = new JSONArray();
			
			for (String key : keys) {
				JSONObject stock = new JSONObject();
				
				stock.put("Ticker ID", key);
				
				StockPriceHistoryBean values = stockPriceHistoryMap.get(key);
				
				if (values != null) {
					List<PriceBean> averarges = values.getMovingAverage();
					if (averarges != null && averarges.size() > 0) {
						JSONArray averagesJson = new JSONArray();

						for (PriceBean average : averarges) {
							Date date = average.getPriceDate();
							String price = average.getPrice();

							JSONObject priceJason = new JSONObject();
							priceJason.put("Date", date);
							priceJason.put("Price", price);

							averagesJson.add(priceJason);
						}

						stock.put("History", averagesJson);
					}

					stocks.add(stock);
				}
			}
			
			
			// add stock object to stockPriceHistory
			stockPriceHistory.put("Stocks", stocks);
		}
		
		return stockPriceHistory;
	}

}
