package com.coolisland.trackmystocks.sellbuy;


public enum EnumTest {
	PENNY(1, "ONE"), NICKEL(5, "FIVE");
	private int worth;
	private String value;
	
	private EnumTest(int worth, String value) {
		this.worth = worth;
		this.value = value;
	}
	
	public static EnumTest getFirst() {
		
		EnumTest[] enumSet = EnumTest.values();
		for (EnumTest e : enumSet) {
			System.out.println(e);
		}
		
		return enumSet[0];
	}
	
	public EnumTest getNext() {
		int thisOrd = this.ordinal();
		EnumTest[] enumSet = EnumTest.values();
		
		thisOrd++;
		if (enumSet.length > thisOrd) {
			return enumSet[thisOrd];
		} else {
			return null;
		}
		
	}
	
	
	public static void main(String[] args) {
		System.out.println("Penny: " + EnumTest.PENNY);
		System.out.println("Nickel: " + EnumTest.NICKEL);
		
		System.out.println("First: " + EnumTest.getFirst());
		System.out.println("Next: " + EnumTest.getFirst().getNext());
		System.out.println("Next: " + EnumTest.getFirst().getNext().getNext());
		
	}
	
}
