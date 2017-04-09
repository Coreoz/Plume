package com.coreoz.plume.db.crud;

import java.util.List;
import java.util.Optional;

/**
 * A generic service that expose {@link CrudDao} operations.
 *
 * The common use is to extend this service.
 */
public class CrudService<T> {

	private final CrudDao<T> crudDao;

	public CrudService(CrudDao<T> crudDao) {
		this.crudDao = crudDao;
	}

	public List<T> findAll() {
		return crudDao.findAll();
	}

	public T findById(Long id) {
		if(id == null) {
			return null;
		}
		return crudDao.findById(id);
	}

	public Optional<T> findByIdOptional(Long id) {
		return Optional.ofNullable(crudDao.findById(id));
	}

	public T save(T entityToSave) {
		return crudDao.save(entityToSave);
	}

	public void delete(Long id) {
		crudDao.delete(id);
	}

}
