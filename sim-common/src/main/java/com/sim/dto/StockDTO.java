package com.sim.dto;

import java.sql.Timestamp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class StockDTO {

	private long id;

	private double quantity;

	private Timestamp updatedTs;

	private ItemDTO item;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Timestamp getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Timestamp updatedTs) {
		this.updatedTs = updatedTs;
	}

	public ItemDTO getItem() {
		return item;
	}

	public void setItem(ItemDTO item) {
		this.item = item;
	}

	public StringProperty  getQuantityName() {
		return new SimpleStringProperty(String.valueOf(this.getQuantity()));
	}
	
	
}
