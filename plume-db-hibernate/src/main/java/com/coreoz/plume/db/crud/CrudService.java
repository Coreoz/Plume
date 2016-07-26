package com.coreoz.plume.db.crud;

import java.util.List;
import java.util.Optional;

public class CrudService<T> {

	private final CrudDao<T> crudDao;

	public CrudService(CrudDao<T> crudDao) {
		this.crudDao = crudDao;
	}

	public List<T> fetchAll() {
		return crudDao.fetchAll();
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

	public T insert(T entityToInsert) {
		return crudDao.insert(entityToInsert);
	}

	public T update(T entityToUpdate) {
		return crudDao.update(entityToUpdate);
	}

	public void delete(Long id) {
		crudDao.delete(id);
	}

}
