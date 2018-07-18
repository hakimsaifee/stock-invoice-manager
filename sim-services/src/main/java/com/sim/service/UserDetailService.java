package com.sim.service;

import com.sim.dto.UserDetailDTO;

public interface UserDetailService {

	void create(UserDetailDTO userDetailDTO);
	
	UserDetailDTO getUserByUsername(String username);
}
