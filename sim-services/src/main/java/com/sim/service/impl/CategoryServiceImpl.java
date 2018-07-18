package com.sim.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.dao.CategoryDAO;
import com.sim.domain.Category;
import com.sim.dto.CategoryDTO;
import com.sim.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;

	private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Override
	public CategoryDTO findByPK(Long id) {
		try {
			Category category = categoryDAO.findByPK(id);
			if (category != null) {
				ModelMapper modelMapper = new ModelMapper();
				return modelMapper.map(category, CategoryDTO.class);
			}
		} catch (Exception e) {
			LOGGER.error("Failed to find Category by ID : {} ", id);
		}
		return null;
	}

	@Override
	public List<CategoryDTO> findAll() {
		try {
			List<Category> categories = categoryDAO.findAll();
			if (categories != null && !categories.isEmpty()) {
				List<CategoryDTO> categoryDTOs = new ArrayList<>();
				for (Category category : categories) {
					ModelMapper modelMapper = new ModelMapper();
					categoryDTOs.add(modelMapper.map(category, CategoryDTO.class));
				}
				return categoryDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("Failed to find all Categories.");
		}
		return null;
	}

	@Transactional
	public CategoryDTO create(CategoryDTO categoryDTO) {

		try {
			ModelMapper modelMapper = new ModelMapper();
			Category category = modelMapper.map(categoryDTO, Category.class);
			Category createdCategory = categoryDAO.create(category);
			if (createdCategory != null) {
				return modelMapper.map(createdCategory, CategoryDTO.class);
			}
		} catch (Exception e) {
			LOGGER.error("Failed to create category : " + e);
		}
		return null;
	}

}
