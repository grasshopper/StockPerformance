package com.coolisland.trackmystocks.beans;

import java.util.Date;


/**
 * formats the price appropriately
 * 
 * @author Silvio
 *
 */
public class PriceBean {
	private String price;
	private Date priceDate = null;

	public PriceBean() {
		price = "";
	}

	public PriceBean(String price, Date priceDate) {
		this.price = price;
		this.priceDate = priceDate;
	}
	
	public String getPrice() {
		return price;
	}


	/**
	 * @return the priceDate
	 */
	public Date getPriceDate() {
		return priceDate;
	}

	/**
	 * @param priceDate the priceDate to set
	 */
	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}
	
	
	@Override
	public String toString() {
		return "PriceBean [price=" + price + ", date=" + priceDate + "]";
	}
}
