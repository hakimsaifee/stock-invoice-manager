package com.sim.domain;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item_invoice")
public class ItemInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "purchased_quantity", nullable = false)
	private double purchasedQuantity;

	@Column(name = "created_ts", nullable = false)
	private Timestamp createdTs;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Item item;

//	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//	private Invoice invoice;

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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

//	public Invoice getInvoice() {
//		return invoice;
//	}
//
//	public void setInvoice(Invoice invoice) {
//		this.invoice = invoice;
//	}

	@Override
	public String toString() {
		return String.format("ItemInvoice [id=%s, purchangedQuantity=%s, createdTs=%s]", id, purchasedQuantity,
				createdTs);
	}

}
