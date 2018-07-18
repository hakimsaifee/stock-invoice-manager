package com.sim.common.util;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sim.common.constant.MessageConstant;
import com.sim.dto.BillModel;
import com.sim.dto.InvoiceDTO;
import com.sim.dto.ItemInvoiceDTO;

@Component
public class MapperUtil {
	
	@Autowired
	private Environment env;

	public static <E> E map(Object source, Class<E> clz) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(source, clz);
	}

	public BillModel prepareInvoiceModel(InvoiceDTO invoiceDTO) {
		BillModel billModel = new BillModel();
		billModel.setId(invoiceDTO.getId());
		billModel.setCreatedTs(invoiceDTO.getCreatedTs());
		billModel.setCustomer(invoiceDTO.getCustomer());
		billModel.setDiscount(invoiceDTO.getDiscount());
		billModel.setAdditionalCharges(invoiceDTO.getAdditionalCharges());
		billModel.setItemInvoices(invoiceDTO.getItemInvoices());
		billModel.setPaymentMode(invoiceDTO.getPaymentMode());
		billModel.setRoundOff(invoiceDTO.getRoundOff());
		billModel.setTotalAmount(invoiceDTO.getTotalAmount());
		billModel.setStaff(invoiceDTO.getStaff());
  		billModel.setGstNumber(env.getProperty(MessageConstant.GST_NUMBER));
		billModel.setShopName(env.getProperty(MessageConstant.SHOP_NAME));
		billModel.setShopAddress(env.getProperty(MessageConstant.SHOP_ADDRESS));
		billModel.setContactNumber(env.getProperty(MessageConstant.CONTACT_NUMBER));
		

		prepareRemainingDetails(invoiceDTO.getItemInvoices(), billModel);
		return billModel;
	}

	private static void prepareRemainingDetails(List<ItemInvoiceDTO> itemInvoices, BillModel billModel) {

		if(itemInvoices != null && !itemInvoices.isEmpty()) {
			double totalItemsSold =0.0;
			double totalSavings = billModel.getDiscount();
			for (ItemInvoiceDTO itemInvoiceDTO : itemInvoices) {
				//Calculation total items sold.
				totalItemsSold =totalItemsSold + itemInvoiceDTO.getPurchasedQuantity();
				//Calculation total savings.
				double discount = itemInvoiceDTO.getItem().getMrp()- itemInvoiceDTO.getProductSellingPrice();
				totalSavings = totalSavings +  (discount == 0 ? 0.0 : discount * itemInvoiceDTO.getPurchasedQuantity());
				totalSavings = MathUtil.preciseDouble(totalSavings, 2);
			}
			billModel.setTotalItemSold(totalItemsSold);
			billModel.setTotalSavings(totalSavings);
		}
	}
}
