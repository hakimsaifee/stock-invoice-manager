package com.sim.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sim.dao.StaffDAO;
import com.sim.domain.Staff;
import com.sim.dto.StaffDTO;
import com.sim.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffDAO staffDAO;

	private final Logger LOGGER = LoggerFactory.getLogger(StaffServiceImpl.class);

	@Override
	public StaffDTO findByPK(Long id) {
		try {
			Staff staff = staffDAO.findByPK(id);
			if (staff != null) {
				ModelMapper modelMapper = new ModelMapper();
				return modelMapper.map(staff, StaffDTO.class);
			}
		} catch (Exception e) {
			LOGGER.error("Failed to find Staff by ID : {} ", id);
		}
		return null;
	}

	@Override
	public List<StaffDTO> findAll() {
		try {
			List<Staff> staffs = staffDAO.findAll();
			if (staffs != null && !staffs.isEmpty()) {
				List<StaffDTO> staffDTOs = new ArrayList<>();
				for (Staff staff : staffs) {
					ModelMapper modelMapper = new ModelMapper();
					staffDTOs.add(modelMapper.map(staff, StaffDTO.class));
				}
				return staffDTOs;
			}
		} catch (Exception e) {
			LOGGER.error("Failed to find all staffs.");
		}
		return null;
	}

}
