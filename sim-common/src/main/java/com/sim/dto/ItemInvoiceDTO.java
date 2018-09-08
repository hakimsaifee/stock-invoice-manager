package com.sim.dto;

import java.sql.Timestamp;

import com.sim.common.constant.OfferTypeEnum;

public class ItemInvoiceDTO {

	private long id;

	//Sold quantity.
	private double purchasedQuantity;
	
	private double productSellingPrice;

	private Timestamp createdTs;

	private ItemDTO item;

	private double amount;
	
	private long serialNumber;
	
	private OfferTypeEnum offerType;
	
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

	 public double getProductSellingPrice() {
		return productSellingPrice;
	}
	 
	 public void setProductSellingPrice(double productSellingPrice) {
		this.productSellingPrice = productSellingPrice;
	}
	 
	 public OfferTypeEnum getOfferType() {
		return offerType;
	}
	 
	 public void setOfferType(OfferTypeEnum offerType) {
		this.offerType = offerType;
	}
	 
	 public long getSerialNumber() {
		return serialNumber;
	}
	 
	 public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}
	// private InvoiceDTO invoice;

}
