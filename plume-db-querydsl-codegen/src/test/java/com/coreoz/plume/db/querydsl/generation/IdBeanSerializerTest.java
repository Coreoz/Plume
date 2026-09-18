package com.coreoz.plume.db.querydsl.generation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SimpleSerializerConfig;
import com.querydsl.codegen.utils.JavaWriter;
import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.SimpleType;
import com.querydsl.codegen.utils.model.TypeCategory;

class IdBeanSerializerTest {

	@Test
	void default_serializer_should_generate_jakarta_generated_annotation() throws Exception {
		String generated = generate(new IdBeanSerializer());

		assertThat(generated)
            .contains("import jakarta.annotation.Generated;")
            .doesNotContain("javax.annotation");
	}

	@Test
	void injected_generated_annotation_class_should_be_used() throws Exception {
		String generated = generate(new IdBeanSerializer(javax.annotation.processing.Generated.class));

		assertThat(generated)
            .contains("import javax.annotation.processing.Generated;")
            .doesNotContain("jakarta.annotation");
	}

	private String generate(IdBeanSerializer serializer) throws Exception {
		EntityType entityType = new EntityType(new SimpleType(
			TypeCategory.ENTITY, "com.example.User", "com.example", "User", false, false));
		entityType.addProperty(new Property(entityType, "id", new ClassType(Long.class)));

		StringWriter writer = new StringWriter();
		serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new JavaWriter(writer));
		return writer.toString();
	}

}
