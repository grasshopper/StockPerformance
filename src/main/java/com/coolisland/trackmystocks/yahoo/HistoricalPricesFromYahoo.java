package com.coolisland.trackmystocks.yahoo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;


public class HistoricalPricesFromYahoo {
	public enum YahooOptions {
		LAST_TRADE_DATE("d1"), DATE_DATE("d2"), ERROR_INDICATION("e1"), TRADE_LINKS("f0"), MORE_INFO("i"), LAST_TRADE_TIME(
				"l"), LAST_TRADE_AMOUNT("l1"), TWO_HUNDRED_DAY_MA("m4"), CHANGE_FROM_TWO_HUNDRED_DAY_MA("m5"), FIFY_DAY_MA(
				"m3"), CHANGE_FROM_FIFTY_DAY_MA("m7"), STOCK_NAME("n"), NOTES("n4"), TICKER_TREND("t7"), VOLUME("v");

		private String optionCode;

		private YahooOptions(String code) {
			optionCode = code;
		}

		public String getOptionCode() {
			return optionCode;
		}
	}


	private static final Logger logger = LoggerFactory.getLogger(HistoricalPricesFromYahoo.class);

	final private static String downloadBaseUrl = "http://download.finance.yahoo.com/d/";
	final private static String realChartBaseUrl = "http://real-chart.finance.yahoo.com/table.csv";
//	private static String optionsParam = "&f=d1d2e1f0ill1m4m5m3m7nn4t7v";
	final private static String latestPriceOptionsParam = "&f=d1d2e1f0ill1m4m5m3m7nn4t7v";
	final private static String ignoreFileTypeParam = "&e=.csv";
	final private static String stockParamName = "s";
	final private static String fromMonthParamName = "a";
	final private static String fromDayParamName = "b";
	final private static String fromYearParamName = "c";
	final private static String toMonthParamName = "d";
	final private static String toDayParamName = "e";
	final private static String toYearParamName = "f";
	final private static String dailyParam = "g=d";
	final private static String fileTypeParam = "ignore=.csv";


	public List<String[]> getLatestStockPrice(String ticker) throws IOException {
		URL stockUrl = null;
		String urlStr = downloadBaseUrl;
		List<String[]> stockPrices = new ArrayList<String[]>();

		// check params to see if we can continue
		if (StringUtils.isEmpty(ticker)) {
			logger.warn("ticker cannot be null");

			return stockPrices;
		}


		// set the file name
		urlStr += "quotes.csv";

		// add stock ticker
		urlStr += "?s=" + ticker;

		// add options
		urlStr += latestPriceOptionsParam;

		// add download file type
		urlStr += ignoreFileTypeParam;

		logger.debug("URL string: " + urlStr);
		try {
			stockUrl = new URL(urlStr);
		} catch (MalformedURLException e) {
			logger.error("Error creating URL. URL: " + urlStr);
			logger.error(e.getMessage());

			e.printStackTrace();
			return stockPrices;
		}


		// convert the returned stream into our object
		InputStream is = null;
		URLConnection connection = null;

		try {
			connection = stockUrl.openConnection();
			if (connection == null) {
				logger.debug("Unable to open connection to " + urlStr);

				return stockPrices;
			}
		} catch (IOException e) {
			logger.error("Exception caught trying to connect to: ");
			logger.error("\t" + urlStr);

			e.printStackTrace();

			return stockPrices;
		}


		try {
			is = connection.getInputStream();
		} catch (IOException fnfe) {
			logger.error("Error retrieving historical prices");
			fnfe.printStackTrace();

			return stockPrices;
		}

		try {
			if (is.available() <= 0) {
				logger.warn("No data found for " + ticker);
			}
		} catch (IOException e1) {
			logger.error("Error retrieving historical prices");
			e1.printStackTrace();

			is.close();
			return stockPrices;
		}

		CSVReader reader = new CSVReader(new InputStreamReader(is), ',', '"', 0);
		try {
			stockPrices = reader.readAll();
		} catch (IOException e) {
			logger.error("Error retrieving historical prices");
			e.printStackTrace();

			return stockPrices;
		} finally {
			is.close();
		}

		if (stockPrices != null) {
			YahooOptions[] values = YahooOptions.values();
			for (String[] price : stockPrices) {
				int index = 0;
				for (String info : price) {
					logger.trace(values[index] + ": " + info);

					index++;
				}
			}
		}

		// log values we just read
		if (stockPrices != null) {
			int size = stockPrices.size();
			for (int index = 0; index < size; index++) {
				String[] stockStrArray = stockPrices.get(index);

				StringBuffer msg = new StringBuffer();
				for (String stockStr : stockStrArray) {
					msg.append(stockStr + "\t");
				}
				logger.debug(msg.toString());
			}
		}

		if (!containsPrices(stockPrices)) {
			stockPrices.clear();
		}
		
		return stockPrices;
	}

	
	private final String notAvailable = "N/A";
	private boolean containsPrices(List<String[]> stockPrices) {
		boolean containsData = false;

		// if we get back one row with N/A's then stock was not found
		if (stockPrices != null) {
			int rows = stockPrices.size();
			if (rows == 1) {
					String[] stockStrArray = stockPrices.get(0);
					for (String stockStr : stockStrArray) {
						if (!notAvailable.equalsIgnoreCase(stockStr)){
							containsData = true;
							break;
						}
					}
			}
		}

		return containsData;
	}

