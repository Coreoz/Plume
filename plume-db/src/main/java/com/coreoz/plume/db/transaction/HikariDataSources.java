package com.coreoz.plume.db.transaction;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Nonnull;

/**
 * Provide a way to load a HikariCP {@link DataSource} from a {@link Config} Object.
 */
public class HikariDataSources {

    @Nonnull
	public static HikariDataSource fromConfig(@Nonnull Config config, @Nonnull String prefix) {
		return new HikariDataSource(createHikariConfig(config, prefix));
	}

    @Nonnull
    public static HikariConfig createHikariConfig(@Nonnull Config config, @Nonnull String prefix) {
        return new HikariConfig(mapToProperties(readConfig(config, prefix)));
    }

    @Nonnull
	private static Properties mapToProperties(@Nonnull Map<String, String> mapProperties) {
		Properties properties = new Properties();
		properties.putAll(mapProperties);
		return properties;
	}

    @Nonnull
	private static Map<String, String> readConfig(@Nonnull Config config, @Nonnull String prefix) {
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
