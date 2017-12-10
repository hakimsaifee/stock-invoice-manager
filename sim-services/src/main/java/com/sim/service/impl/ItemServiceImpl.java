package com.sim.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.common.util.MapperUtil;
import com.sim.dao.ItemDAO;
import com.sim.domain.Item;
import com.sim.dto.ItemDTO;
import com.sim.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDAO itemDAO;

	private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Transactional
	public void create(ItemDTO itemDTO) {

		Item item = MapperUtil.map(itemDTO, Item.class);
		Item createdItem = itemDAO.create(item);
		if (createdItem != null) {
			LOGGER.info("Updating Generated Barcode : {} ", createdItem.getId());
			createdItem.setBarcode(String.valueOf(createdItem.getId()));
			itemDAO.update(createdItem);
			LOGGER.info("Barcode Updated Successfully : {} ", createdItem.getId());
		} else {
			LOGGER.error("Failed to update the barcode.");
		}
	}

}
