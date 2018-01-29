package com.sim.domain.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sim.dao.InvoiceDAO;
import com.sim.dao.ItemDAO;
import com.sim.domain.Invoice;
import com.sim.domain.ItemInvoice;
import com.sim.domain.Staff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context-domain.xml" })
public class InvoiceTest {

	
	@Autowired
	private InvoiceDAO invoiceDAO;

	@Autowired
	private ItemDAO itemDAO;
	
	@Test
	@Transactional
	public void createUserTest()
	{
		Invoice invoice = new Invoice();
		invoice.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		invoice.setTotalAmount(550.23);
		invoice.setDiscount(55.00);
		invoice.setPaymentMode("Cash");
		//set staff
		Staff staff = new Staff();
		staff.setContactNumber("9090213939");
		staff.setName("Ramesh");
		staff.setAddress("ujjain");
		staff.setAdharNumber("990393");
		
		invoice.setStaff(staff);
		
		//set items
		List<ItemInvoice> itemInvoices = new ArrayList<ItemInvoice>();
		ItemInvoice itemInvoice = new ItemInvoice();
		itemInvoice.setPurchasedQuantity(3);
		itemInvoice.setItem(itemDAO.findByPK(201));
//		itemInvoice.setInvoice(invoice);
		itemInvoice.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		itemInvoices.add(itemInvoice);
		
		invoice.setItemInvoices(itemInvoices);
		invoiceDAO.create(invoice);
		
		System.out.println("Invoice created.");
	}
}
