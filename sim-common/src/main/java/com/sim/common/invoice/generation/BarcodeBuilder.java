package com.sim.common.invoice.generation;

import com.sim.dto.ItemDTO;

public interface BarcodeBuilder {

	void generateBarcode(ItemDTO item) throws Exception;

	void generateBarcode(ItemDTO item, boolean isPdfRequired) throws Exception;
}
