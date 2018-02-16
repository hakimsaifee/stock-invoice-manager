package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.Stock;

@Component("categoryDAO")
public class StockDAOImpl extends AbstractDAO<Stock, Long> implements StockDAO{

}
