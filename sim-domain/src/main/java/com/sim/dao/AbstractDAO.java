package com.sim.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDAO<T, PK extends Serializable> implements DAO<T, PK> {

	/** The logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);

	protected Class<T> domainClass;

	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		domainClass = (Class<T>) ((java.lang.reflect.ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public T create(T domainObj) {

		LOGGER.info("Creating domain object : {} " , domainObj);
		T persitedEntity = domainObj;

		try {
			entityManager.persist(domainObj);
		} catch (EntityExistsException e) {
			LOGGER.error("Failed to create domain object. {} ", e);
			persitedEntity = entityManager.merge(domainObj);
		}
		return persitedEntity;

	}

	public T update(T domainObj) {
		LOGGER.info("Updating domain object : {} " , domainObj);
		T mergedObj = null;
		try {
			mergedObj = entityManager.merge(domainObj);
		} catch (Exception e) {
			LOGGER.error("Failed to update domain object. {} ", e);
		}
		return mergedObj;
	}

	public void delete(T domainObj) {
		LOGGER.info("Deleting domain object : {} " , domainObj);
		try {
			entityManager.remove(domainObj);
		} catch (Exception e) {
			LOGGER.error("Failed to delete domain object. {} ", e);
		}
	}

	public void delete(PK id) {
		LOGGER.info("Deleting domain object for id : {} " , id);
		try {
			entityManager.remove(id);
		} catch (Exception e) {
			LOGGER.error("Failed to delete domain object by id. {} ", e);
		}

	}

	public T findByPK(PK id) {
		LOGGER.info("Finding domain object for id : {} " , id);
		try {
			return entityManager.find(domainClass, id);
		} catch (Exception e) {
			LOGGER.error("Failed to fetch domain object by id. {} ", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		LOGGER.info("Finding all domain objects.");
		List<T> results = null;
		Query query;
		try {
			query = entityManager.createQuery("from " + domainClass.getSimpleName());
			results = (List<T>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Failed to find all domain objects. : {} ", e);
		}
		return results;
	}
}
