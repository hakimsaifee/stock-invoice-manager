package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.Item;

@Component("itemDAO")
public class ItemDAOImpl extends AbstractDAO<Item, Integer> implements ItemDAO{

	
}
