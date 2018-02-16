package com.sim.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.common.util.MapperUtil;
import com.sim.dao.InvoiceDAO;
import com.sim.dao.StockDAO;
import com.sim.domain.Invoice;
import com.sim.domain.ItemInvoice;
import com.sim.domain.Stock;
import com.sim.dto.InvoiceDTO;
import com.sim.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceDAO invoiceDAO;

	@Autowired
	private StockDAO stockDAO;

	private final Logger LOGGER = LoggerFactory.getLogger(InvoiceServiceImpl.class);

	@Transactional
	public InvoiceDTO create(InvoiceDTO invoiceDTO) {
		Invoice invoice = MapperUtil.map(invoiceDTO, Invoice.class);
		Invoice createdInvoice = invoiceDAO.create(invoice);
		LOGGER.trace("Invoice Created Successfully : {} ", createdInvoice);

		// update stock..
		LOGGER.trace("Updating the stock for Invoice : {} ", createdInvoice.getId());
		updateStock(invoice);
		
		return MapperUtil.map(createdInvoice, InvoiceDTO.class);
	}

	@Transactional
	private void updateStock(Invoice invoice) {
		for (ItemInvoice itemInvoice : invoice.getItemInvoices()) {
			Stock stock = itemInvoice.getItem().getStock();
			if (stock != null) {

				double updatedQuantity = stock.getQuantity() - itemInvoice.getPurchasedQuantity();
				if (updatedQuantity < 1) {
					updatedQuantity = 0.0;
				}
				stock.setQuantity(updatedQuantity);
				stockDAO.update(stock);
			}
		}
	}

	@Transactional
	public List<InvoiceDTO> getInvoicesByDate(Timestamp startDate, Timestamp endDate) {
		LOGGER.info("Finding Invoices Between Dates : {} - {} ", startDate, endDate);
		List<Invoice> invoiceDAOs = invoiceDAO.getInvoicesByDate(startDate, endDate);
		LOGGER.info("Found invoices : {} ", invoiceDAOs != null ? invoiceDAOs.size() : "null");
		if (invoiceDAOs != null && !invoiceDAOs.isEmpty()) {
			List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>();
			for (Invoice invoiceDAO : invoiceDAOs) {
				invoiceDTOs.add(MapperUtil.map(invoiceDAO, InvoiceDTO.class));
			}
			return invoiceDTOs;
		}
		return null;
	}

}
