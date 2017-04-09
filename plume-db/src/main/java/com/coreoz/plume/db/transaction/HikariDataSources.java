package com.coreoz.plume.db.transaction;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Provide a way to load a HikariCP {@link DataSource} from a {@link Config} Object.
 */
public class HikariDataSources {

	public static DataSource fromConfig(Config config, String prefix) {
		return initializeFromProperties(readConfig(config, prefix));
	}

	private static HikariDataSource initializeFromProperties(Map<String, String> properties) {
		return new HikariDataSource(new HikariConfig(mapToProperties(properties)));
	}

	private static Properties mapToProperties(Map<String, String> mapProperties) {
		Properties properties = new Properties();
		properties.putAll(mapProperties);
		return properties;
	}

	private static Map<String, String> readConfig(Config config, String prefix) {
		return config
			.getObject(prefix)
			.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				value -> value.getValue().unwrapped().toString()
			));
	}

}
