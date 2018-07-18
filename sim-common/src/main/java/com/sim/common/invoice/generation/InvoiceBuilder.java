package com.sim.common.invoice.generation;

import com.sim.dto.BillModel;

public interface InvoiceBuilder {

	void generateInvoice(BillModel billModel, boolean isPDF) throws Exception;

	void generateInvoice(BillModel billModel) throws Exception;

}
