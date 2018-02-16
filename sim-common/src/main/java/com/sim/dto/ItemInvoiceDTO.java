package com.sim.dto;

import java.sql.Timestamp;

public class ItemInvoiceDTO {

	private long id;

	private double purchasedQuantity;

	private Timestamp createdTs;

	private ItemDTO item;

	private double amount;
	
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getPurchasedQuantity() {
		return purchasedQuantity;
	}

	public void setPurchasedQuantity(double purchasedQuantity) {
		this.purchasedQuantity = purchasedQuantity;
	}

	public Timestamp getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Timestamp createdTs) {
		this.createdTs = createdTs;
	}

	public ItemDTO getItem() {
		return item;
	}

	public void setItem(ItemDTO item) {
		this.item = item;
	}

	// private InvoiceDTO invoice;

}
