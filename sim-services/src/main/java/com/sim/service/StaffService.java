package com.sim.service;

import java.util.List;

import com.sim.dto.StaffDTO;

public interface StaffService {

	StaffDTO findByPK(Long id);

	List<StaffDTO> findAll();
	
	
}
