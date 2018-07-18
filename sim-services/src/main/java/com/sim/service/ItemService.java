package com.sim.service;

import java.util.List;

import com.sim.dto.ItemDTO;

public interface ItemService {

	ItemDTO create(ItemDTO itemDTO);
	
	ItemDTO update(ItemDTO itemDTO);

	void removeItem(Integer number);
	
	List<ItemDTO> getAll();
	
	ItemDTO findByBarcode(String barcode);
}
