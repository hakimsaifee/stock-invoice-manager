package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.Staff;

@Component("staffDAO")
public class StaffDAOImpl extends AbstractDAO<Staff, Long> implements StaffDAO{

}
