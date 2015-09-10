package com.coolisland.trackmystocks.database;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.coolisland.trackmystocks.utils.StringUtils;

public class StockTickerTypeBO {
	private BigDecimal id;
	private String type;
	private String description;

	public StockTickerTypeBO() {

	}

	public StockTickerTypeBO(ResultSet rs) throws SQLException {
		load(rs);
	}

	public StockTickerTypeBO(StockTickerTypeBO bo) {
		load(bo);
	}

	
	public int getId() {
		return id.intValue();
	}

	public String getType() {
		return type;
	}
	

	public String getDescription() {
		return description;
	}


	public StockTickerTypeBO load(ResultSet rs) throws SQLException {
		try {
			id = rs.getBigDecimal("ID");
			type = rs.getString("TYPE");
			description = rs.getString("DESCRIPTION");
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
	public StockTickerTypeBO load(StockTickerTypeBO bo) {
		id = new BigDecimal(bo.getId());
		type = bo.getType();
		description = bo.getDescription();
		
		return this;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuffer out = new StringBuffer(200);
		String myIndent = StringUtils.INDENT;

		StringUtils.appendHeader(out, "Ticker", "");

		StringUtils.appendNameValueLine(out, "ID: ", id, myIndent);
		StringUtils.appendNameValueLine(out, "Name: ", type, myIndent);
		StringUtils.appendNameValueLine(out, "Exchange: ", description, myIndent);

		return out.toString();
	}

}
