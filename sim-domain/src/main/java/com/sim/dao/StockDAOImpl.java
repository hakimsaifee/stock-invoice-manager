package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.Stock;

@Component("stockDAO")
public class StockDAOImpl extends AbstractDAO<Stock, Long> implements StockDAO{

}
