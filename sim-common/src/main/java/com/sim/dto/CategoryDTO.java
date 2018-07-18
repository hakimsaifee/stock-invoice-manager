package com.sim.dto;

import java.util.List;

public class CategoryDTO  implements Comparable<CategoryDTO>{

	private long id;

	private String categoryName;

	private List<ItemDTO> items;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public int compareTo(CategoryDTO category) {
		return this.categoryName.compareToIgnoreCase(category.categoryName);
	}
	
	
}
