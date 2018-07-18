package com.sim.common.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sim.common.invoice.generation.BarcodeBuilder;
import com.sim.common.invoice.generation.InvoiceBuilder;
import com.sim.common.invoice.generation.JasperBarcodeBuilder;
import com.sim.common.invoice.generation.JasperInvoiceBuilder;
import com.sim.dto.BillModel;
import com.sim.dto.ItemDTO;
import com.sim.dto.ItemInvoiceDTO;

public class ReportBuilderTest {

	@Test
	public void testReport() {
		BillModel billModel = new BillModel();
		billModel.setId(112);
		billModel.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		billModel.setShopName("Rangwala");
		billModel.setShopAddress("24,Dhaba road,opp.Gebi sahab gali");
		billModel.setContactNumber("8269010039");
		billModel.setTotalAmount(23356.00);
		billModel.setDiscount(33.00);
		billModel.setAdditionalCharges(5.00);
		billModel.setTotalSavings(1223.0);
		billModel.setTotalItemSold(21.0);
		List<ItemInvoiceDTO> itemInvoices = new ArrayList<ItemInvoiceDTO>();
		
		ItemDTO item = new ItemDTO();
		item.setName("LX");
		item.setRrp(23.00);
		item.setMrp(30.00);
		for(int x = 0 ; x < 2; x++) {
			itemInvoices.add(getInvoiceDTO(item));
		}
		billModel.setItemInvoices(itemInvoices);
		InvoiceBuilder ib = new JasperInvoiceBuilder();
		try {
			ib.generateInvoice(billModel, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ItemInvoiceDTO getInvoiceDTO(ItemDTO item) {
		ItemInvoiceDTO itemInvoiceDTO = new ItemInvoiceDTO();
		itemInvoiceDTO.setPurchasedQuantity(3);
		itemInvoiceDTO.setAmount(334);
		itemInvoiceDTO.setItem(item);
		return itemInvoiceDTO;
	}

	@Test
	public void test() {
	
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
