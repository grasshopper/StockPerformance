package com.coolisland.trackmystocks;

public enum MenuState {
	HOME(1), ACCOUNT(2), BUY(3), SELL(4), PRICE_UPDATE(5), RESEARCH(6), SANDBOX(7);

	private int state;

	private MenuState(int value) {
		state = value;
	}
	
	public int getState() {
		return state;
	}

}
