package com.sim.dao;

import com.sim.domain.UserDetail;

public interface UserDetailDAO extends DAO<UserDetail, Long>{

	UserDetail getUserByUserName(String username);
}