	/**
	 * 
	 * @param stockName
	 * @param fromMonth
	 * @param fromDay
	 * @param fromYear
	 * @param toMonth
	 * @param toDay
	 * @param toYear
	 * @param filePath
	 */
	public void saveHistoricalPricesToFile(String stockName, String fromMonth, String fromDay, String fromYear,
			String toMonth, String toDay, String toYear, String filePath) {

		String fullUrl = realChartBaseUrl + "?";
		fullUrl += stockParamName + "=" + stockName;
		fullUrl += "&" + fromMonthParamName + "=" + fromMonth;
		fullUrl += "&" + fromDayParamName + "=" + fromDay;
		fullUrl += "&" + fromYearParamName + "=" + fromYear;

		fullUrl += "&" + toMonthParamName + "=" + toMonth;
		fullUrl += "&" + toDayParamName + "=" + toDay;
		fullUrl += "&" + toYearParamName + "=" + toYear;

		fullUrl += "&" + dailyParam;

		fullUrl += "&" + fileTypeParam;

		logger.debug("URL: " + fullUrl);

		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath);

			// save the historical prices to file
			saveToFile(fullUrl, writer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param urlString
	 * @param writer
	 */
	private void saveToFile(String urlString, FileWriter writer) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (url == null) {
			logger.error("Bad URL: " + urlString);
			return;
		}

		try {
			InputStream is = null;
			URLConnection connection = null;
			try {
				connection = url.openConnection();
				if (connection == null) {
					logger.debug("Unable to open connection to " + urlString);
					return;
				}

				try {
					// connection.getContentType();
					//
					// connection.getAllowUserInteraction();
					// connection.getContent();
					// connection.getContentLength();
					// connection.getContentEncoding();
					// connection.connect();
					// connection.getHeaderFields();
					// connection.getInputStream();
					// connection.getPermission();
					// connection.toString();


					is = connection.getInputStream();
				} catch (FileNotFoundException fnfe) {
					logger.error("Error retrieving historical prices");
				}

				if (is == null) {
					logger.error("\t" + "Unable to get input string from " + urlString);
					return;
				}

			} catch (IOException e) {
				logger.error("Exception caught trying to connect to: ");

				if (urlString != null) {
					logger.error("\t" + urlString);
				} else {
					logger.error("\t" + "URL is null");
				}

				if (connection == null) {
					logger.error("\t" + "Connection is null");
				} else {
					logger.error("\t" + connection);
				}

				e.printStackTrace();
			}

			InputStreamReader inputStreamReader = new InputStreamReader(is);
			BufferedReader reader = null;
			if (inputStreamReader != null) {
				reader = new BufferedReader(inputStreamReader);
			}

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					// logger.debug(line);
					writer.write(line);
					writer.write("\n");
				}

				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				reader.close();
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HistoricalPricesFromYahoo prices = new HistoricalPricesFromYahoo();

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		String filePath = "C:\\temp\\YahooPrices\\VPL-" + year + month + day + ".csv";

		String toMonth = String.valueOf(month);
		String toDay = String.valueOf(day);
		String toYear = String.valueOf(year);

		prices.saveHistoricalPricesToFile("VPL", "00", "01", "2011", toMonth, toDay, toYear, filePath);
	}
}


