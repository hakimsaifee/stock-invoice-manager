package com.sim.service.test;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sim.dto.RoleDTO;
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
		domainObj.setUsername("saifee1");
		domainObj.setPassword("1234");
		RoleDTO role = new RoleDTO();
		role.setRole("USER");
		List<RoleDTO> roles = new ArrayList<>();
		roles.add(role);
		domainObj.setUserRoles(roles);
		userDetailService.create(domainObj );
		System.out.println("user  created.");
	}
	
}
