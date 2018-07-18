package com.sim.dto;

public class BillModel extends InvoiceDTO {

	private String shopName;

	private String shopAddress;

	private String contactNumber;

	private String gstNumber;

	private double totalSavings;

	private double totalItemSold;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public double getTotalSavings() {
		return totalSavings;
	}

	public void setTotalSavings(double totalSavings) {
		this.totalSavings = totalSavings;
	}

	public double getTotalItemSold() {
		return totalItemSold;
	}

	public void setTotalItemSold(double totalItemSold) {
		this.totalItemSold = totalItemSold;
	}

	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
}
