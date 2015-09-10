package com.coolisland.trackmystocks.sellbuy;



public enum StockType {
	ETF(1, "ETF"), STOCK(2, "Stocks"), INDEX(3, "Index");
	private int dbId;
	private String displayName;
	
	private StockType(int dbId, String displayName) {
		setStockTypeId(dbId);
		setType(displayName);
	}
	

	/**
	 * 
	 * @return returns the id used in the DB which is associated with this Stock
	 *         Type
	 */
	public int getStockTypeId() {
		return dbId;
	}

	
	public void setStockTypeId(int typeId) {
		this.dbId = typeId;
	}


	/**
	 * 
	 * @return stock type
	 */
	public String getStockType() {
		return displayName;
	}

	private void setType(String type) {
		this.displayName = type;
	}
	
	
	public static StockType getFirst() {
		StockType[] enumSet = StockType.values();
//		for (StockType e : enumSet) {
//			System.out.println(e);
//		}
		
		return enumSet[0];
	}
	
	public StockType getNext() {
		int thisOrd = this.ordinal();
		StockType[] enumSet = StockType.values();
		
		thisOrd++;
		if (enumSet.length > thisOrd) {
			return enumSet[thisOrd];
		} else {
			return null;
		}
		
	}


	public static StockType getDefault() {
		return StockType.STOCK;
	}
	
	
	public static StockType valueFor(String stockType) {
		StockType[] enumSet = StockType.values();
		for (StockType e : enumSet) {
			if (e.displayName.equalsIgnoreCase(stockType)) {
				return e;
			}
		}
		
		return null;
	}
}

