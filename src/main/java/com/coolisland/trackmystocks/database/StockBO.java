package com.coolisland.trackmystocks.database;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.coolisland.trackmystocks.sellbuy.ExchangeType;
import com.coolisland.trackmystocks.sellbuy.StockType;
import com.coolisland.trackmystocks.utils.StringUtils;


public class StockBO {
	private Long accountId;
	private String exchange;
	private String graphUrl;
	private Long id;
	private String itemLabel;
	private String itemValue;
	private String name;
	private Boolean own;
	private String symbol;
	private BigDecimal tickerTypeId;
	private Boolean track;

	public StockBO() {

	}

	public StockBO(ResultSet rs) throws SQLException {
		load(rs);
	}

	public StockBO(StockBO bo) {
		load(bo);
	}

	
	public Long getAccountId() {
		return accountId;
	}

	public String getExchange() {
		return exchange;
	}
	
	public String getGraphUrl() {
		return graphUrl;
	}

	public Long getId() {
		return id;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public String getItemValue() {
		return itemValue;
	}

	public String getName() {
		return name;
	}

	public Boolean getOwn() {
		return own;
	}

	public String getSymbol() {
		return symbol;
	}

	public BigDecimal getTickerTypeId() {
		return tickerTypeId;
	}

	public Boolean getTrack() {
		return track;
	}

	public StockBO load(ResultSet rs) throws SQLException {
		try {
			id = rs.getLong("ID");
			symbol = rs.getString("SYMBOL");
			name = rs.getString("NAME");
			exchange = rs.getString("EXCHANGE");
			track = rs.getBoolean("TRACK");
			own = rs.getBoolean("OWN");
			accountId = rs.getLong("ACCOUNT_ID");
			setTickerTypeId(rs.getBigDecimal("TICKER_TYPE_ID"));
		} catch (SQLException e) {
			System.out.println(rs.toString());
			e.printStackTrace();
			throw new SQLException(e.getMessage(), e.getCause());
		}
		
		return this;
	}

	/**
	 * Clones the object passed in into this one
	 * 
	 * @param bo
	 * @return
	 */
	public StockBO load(StockBO bo) {
		id = bo.getId();
		symbol = bo.getSymbol();
		name = bo.getName();
		exchange = bo.getExchange();
		track = bo.getTrack();
		own = bo.getOwn();
		accountId = bo.getAccountId();
		tickerTypeId = bo.getTickerTypeId();
		itemValue = bo.getItemValue();
		itemLabel = bo.getItemLabel();
		
		return this;
	}

	public void setAccountId(Long id) {
		this.accountId = id;
	}

	public void setExchange(ExchangeType exchange) {
		this.exchange = exchange.getExchangeType();
	}

	/**
	 * @param graphUrl the graphUrl to set
	 */
	public void setGraphUrl(String graphUrl) {
		this.graphUrl = graphUrl;
	}

	public void setId(Long id2) {
		this.id = id2;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwn(Boolean own) {
		this.own = own;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	private void setTickerTypeId(BigDecimal tickerTypeId) {
		this.tickerTypeId = tickerTypeId;
	}

	public void setTickerTypeId(StockType tickerTypeId) {
		this.tickerTypeId = new BigDecimal(tickerTypeId.getStockTypeId());
	}

	public void setTrack(Boolean track) {
		this.track = track;
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(200);
		String myIndent = StringUtils.INDENT;

		StringUtils.appendHeader(out, "Ticker", "");

		StringUtils.appendNameValueLine(out, "ID: ", "" + id, myIndent);
		StringUtils.appendNameValueLine(out, "Symbol: ", symbol, myIndent);
		StringUtils.appendNameValueLine(out, "Name: ", name, myIndent);
		StringUtils.appendNameValueLine(out, "Exchange: ", exchange, myIndent);
		StringUtils.appendNameValueLine(out, "Track: ", track, myIndent);
		StringUtils.appendNameValueLine(out, "Own: ", own, myIndent);
		StringUtils.appendNameValueLine(out, "Account: ", "" + accountId, myIndent);

		return out.toString();
	}
	
}
