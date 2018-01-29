package com.sim.dao;

import com.sim.domain.Item;

public interface ItemDAO extends DAO<Item, Integer>{

	Item findByBarcode(String barcode);
}
