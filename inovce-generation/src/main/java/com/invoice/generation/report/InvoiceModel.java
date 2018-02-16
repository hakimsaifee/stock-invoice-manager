package com.invoice.generation.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InvoiceModel {

	private String storeTitle;
	private String storeAddress;
	private String invoiceNumber;
	private Timestamp billDate;
	List<ItemModel> items = new ArrayList<ItemModel>();
	public String getStoreTitle() {
		return storeTitle;
	}
	public void setStoreTitle(String storeTitle) {
		this.storeTitle = storeTitle;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Timestamp getBillDate() {
		return billDate;
	}
	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}
	public List<ItemModel> getItems() {
		return items;
	}
	public void setItems(List<ItemModel> items) {
		this.items = items;
	}

	
}
