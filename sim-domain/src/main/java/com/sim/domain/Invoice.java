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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_ID_GENERATOR")
	@SequenceGenerator(name = "ITEM_ID_GENERATOR", sequenceName = "ITEM_SQ")
	private int id;

	@Column(name = "created_ts", nullable = false)
	private Timestamp createdTs;

	@Column(name = "total_amount", nullable = false)
	private double totalAmount;

	@Column(name = "round_off", nullable = false)
	private double roundOff;

	@Column(name = "discount", nullable = false)
	private double discount;

	@Column(name = "additional_charges", nullable = true)
	private double additionalCharges;

	@Column(name = "payment_mode", length = 100)
	private String paymentMode;
	
	@Column(name = "bill_name", length = 250)
	private String billName;

	@ManyToOne(fetch = FetchType.LAZY)
	private Staff staff;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Customer customer;

	@OneToMany(/* mappedBy = "invoice", */cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ItemInvoice> itemInvoices;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Timestamp createdTs) {
		this.createdTs = createdTs;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<ItemInvoice> getItemInvoices() {
		return itemInvoices;
	}

	public void setItemInvoices(List<ItemInvoice> itemInvoices) {
		this.itemInvoices = itemInvoices;
	}

	public double getAdditionalCharges() {
		return additionalCharges;
	}

	public void setAdditionalCharges(double additionalCharges) {
		this.additionalCharges = additionalCharges;
	}

	public double getRoundOff() {
		return roundOff;
	}
	
	public void setRoundOff(double roundOff) {
		this.roundOff = roundOff;
	}
	
	public String getBillName() {
		return billName;
	}
	
	public void setBillName(String billName) {
		this.billName = billName;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Invoice [id=%s, createdTs=%s, totalAmount=%s, roundOff=%s, discount=%s, additionalCharges=%s, paymentMode=%s, billName=%s, staff=%s, customer=%s, itemInvoices=%s]",
				id, createdTs, totalAmount, roundOff, discount, additionalCharges, paymentMode, billName, staff,
				customer, itemInvoices);
	}

}
