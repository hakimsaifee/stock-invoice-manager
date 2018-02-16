package com.sim.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.dao.InvoiceDAO;
import com.sim.dao.ItemDAO;
import com.sim.dao.UserDetailDAO;
import com.sim.domain.Invoice;
import com.sim.domain.ItemInvoice;
import com.sim.domain.Staff;
import com.sim.domain.UserDetail;
import com.sim.dto.UserDetailDTO;
import com.sim.service.UserDetailService;

@Service
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	private UserDetailDAO userDetailDAO;

	@Transactional
	public void create(UserDetailDTO userDetailDTO) {
		
		ModelMapper modelMapper = new ModelMapper();
		UserDetail userDetail =  modelMapper.map(userDetailDTO, UserDetail.class);
		userDetailDAO.create(userDetail);
	}

	@Autowired
	private InvoiceDAO invoiceDAO;

	@Autowired
	private ItemDAO itemDAO;
	
	/*@Test
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
		itemInvoice.setItem(itemDAO.findByPK(301l));
//		itemInvoice.setInvoice(invoice);
		itemInvoice.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		itemInvoices.add(itemInvoice);
		
		invoice.setItemInvoices(itemInvoices);
		invoiceDAO.create(invoice);
		
		System.out.println("Invoice created.");
	}*/
}
