package com.sim.service;

import java.sql.Timestamp;
import java.util.List;

import com.sim.dto.InvoiceDTO;

public interface InvoiceService {

	InvoiceDTO create(InvoiceDTO itemDTO);

//	void remove(Integer id);
//	
//	List<InvoiceDTO> getAll();
	
	List<InvoiceDTO> getInvoicesByDate(Timestamp startDate, Timestamp endDate);
	
}
