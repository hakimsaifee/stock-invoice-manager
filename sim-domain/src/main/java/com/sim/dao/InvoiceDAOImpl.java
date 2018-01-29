package com.sim.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sim.domain.Invoice;

@Component("invoiceDAO")
public class InvoiceDAOImpl extends AbstractDAO<Invoice, Long> implements InvoiceDAO{

	private final Logger LOGGER = LoggerFactory.getLogger(InvoiceDAOImpl.class);
	
	public List<Invoice> getInvoicesByDate(Timestamp startDate, Timestamp endDate) {
		try{
			Query query = getEntityManager().createQuery("from Invoice where createdTs between :startDate and :endDate");
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.getResultList();
		}catch(Exception e) {
			LOGGER.error("Failed to fetch the invoices by date : {} ", e);
			return null;
		}
	}

}
