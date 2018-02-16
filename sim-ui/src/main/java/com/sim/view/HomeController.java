package com.sim.view;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.sim.InvoiceStartup;

import javafx.fxml.FXML;

@Component
public class HomeController{

	private static final String ADD_PRODUCT_PAGE = "view/addProduct.fxml";
	private static final String VIEW_PRODUCTS_PAGE = "view/viewProducts.fxml";
	private static final String INVOICE_SUMMARY_PAGE = "view/invoiceSummary.fxml";
	private static final String INVOICE_GENERATION_PAGE = "view/invoiceGeneration.fxml";

	@FXML
	public void gotoAddProducts() throws IOException {
		InvoiceStartup.navigate(ADD_PRODUCT_PAGE);
		
		
	}

	@FXML
	public void gotoViewProducts() throws IOException {
		InvoiceStartup.navigate(VIEW_PRODUCTS_PAGE);
	}

	@FXML
	public void gotoInvoice() throws IOException {
		InvoiceStartup.navigate(INVOICE_GENERATION_PAGE);
	}

	@FXML
	public void gotoInvoiceSummary() throws IOException {
		InvoiceStartup.navigate(INVOICE_SUMMARY_PAGE);
	}
	
}
