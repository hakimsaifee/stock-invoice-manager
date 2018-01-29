package com.sim.service;

import java.util.List;

import com.sim.dto.ItemDTO;

public interface ItemService {

	ItemDTO create(ItemDTO itemDTO);

	void removeItem(Integer id);
	
	List<ItemDTO> getAll();
	
	ItemDTO findByBarcode(String barcode);
}
