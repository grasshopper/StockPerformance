package com.coolisland.trackmystocks.database;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coolisland.trackmystocks.utils.StringUtils;

public class StockQuoteHistoryBO {
	private static final Logger logger = LoggerFactory
			.getLogger(StockQuoteHistoryBO.class);

	BigDecimal changeAmount = null;
	BigDecimal changePercent = null;
	BigDecimal dailyVolume = null;
	BigDecimal dayHighAmount = null;
	BigDecimal dayLowAmount = null;
	BigDecimal earningsPerShare = null;
	String fiftyTwoWeekRange = null;
	BigDecimal id = null;
	BigDecimal lastTradeAmount = null;
	Date lastTradeDateTime = null;
	BigDecimal openAmount = null;
	BigDecimal previousClose = null;
	BigDecimal pricePerEarnings = null;
	Date quoteDate = null;
	Long tickerId = null;
	BigDecimal volume = null;

	public StockQuoteHistoryBO() {
	}

	public BigDecimal getChangeAmount() {
		return changeAmount;
	}

	public String getChangeAmountAsString() {
		if (changeAmount == null) {
			return null;
		} else {
			return changeAmount.toString();
		}
	}

	public BigDecimal getChangePercent() {
		return changePercent;
	}

	public String getChangePercentAsString() {
		if (changePercent != null) {
			return changePercent.toString();
		} else {
			return null;
		}
	}

	public BigDecimal getDailyVolume() {
		return dailyVolume;
	}

	public BigDecimal getDayHighAmount() {
		return dayHighAmount;
	}

	public String getDayHighAmountAsString() {
		if (dayHighAmount == null) {
			return null;
		} else {
			return dayHighAmount.toString();
		}
	}

	public BigDecimal getDayLowAmount() {
		return dayLowAmount;
	}

	public String getDayLowAmountAsString() {
		if (dayLowAmount == null) {
			return null;
		} else {
			return dayLowAmount.toString();
		}
	}

	public BigDecimal getEarningsPerShare() {
		return earningsPerShare;
	}

	public String getEarningsPerShareAsString() {
		if (earningsPerShare == null) {
			return null;
		} else {
			return earningsPerShare.toString();
		}
	}

	public String getFiftyTwoWeekRange() {
		return fiftyTwoWeekRange;
	}

	public BigDecimal getId() {
		return id;
	}

	public BigDecimal getLastTradeAmount() {
		return lastTradeAmount;
	}

	public String getLastTradeAmountAsString() {
		if (lastTradeAmount == null) {
			return null;
		} else {
			float floatValue = lastTradeAmount.floatValue();
			floatValue = Math.round(floatValue * 100);
			floatValue /= 100;

			String pattern = "###.##";
			DecimalFormat df = new DecimalFormat(pattern );
			df.setMaximumFractionDigits(2);
			return (df.format(floatValue));
		}
	}

	public Date getLastTradeDateTime() {
		return lastTradeDateTime;
	}

	public java.sql.Timestamp getLastTradeSqlDateTime() {
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(
				lastTradeDateTime.getTime());
		return sqlDate;
	}

	public BigDecimal getOpenAmount() {
		return openAmount;
	}

	public String getOpenAmountAsString() {
		if (openAmount == null) {
			return null;
		} else {
			return openAmount.toString();
		}
	}

	public BigDecimal getPreviousClose() {
		return previousClose;
	}

	public String getPreviousCloseAsString() {
		if (previousClose == null) {
			return null;
		} else {
			return previousClose.toString();
		}
	}

	public BigDecimal getPricePerEarnings() {
		return pricePerEarnings;
	}

