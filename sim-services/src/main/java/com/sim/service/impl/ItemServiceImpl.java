package com.sim.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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

	private static final String DUMMY_BARCODE_IMAGE = "barcodeSample.png";

	@Autowired
	private ItemDAO itemDAO;

	private final Logger LOGGER = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Transactional
	public ItemDTO create(ItemDTO itemDTO) {

		Item item = MapperUtil.map(itemDTO, Item.class);
		Item createdItem = itemDAO.create(item);
		if (createdItem != null && (createdItem.getBarcode() == null || createdItem.getBarcode().trim().isEmpty())) {
			LOGGER.info("Updating Generated Barcode : {} ", createdItem.getId());
			createdItem.setBarcode(String.valueOf(createdItem.getId()));
			Item updatedItem = itemDAO.update(createdItem);
			LOGGER.info("Barcode Updated Successfully : {} ", createdItem.getId());
			
			return MapperUtil.map(updatedItem, ItemDTO.class);
		} else {
			LOGGER.error("Barcode is provided by user.");
			return MapperUtil.map(createdItem, ItemDTO.class);
		}
	}

	@Transactional
	public void removeItem(Integer id) {
		Item item = itemDAO.findByPK(id);
		itemDAO.delete(item);

	}

	public List<ItemDTO> getAll() {
		try {
			List<Item> items = itemDAO.findAll();

			List<ItemDTO> itemDTOs = new ArrayList<ItemDTO>();
			if (items != null) {
				for (Item item : items) {
					itemDTOs.add(MapperUtil.map(item, ItemDTO.class));
				}
			}
			return itemDTOs;
		} catch (Exception e) {
			LOGGER.error("Failed to fetch the item details {} ", e);
		}
		return null;
	}

	public ItemDTO findByBarcode(String barcode) {
		try {
			Item item = itemDAO.findByBarcode(barcode);
			
			if(item == null) {
				LOGGER.warn("No records found for the given barcode : {} ", barcode);
				return null;
			}
			
			ItemDTO itemDTO = MapperUtil.map(item, ItemDTO.class);
			return itemDTO;
		} catch (Exception e) {
			LOGGER.error("Failed to find item details by barcode. : {} ", e);
		}
		return null;
	}

	@Override
	@Transactional
	public ItemDTO update(ItemDTO itemDTO) {
		try {
			Item item = MapperUtil.map(itemDTO, Item.class);
			Item updatedItem = itemDAO.update(item);
			if(updatedItem == null) {
				LOGGER.error("Unalbe to perform update operation : {} ", updatedItem);
				return null;
			}
			return MapperUtil.map(item, ItemDTO.class);
		}catch(Exception e) {
			LOGGER.error("Failed to update Item : {}" , itemDTO);
			return null;
		}
	}
}
