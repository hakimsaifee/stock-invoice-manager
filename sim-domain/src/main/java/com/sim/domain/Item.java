package com.sim.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

	// TODO: Add sequence.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "barcode", unique = true, nullable = false, length = 100)
	private String barcode;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "cost_price", nullable = false)
	private double costPrice;

	@Column(name = "mrp", nullable = false)
	private double mrp;

	@Column(name = "rrp", nullable = false)
	private double rrp;

	@Column(name = "created_ts", nullable = false)
	private Timestamp createdTs;

	@Column(name = "updated_ts")
	private Timestamp updatedTs;

	
	@Lob
	private byte [] barcodeImage;
	
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Category category;

	// TODO: Check for correct cascade type.
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "item")
	private Stock stock;

	@OneToMany(mappedBy = "item",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ItemInvoice> itemInvoices;

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public double getRrp() {
		return rrp;
	}

	public void setRrp(double rrp) {
		this.rrp = rrp;
	}

	public Timestamp getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Timestamp createdTs) {
		this.createdTs = createdTs;
	}

	public Timestamp getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Timestamp updatedTs) {
		this.updatedTs = updatedTs;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public List<ItemInvoice> getItemInvoices() {
		return itemInvoices;
	}

	public void setItemInvoices(List<ItemInvoice> itemInvoices) {
		this.itemInvoices = itemInvoices;
	}

	public byte[] getBarcodeImage() {
		return barcodeImage;
	}
	
	public void setBarcodeImage(byte[] barcodeImage) {
		this.barcodeImage = barcodeImage;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Item [id=%s, name=%s, barcode=%s, description=%s, costPrice=%s, mrp=%s, rrp=%s, createdTs=%s, updatedTs=%s, category=%s, stock=%s]",
				id, name, barcode, description, costPrice, mrp, rrp, createdTs, updatedTs, category, stock
				);
	}

}
