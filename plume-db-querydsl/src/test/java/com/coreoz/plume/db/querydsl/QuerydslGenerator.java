package com.coreoz.plume.db.querydsl;

import java.io.File;
import java.sql.SQLException;

import com.coreoz.plume.db.querydsl.generation.IdBeanSerializer;
import com.coreoz.plume.db.transaction.TransactionManager;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.codegen.MetaDataExporter;
import com.querydsl.sql.types.JSR310LocalDateTimeType;
import com.querydsl.sql.types.JSR310LocalDateType;
import com.querydsl.sql.types.JSR310LocalTimeType;
import com.querydsl.sql.types.JSR310ZonedDateTimeType;
import com.querydsl.sql.types.Type;

public class QuerydslGenerator {

	public static void main(String[] args) {
		Configuration configuration = new Configuration(SQLTemplates.DEFAULT);
//		configuration.register(classType(JSR310InstantType.class));
		configuration.register(classType(JSR310LocalDateTimeType.class));
		configuration.register(classType(JSR310LocalDateType.class));
		configuration.register(classType(JSR310LocalTimeType.class));
//		configuration.register(classType(JSR310OffsetDateTimeType.class));
//		configuration.register(classType(JSR310OffsetTimeType.class));
		configuration.register(classType(JSR310ZonedDateTimeType.class));

		MetaDataExporter exporter = new MetaDataExporter();
		exporter.setPackageName("com.coreoz.plume.db.querydsl.generated");
		exporter.setTargetFolder(new File("src/test/java"));
		exporter.setBeanSerializer(new IdBeanSerializer());
		exporter.setColumnAnnotations(true);
		exporter.setConfiguration(configuration);

		Injector injector = Guice.createInjector(new DbQuerydslTestModule());
		injector.getInstance(TransactionManager.class).execute(connection -> {
			try {
				exporter.export(connection.getMetaData());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	static Type<?> classType(Class<?> classType) {
		try {
			return (Type<?>) classType.newInstance();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

}
