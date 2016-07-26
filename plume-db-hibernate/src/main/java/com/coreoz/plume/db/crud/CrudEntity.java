package com.coreoz.plume.db.crud;

import java.util.Objects;

public abstract class CrudEntity<I> {

	public abstract I getId();

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		
		return Objects.equals(getId(), ((CrudEntity<I>) obj).getId());
	}
	
}
