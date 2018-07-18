package com.invoice.generation.report;

public class ItemModel {

	private Temp temp1;
	
	private String name;

	private String quantity;

	private double rate;
	private double amount;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Temp getTemp1() {
		return temp1;
	}
	public void setTemp1(Temp temp1) {
		this.temp1 = temp1;
	}
}
