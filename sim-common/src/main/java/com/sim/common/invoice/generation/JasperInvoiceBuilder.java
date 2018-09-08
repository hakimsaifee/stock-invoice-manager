package com.sim.common.invoice.generation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.sim.dto.BillModel;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class JasperInvoiceBuilder implements InvoiceBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(JasperInvoiceBuilder.class);
	private static final Path PDF_INVOICE_PATH = Paths.get(System.getProperty("PDF_INVOICE_PATH", "invoices"));
	private static final String FILE_EXTENSION = ".pdf";
	private static final String INVOICE_TEMPLATE = "invoiceTemplate.jrxml";

	@Override
	public void generateInvoice(BillModel billModel, boolean isPDF) throws Exception {
		InputStream stream = null;

		try (java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream()) {
			stream = new ClassPathResource(INVOICE_TEMPLATE).getInputStream();

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(billModel.getItemInvoices());

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("dataSouce", dataSource);
			parameters.put("storeNameField", billModel.getShopName());
			parameters.put("storeAddressField", billModel.getShopAddress());
			parameters.put("contactNumber", billModel.getContactNumber());
			parameters.put("invoiceIdField", billModel.getId());
			parameters.put("customerNameField", (billModel.getBillName() == null || billModel.getBillName().isEmpty())
					? "N/A" : billModel.getBillName());
			parameters.put("invoiceDateField", billModel.getCreatedTs());
			parameters.put("payableAmount", billModel.getTotalAmount());
			parameters.put("roundOff", billModel.getRoundOff());
			parameters.put("discount", billModel.getDiscount());
			parameters.put("additionalCharges", billModel.getAdditionalCharges());
			parameters.put("totalSavings", billModel.getTotalSavings());
			parameters.put("totalItemSold", billModel.getTotalItemSold());
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

			// compiles jrxml
			JasperCompileManager.compileReportToStream(stream, os);
			InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
			// fills compiled report with parameters and a connection
			JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource());
			if (!isPDF) {
				boolean isPrinted = JasperPrintManager.printReport(print, false);
				LOGGER.info("Invoice Printing Status : {} ", isPrinted);
			} else {
				// exports report to pdf
				exportToPdf(print, PDF_INVOICE_PATH, billModel.getId() + FILE_EXTENSION);
			}
			LOGGER.trace("Invoice Printed.");

		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void generateInvoice(BillModel billModel) throws Exception {
		generateInvoice(billModel, false);
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void exportToPdf(JasperPrint print, Path filePath, String fileName) throws JRException, IOException {
		if (filePath != null && !filePath.toString().isEmpty() && !filePath.toFile().exists()) {
			try {
				Files.createDirectories(filePath);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		Path file = Paths.get(filePath + File.separator + fileName);
		if (filePath.toFile().exists()) {
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteOS);
			exporter.exportReport();
			byteOS.close();
			ByteArrayOutputStream outputSream = byteOS;
			InputStream is = new ByteArrayInputStream(outputSream.toByteArray());
			Files.copy(is, file);
		}
	}

}
