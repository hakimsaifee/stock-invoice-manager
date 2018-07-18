package com.sim.dto;

import java.util.List;

public class StaffDTO {

	private long id;

	private String name;

	private String contactNumber;

	private String address;

	private String adharNumber;

//	private List<InvoiceDTO> invoices;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAdharNumber() {
		return adharNumber;
	}

	public void setAdharNumber(String adharNumber) {
		this.adharNumber = adharNumber;
	}

//	public List<InvoiceDTO> getInvoices() {
//		return invoices;
//	}
//
//	public void setInvoices(List<InvoiceDTO> invoices) {
//		this.invoices = invoices;
//	}

}
