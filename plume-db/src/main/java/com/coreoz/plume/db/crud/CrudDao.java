package com.coreoz.plume.db.crud;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Describe a generic DAO with CRUD operations.
 */
public interface CrudDao<T> {

	List<T> findAll();

	@Nullable
	T findById(@Nonnull Long id);

	T save(@Nonnull T entityToUpdate);

	long delete(@Nonnull Long id);

}
