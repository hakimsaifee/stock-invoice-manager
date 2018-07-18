package com.invoice.generation.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class ReportGenerator {

	@SuppressWarnings("deprecation")	
	public static void generateReport() {

		List<ItemModel> items = new ArrayList<ItemModel>();

		ItemModel model = new ItemModel();
		model.setName("Haldi 200");
		model.setQuantity("2");
		model.setRate(33.00);
		model.setAmount(66.00);
		model.setTemp1(new Temp("Dummy Shop"));
		
		ItemModel model1 = new ItemModel();
		model1.setName("Lux 200");
		model1.setQuantity("20");
		model1.setRate(13.00);
		model1.setAmount(260.00);
		
		ItemModel model11 = new ItemModel();
		model11.setName("Surf");
		model11.setQuantity("30");
		model11.setRate(13.00);
		model11.setAmount(260.00);
		
		ItemModel model12 = new ItemModel();
		model12.setName("Toothpaste");
		model12.setQuantity("60");
		model12.setRate(10.00);
		model12.setAmount(600.00);
		
		ItemModel model13 = new ItemModel();
		model13.setName("Poha");
		model13.setQuantity("1");
		model13.setRate(130.00);
		model13.setAmount(130.00);
		
		ItemModel model14 = new ItemModel();
		model14.setName("All Out");
		model14.setQuantity("20");
		model14.setRate(3.00);
		model14.setAmount(60.00);
		
		items.add(model);
		items.add(model1);
		items.add(model11);
		items.add(model12);
		items.add(model13);
		items.add(model14);
		
		InputStream stream = null;
		String reportName = "demoTemplate.jrxml";

		try (java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream()) {
			stream = new ClassPathResource(reportName).getInputStream();

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("dataSouce", dataSource);
			Temp temp = new Temp("A.M Rangwala");
			parameters.put("TEMP", temp);
			parameters.put("storeAddressField", "201, Gopal Mandir, Ujjain(M.P.)");
			parameters.put("invoiceField", "239901");
			parameters.put("dateField", new Timestamp(System.currentTimeMillis()));

			// compiles jrxml
			JasperCompileManager.compileReportToStream(stream, os);
			InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

			// fills compiled report with parameters and a connection
			JasperPrint print = JasperFillManager.fillReport(inputStream, parameters ,new JREmptyDataSource());
//			JasperPrintManager.printReport(print, false); 
			// exports report to pdf
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteOS);
			exporter.exportReport();
			byteOS.close();
			 ByteArrayOutputStream outputSream = byteOS;
	          InputStream is = new ByteArrayInputStream(outputSream.toByteArray());
	          // copy it to response's OutputStream
//	          Path created = Files.createFile(Paths.get("demo.pdf"));
	          Files.copy(is, Paths.get("demo1.pdf"));
	          System.out.println("d");

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	public static void main(String[] args) {

		generateReport();
	}
}
