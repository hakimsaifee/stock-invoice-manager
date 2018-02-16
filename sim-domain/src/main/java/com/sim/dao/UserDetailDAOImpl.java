package com.sim.dao;

import org.springframework.stereotype.Component;

import com.sim.domain.UserDetail;

@Component("userDao")
public class UserDetailDAOImpl extends AbstractDAO<UserDetail, Long> implements UserDetailDAO{

}
 