	public String getPricePerEarningsAsString() {
		if (pricePerEarnings != null) {
			return pricePerEarnings.toString();
		} else {
			return null;
		}
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public java.sql.Timestamp getQuoteSqlDate() {
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(quoteDate.getTime());

		return sqlDate;
	}

	public Long getTickerId() {
		return tickerId;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public String getVolumeAsString() {
		if (volume == null) {
			return null;
		} else {
			return volume.toString();
		}
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	public void setChangePercent(BigDecimal changePercent) {
		logger.trace("ticker: " + this.getTickerId() + ", new change percent: "
				+ changePercent);

		this.changePercent = changePercent;
	}

	public void setChangePercent(String changePercent) {
		logger.debug("ticker: " + this.getTickerId() + "New Change Percent: "
				+ changePercent);

		if (changePercent != null) {
			String number = "";

			if (changePercent.indexOf("%") > 0) {
				number = changePercent.substring(0, changePercent.indexOf("%"));
			}

			this.changePercent = new BigDecimal(number.trim());
		}
	}

	public void setDailyVolume(BigDecimal dailyVolume) {
		this.dailyVolume = dailyVolume;
	}

	public void setDayHighAmount(BigDecimal dayHighAmount) {
		this.dayHighAmount = dayHighAmount;
	}

	public void setDayHighAmount(String dayHigh) {
		java.math.BigDecimal temp = new java.math.BigDecimal(dayHigh);
		this.dayHighAmount = temp;
	}

	public void setDayLowAmount(BigDecimal dayLowAmount) {
		this.dayLowAmount = dayLowAmount;
	}

	public void setDayLowAmount(String dayLow) {
		java.math.BigDecimal temp = new java.math.BigDecimal(dayLow);
		this.dayLowAmount = temp;
	}

	public void setEarningsPerShare(BigDecimal earningsPerShare) {
		this.earningsPerShare = earningsPerShare;
	}

	public void setFiftyTwoWeekRange(String fiftyTwoWeekRange) {
		this.fiftyTwoWeekRange = fiftyTwoWeekRange;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public void setLastTradeAmount(BigDecimal lastTradeAmount) {

		this.lastTradeAmount = lastTradeAmount;
	}

	public void setLastTradeAmount(String lastTradeAmount) {
		String tempAmount = lastTradeAmount.trim();
		
		java.math.BigDecimal temp = new java.math.BigDecimal(tempAmount);
		this.lastTradeAmount = temp;
	}

	public void setLastTradeDateTime(Calendar calDateTime) {
		this.lastTradeDateTime = calDateTime.getTime();
	}

	public void setLastTradeDateTime(String dateTime, SimpleDateFormat format)
			throws Exception {
		Date date = null;
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(format.parse(dateTime));

			date = new Date(cal.getTimeInMillis());
		} catch (Exception e) {
			String message = "Unable to parse date/time for ticker: "
					+ this.getTickerId() + ", date/time: " + dateTime
					+ " using format: " + format.toString();
			logger.error(message);

			throw new Exception(message);
		}

		this.lastTradeDateTime = date;
	}

	public void setLastTradeDateTime(Date lastTradeDateTime) {
		this.lastTradeDateTime = lastTradeDateTime;
	}

	public void setOpenAmount(BigDecimal openAmount) {
		this.openAmount = openAmount;
	}

	public void setOpenAmount(String openAmount) {
		java.math.BigDecimal temp = new java.math.BigDecimal(openAmount);
		this.openAmount = temp;
	}

	public void setPreviousClose(BigDecimal previousClose) {
		this.previousClose = previousClose;
	}

	public void setPricePerEarnings(BigDecimal pricePerEarnings) {
		this.pricePerEarnings = pricePerEarnings;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public void setQuoteDate(String dateTime, SimpleDateFormat format) {
		java.util.Calendar temp = StringUtils.dateString2Calendar(dateTime,
				format);
		Date date = new Date(temp.getTimeInMillis());

		this.quoteDate = date;
	}

	public void setTickerId(Long tickerId) {
		this.tickerId = tickerId;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public void setVolume(String volume) {
		BigDecimal temp = new BigDecimal(volume);

		this.volume = temp;
	}

	public void setVolume(int volume) {
		this.volume = new BigDecimal(volume);
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(125);

		StringUtils.appendHeader(out, "StockQuoteHistoryBo", "");
		StringUtils.appendNameValue(out, "ID", id, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Ticker Symbol Id", "" + tickerId,
				StringUtils.INDENT);

		SimpleDateFormat quoteDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		if (quoteDate != null) {
			StringUtils.appendNameValue(out, "Quote Date",
					quoteDateFormat.format(quoteDate), StringUtils.INDENT);
		} else {
			StringUtils.appendNameValue(out, "Quote Date", "null",
					StringUtils.INDENT);
		}
		out.append(StringUtils.LINE_FEED);
		StringUtils.appendNameValue(out, "Last Trade Amount", lastTradeAmount,
				StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Last Trade Date Time",
				lastTradeDateTime, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Change Amount", changeAmount,
				StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Open Amount", openAmount,
				StringUtils.INDENT);
		out.append(StringUtils.LINE_FEED);
		StringUtils.appendNameValue(out, "Day High Amount", dayHighAmount,
				StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Day Low Amount", dayLowAmount,
				StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Volume", volume, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Previous Close", previousClose,
				StringUtils.INDENT);
		out.append(StringUtils.LINE_FEED);
		StringUtils.appendNameValue(out, "Change Percent", changePercent,
				StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Fifty Two Week Range",
				fiftyTwoWeekRange, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Earnings Per Share",
				earningsPerShare, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Price Per Earnings",
				pricePerEarnings, StringUtils.INDENT);
		StringUtils.appendNameValue(out, "Daily Volume", dailyVolume,
				StringUtils.INDENT);

		return out.toString();
	}
}
