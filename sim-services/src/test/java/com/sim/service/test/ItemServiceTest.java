package com.sim.service.test;
import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sim.dto.CategoryDTO;
import com.sim.dto.ItemDTO;
import com.sim.dto.StockDTO;
import com.sim.service.ItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context-domain.xml" })
public class ItemServiceTest {

	
	@Autowired
	private ItemService itemService;
	
	@Test
	public void createUserTest()
	{

		ItemDTO item = new ItemDTO();
		item.setBarcode("");
		item.setName("SchoolBag");
		item.setMrp(22.00);
		item.setRrp(11.00);
		item.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		
		//Create/Map category
		CategoryDTO category = new CategoryDTO();
		category.setCategoryName("Bag");
		item.setCategory(category);
		
		//Create stock
		StockDTO stockDTO = new StockDTO();
		stockDTO.setQuantity(3);
		
		stockDTO.setItem(item);
		item.setStock(stockDTO);
		itemService.create(item );
		System.out.println("user detail created.");
	}
}
