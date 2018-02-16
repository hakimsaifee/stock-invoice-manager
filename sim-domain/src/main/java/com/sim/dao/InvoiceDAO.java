package com.sim.dao;

import java.sql.Timestamp;
import java.util.List;

import com.sim.domain.Invoice;

public interface InvoiceDAO extends DAO<Invoice, Integer>{

	List<Invoice> getInvoicesByDate(Timestamp startDate, Timestamp endDate);
}
