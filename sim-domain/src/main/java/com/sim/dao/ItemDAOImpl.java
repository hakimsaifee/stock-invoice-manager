package com.sim.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sim.domain.Item;


@Component("itemDAO")
public class ItemDAOImpl extends AbstractDAO<Item, Integer> implements ItemDAO{

	private final Logger LOGGER = LoggerFactory.getLogger(ItemDAOImpl.class);
		
	public Item findByBarcode(String barcode) {
		try {
			Query query = getEntityManager().createQuery("from Item where barcode = :barcode");
			query.setParameter("barcode", barcode);
			return (Item) query.getSingleResult();
		} catch(NoResultException nre){
			LOGGER.error("No Record Found for Barcode : {} ", barcode);
		}catch(Exception e) {
			LOGGER.error("Error occured while getting item by barcode. {} ", e);
		}
		return null;
	}

	
}
