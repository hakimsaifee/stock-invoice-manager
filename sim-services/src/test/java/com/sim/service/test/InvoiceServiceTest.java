package com.sim.service.test;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sim.dto.InvoiceDTO;
import com.sim.service.InvoiceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context-domain.xml" })
public class InvoiceServiceTest {

	
	@Autowired
	private InvoiceService invoiceService;
	
	
	@Test
	public void getItem() {
	
		Timestamp startDate = new Timestamp(System.currentTimeMillis());
		Timestamp endDate = new Timestamp(System.currentTimeMillis() + 40000000);
		List<InvoiceDTO> invoices = invoiceService.getInvoicesByDate(startDate, endDate);
		System.out.println(invoices);
	}
}
