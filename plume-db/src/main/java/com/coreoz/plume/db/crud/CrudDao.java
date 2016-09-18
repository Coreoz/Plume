package com.coreoz.plume.db.crud;

import java.util.List;

public interface CrudDao<T> {

	List<T> findAll();

	T findById(Long id);

	T save(T entityToUpdate);

	long delete(Long id);

}
