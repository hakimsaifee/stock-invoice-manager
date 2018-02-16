package com.sim.domain.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sim.dao.UserDetailDAO;
import com.sim.domain.UserDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class UserTest {

	
	@Autowired
	private UserDetailDAO userDetailDAO;
	
	@Test
	public void createUserTest()
	{
		UserDetail domainObj = new UserDetail();
		domainObj.setUserName("hakim");
		domainObj.setPassword("1234");
		userDetailDAO.create(domainObj );
		System.out.println("user detail created.");
	}
}
