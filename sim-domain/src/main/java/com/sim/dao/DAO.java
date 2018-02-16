package com.sim.dao;

import java.io.Serializable;
import java.util.List;

public interface DAO<T,PK extends Serializable> {

	 T create(T domainObj);
	
	 T findByPK(PK id);
	 
	 void delete(T domainObj);
	 
	 T update(T domainObj);
	 
	 List<T> findAll();
	 
	 void delete(PK id);
	 
	 
}
