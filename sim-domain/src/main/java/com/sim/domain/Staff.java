package com.sim.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "contact_number", nullable = false, length = 20)
	private String contactNumber;

	@Column(name = "address", nullable = false, length = 200)
	private String address;

	@Column(name = "adhar_number", nullable = false, length = 20)
	private String adharNumber;

	@OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
	private List<Invoice> invoices;

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

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	@Override
	public String toString() {
		return String.format("Staff [id=%s, name=%s, contactNumber=%s, address=%s, adharNumber=%s, invoices=%s]", id,
				name, contactNumber, address, adharNumber, invoices);
	}

}
