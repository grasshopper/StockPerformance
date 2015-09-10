package com.coolisland.trackmystocks.sellbuy;

public enum ExchangeType {
	AMEX("AMEX");

	private String type;

	private ExchangeType(String s) {
		type = s;
	}

	public String getExchangeType() {
		return type;
	}

	public static ExchangeType getDefault() {
		return ExchangeType.AMEX;
	}
	
	/**
	 * @return return the next enum if there is a next, otherwise return null
	 */
	public ExchangeType getNext() {
		return this.ordinal() < ExchangeType.values().length - 1 ? ExchangeType.values()[this
				.ordinal() + 1] : null;
	}
}
