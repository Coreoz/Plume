package com.coreoz.plume.db.querydsl.generation;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.coreoz.plume.db.querydsl.crud.CrudEntityQuerydsl;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.Supertype;
import com.querydsl.sql.codegen.ExtendedBeanSerializer;

public class IdBeanSerializer extends ExtendedBeanSerializer {

	private boolean useJacksonAnnotation;

	public IdBeanSerializer() {
		setPrintSupertype(true);

		this.useJacksonAnnotation = false;
	}

	@Override
	public void serialize(EntityType model, SerializerConfig serializerConfig, CodeWriter writer) throws IOException {
		injectIdInterface(model);
		if(useJacksonAnnotation) {
			injectJacksonAnnotation(model);
		}

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

	@SuppressWarnings("rawtypes")
	protected void injectJacksonAnnotation(EntityType model) {
		JsonSerialize jacksonAnnotation = new JsonSerialize() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return JsonSerialize.class;
			}

			@Override
			public Class<? extends JsonSerializer> using() {
				return ToStringSerializer.class;
			}

			@Override
			public Class<? extends JsonSerializer> contentUsing() {
				return null;
			}

			@Override
			public Class<? extends JsonSerializer> keyUsing() {
				return null;
			}

			@Override
			public Class<? extends JsonSerializer> nullsUsing() {
				return null;
			}

			@Override
			public Class<?> as() {
				return null;
			}

			@Override
			public Class<?> keyAs() {
				return null;
			}

			@Override
			public Class<?> contentAs() {
				return null;
			}

			@Override
			public Typing typing() {
				return null;
			}

			@Override
			public Class<? extends Converter> converter() {
				return null;
			}

			@Override
			public Class<? extends Converter> contentConverter() {
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Inclusion include() {
				return null;
			}

		};

		for(Property property : model.getProperties()) {
			if(Long.class.equals(property.getType().getJavaClass())) {
				property.addAnnotation(jacksonAnnotation);
			}
		}
	}

	public void setUseJacksonAnnotation(boolean useJacksonAnnotation) {
		this.useJacksonAnnotation = useJacksonAnnotation;
	}

}
