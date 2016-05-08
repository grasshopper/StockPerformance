package com.coolisland.trackmystocks.yahoo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coolisland.trackmystocks.database.StockQuoteHistoryBO;
import com.coolisland.trackmystocks.utils.StringUtils;

import au.com.bytecode.opencsv.CSVReader;
//import com.coolisland.trackmystocks.utils.QuoteData;

public class ParseYahooCsvFileQuotes {
	private static final Logger logger = LoggerFactory.getLogger(StockQuoteHistoryBO.class);

	private static final String YAHOO_DATE_FORMAT = "yyyy-MM-dd";
	private CSVReader reader = null;
	private int row = 0;

	public ParseYahooCsvFileQuotes(String fileName) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(fileName)) {
			logger.error("ERROR: File name is empty");
			return;
		}

		getColumnTitles(fileName);
	}

	private void getColumnTitles(String fileName) {
		try {
			reader = new CSVReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			logger.error("ERROR: unable to open file: " + fileName);
			e.printStackTrace();
		}

		// columnTitles = reader.readNext();
		row++;

		// System.out.println("Column Titles: ");
		//
		// for (int ndx = 0; ndx < columnTitles.length; ndx++) {
		// System.out.println("\t" + columnTitles[ndx]);
		// }
	}

	public void closeReader() {
		// close the reader
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int parseHistoricalFile(String fileName) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(fileName)) {
			logger.error("ERROR: File name is empty");
			return row;
		}

		String[] nextLine;
		try {
			while ((nextLine = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				row++;

				StringBuffer out = new StringBuffer();
				out.append(row + ": ");
				for (int ndx = 0; ndx < nextLine.length; ndx++) {
					out.append(" " + ndx + ": " + nextLine[ndx]);
				}

				// System.out.println(out.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;
	}

	// public void mapCsvTo(QuoteData quoteDataBean) {
	// if (quoteDataBean == null) {
	// return;
	// }
	//
	// ColumnPositionMappingStrategy strat = new
	// ColumnPositionMappingStrategy();
	//
	// strat.setType(quoteDataBean.getClass());
	//
	// // the fields to bind to in your JavaBean
	// String[] columns = quoteDataBean.getColumnNamesForYahoo();
	//
	// strat.setColumnMapping(columns);
	//
	// CsvToBean csv = new CsvToBean();
	//
	// try {
	// reader = new CSVReader(new FileReader(csvFileName));
	// } catch (FileNotFoundException e) {
	// System.out.println("ERROR: unable to open file: " + csvFileName);
	// e.printStackTrace();
	// }
	//
	// List list = csv.parse(strat, reader);
	//
	// for (int ndx = 0; ndx < list.size(); ndx++) {
	// Object o = list.get(ndx);
	// System.out.println("Class: " + o.getClass());
	// }
	//
	// // close the reader
	// try {
	// reader.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public boolean getNextCsvRow(StockQuoteHistoryBO quoteDataBean) throws Exception {
		if (quoteDataBean == null) {
			quoteDataBean = new StockQuoteHistoryBO();
		}

		if (reader == null) {
			return false;
		}

		String[] nextLine;
		try {
			// nextLine[] is an array of values from the line
			nextLine = reader.readNext();

			// header line with column header
			if (nextLine == null) {
				logger.info("no more data");
			}

			if (nextLine != null && "DATE".compareToIgnoreCase(nextLine[0]) == 0) {
				nextLine = reader.readNext();
			}

			if (nextLine != null) {
				row++;

				quoteDataBean.setLastTradeDateTime(nextLine[0], new SimpleDateFormat(YAHOO_DATE_FORMAT));
				quoteDataBean.setQuoteDate(nextLine[0], new SimpleDateFormat(YAHOO_DATE_FORMAT));
				
				String strAmount = StringUtils.convertToNumber(nextLine[1], 2);
				quoteDataBean.setOpenAmount(strAmount);
				
				strAmount = StringUtils.convertToNumber(nextLine[2], 2);
				quoteDataBean.setDayHighAmount(strAmount);
				
				strAmount = StringUtils.convertToNumber(nextLine[3], 2);
				quoteDataBean.setDayLowAmount(strAmount);
				
				strAmount = StringUtils.convertToNumber(nextLine[4], 2);
				quoteDataBean.setLastTradeAmount(strAmount);
				
				quoteDataBean.setVolume(nextLine[5]);
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public boolean hasData() {
		boolean returnVal = true;

		if (reader == null) {
			returnVal = false;
		}

		return returnVal;
	}
}
