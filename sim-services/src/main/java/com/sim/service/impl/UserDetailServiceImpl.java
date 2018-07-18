package com.sim.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sim.dao.UserDetailDAO;
import com.sim.domain.UserDetail;
import com.sim.dto.UserDetailDTO;
import com.sim.service.UserDetailService;

@Service
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	private UserDetailDAO userDetailDAO;

	@Transactional
	public void create(UserDetailDTO userDetailDTO) {

		ModelMapper modelMapper = new ModelMapper();
		UserDetail userDetail = modelMapper.map(userDetailDTO, UserDetail.class);
		userDetailDAO.create(userDetail);
	}

	@Override
	public UserDetailDTO getUserByUsername(String username) {
		UserDetail userDetail = userDetailDAO.getUserByUserName(username);
		if (userDetail != null) {
			userDetail.getUserRoles();
			ModelMapper modelMapper = new ModelMapper();
			UserDetailDTO userDetailDTO = modelMapper.map(userDetail, UserDetailDTO.class);
			return userDetailDTO;
		}
		return null;
	}

}
