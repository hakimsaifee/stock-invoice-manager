package com.sim.service.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sim.dto.UserDetailDTO;
import com.sim.service.UserDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context-domain.xml" })
public class UserTest {

	
	@Autowired
	private UserDetailService userDetailService;
	
	@Test
	public void createUserTest()
	{
		UserDetailDTO domainObj = new UserDetailDTO();
		domainObj.setUserName("hakim");
		domainObj.setPassword("1234");
		userDetailService.create(domainObj );
		System.out.println("user detail created.");
	}
}
