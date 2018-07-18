package com.sim.dao;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sim.common.exception.DBException;
import com.sim.domain.UserDetail;

@Component("userDao")
public class UserDetailDAOImpl extends AbstractDAO<UserDetail, Long> implements UserDetailDAO{

	private final Logger LOGGER = LoggerFactory.getLogger(UserDetailDAOImpl.class);
	@Override
	public UserDetail getUserByUserName(String username){
		try{
			Query query = getEntityManager().createQuery("from UserDetail where username= :username");
			query.setParameter("username", username);
			return (UserDetail) query.getSingleResult();
		}catch(Exception e) {
			LOGGER.error("Failed to fetch User Detail : {} ", e);
			throw new DBException(e.getMessage());
		}
	}

}
 