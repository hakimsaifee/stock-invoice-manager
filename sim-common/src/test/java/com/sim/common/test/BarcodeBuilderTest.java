package com.sim.common.test;

import org.junit.Test;

import com.sim.common.invoice.generation.BarcodeBuilder;
import com.sim.common.invoice.generation.JasperBarcodeBuilder;
import com.sim.dto.ItemDTO;

public class BarcodeBuilderTest {

	@Test
	public void testReport() {
	
		ItemDTO item = new ItemDTO();
		item.setBarcode("1236653112");
		item.setMrp(33.0);
		item.setRrp(32.0);
		
		BarcodeBuilder bb = new JasperBarcodeBuilder();
		try {
			bb.generateBarcode(item, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
