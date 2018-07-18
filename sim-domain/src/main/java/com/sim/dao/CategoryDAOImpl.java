package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.Category;

@Component("categoryDAO")
public class CategoryDAOImpl extends AbstractDAO<Category, Long> implements CategoryDAO{

}
