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

import com.sim.dto.ItemDTO;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class JasperBarcodeBuilder implements BarcodeBuilder {

	private final static Logger LOGGER = LoggerFactory.getLogger(JasperBarcodeBuilder.class);

	private static final Path PDF_INVOICE_PATH = Paths.get(System.getProperty("PDF_BARCODE_PATH", "barcodes"));
	private static final String FILE_EXTENSION = ".pdf";
	private static final String BARCODE_TEMPLATE = "barcodeTemplate.jrxml";

	@Override
	public void generateBarcode(ItemDTO item, boolean isPdfRequired) throws Exception {
		InputStream stream = null;

		try (java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream()) {
			stream = new ClassPathResource(BARCODE_TEMPLATE).getInputStream();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("barcode", item.getBarcode());
			parameters.put("mrp", item.getMrp());
			parameters.put("rrp", item.getRrp());
			parameters.put("name", (item.getName() != null && item.getName().length() > 25)
					? item.getName().substring(0, 25) : item.getName());

			// compiles jrxml
			JasperCompileManager.compileReportToStream(stream, os);
			InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
			// fills compiled report with parameters and a connection
			JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource());
			if (!isPdfRequired) {
				boolean isPrinted = JasperPrintManager.printReport(print, false);
				LOGGER.info("Barcode Printing Status : {} ", isPrinted);
			} else {
				// exports report to pdf
				exportToPdf(print, PDF_INVOICE_PATH,
						item.getBarcode() + "-" + System.currentTimeMillis() + FILE_EXTENSION);
			}
			System.out.println("Generated");

		} catch (Exception e) {
			LOGGER.error("Error occurred while printing the barcode : " + e);
			throw new Exception(e);
		}

	}

	@Override
	public void generateBarcode(ItemDTO item) throws Exception {
		generateBarcode(item, false);
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

	public static void main(String[] args) {
		ItemDTO item = new ItemDTO();
		item.setBarcode("3345");
		item.setMrp(33.0);
		item.setRrp(32.0);

		BarcodeBuilder bb = new JasperBarcodeBuilder();
		try {
			bb.generateBarcode(item, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
