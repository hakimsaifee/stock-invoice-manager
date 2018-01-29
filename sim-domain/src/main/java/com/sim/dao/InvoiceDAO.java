package com.sim.dao;

import java.sql.Timestamp;
import java.util.List;

import com.sim.domain.Invoice;

public interface InvoiceDAO extends DAO<Invoice, Long>{

	List<Invoice> getInvoicesByDate(Timestamp startDate, Timestamp endDate);
}
