package com.sim.dao;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.sim.domain.Item;

@Component("itemDAO")
public class ItemDAOImpl extends AbstractDAO<Item, Integer> implements ItemDAO{

	public Item findByBarcode(String barcode) {
		try {
			Query query = getEntityManager().createQuery("from Item where barcode = :barcode");
			query.setParameter("barcode", barcode);
			return (Item) query.getSingleResult();
		} catch(Exception e) {
			System.out.println("ex");
		}
		return null;
	}

	
}
