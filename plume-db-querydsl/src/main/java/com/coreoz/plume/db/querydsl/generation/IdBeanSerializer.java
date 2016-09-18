package com.coreoz.plume.db.querydsl.generation;

import java.io.IOException;

import com.coreoz.plume.db.querydsl.crud.CrudEntityQuerydsl;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.Supertype;
import com.querydsl.sql.codegen.ExtendedBeanSerializer;

public class IdBeanSerializer extends ExtendedBeanSerializer {

	public IdBeanSerializer() {
		setPrintSupertype(true);
	}

	@Override
	public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
		injectIdInterface(model);

		super.serialize(model, serializerConfig, writer);
	}

	protected void injectIdInterface(EntityType model) {
		for(Property property : model.getProperties()) {
			if("id".equals(property.getName()) && Long.class.equals(property.getType().getJavaClass())) {
				model.addSupertype(new Supertype(new ClassType(CrudEntityQuerydsl.class)));
				break;
			}
		}
	}

}
