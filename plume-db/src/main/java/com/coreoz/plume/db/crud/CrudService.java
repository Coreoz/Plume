package com.coreoz.plume.db.crud;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

    @Nonnull
	public List<T> findAll() {
		return crudDao.findAll();
	}

	@Nullable
	public T findById(@Nullable Long id) {
		if(id == null) {
			return null;
		}
		return crudDao.findById(id);
	}

    @Nonnull
	public Optional<T> findByIdOptional(@Nullable Long id) {
		return Optional.ofNullable(findById(id));
	}

    @Nonnull
	public T save(@Nonnull T entityToSave) {
		return crudDao.save(entityToSave);
	}

	public void delete(@Nonnull Long id) {
		crudDao.delete(id);
	}

}
