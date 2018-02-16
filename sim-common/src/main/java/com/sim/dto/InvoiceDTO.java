package com.sim.dto;

import java.sql.Timestamp;
import java.util.List;

public class InvoiceDTO {

	private int id;

	private Timestamp createdTs;

	private double totalAmount;

	private double discount;

	private String paymentMode;

	private StaffDTO staff;

	private CustomerDTO customer;

	private List<ItemInvoiceDTO> itemInvoices;

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

	public StaffDTO getStaff() {
		return staff;
	}

	public void setStaff(StaffDTO staff) {
		this.staff = staff;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public List<ItemInvoiceDTO> getItemInvoices() {
		return itemInvoices;
	}

	public void setItemInvoices(List<ItemInvoiceDTO> itemInvoices) {
		this.itemInvoices = itemInvoices;
	}
	
	
}
