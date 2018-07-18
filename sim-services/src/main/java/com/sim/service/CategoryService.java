package com.sim.service;

import java.util.List;

import com.sim.dto.CategoryDTO;


public interface CategoryService {

	CategoryDTO findByPK(Long id);

	List<CategoryDTO> findAll();
	
	CategoryDTO create(CategoryDTO categoryDTO);
	
	
}
