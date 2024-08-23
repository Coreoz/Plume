package com.coreoz.plume.db.transaction;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

/**
 * Expose a {@link HikariDataSource} Object through dependency injection.
 */
@Singleton
public class HikariDataSourceProvider implements Provider<HikariDataSource> {

    private final HikariDataSource dataSource;

    @Inject
    public HikariDataSourceProvider(Config config) {
        this.dataSource = HikariDataSources.fromConfig(config, "db.hikari");
    }

    @Override
    public HikariDataSource get() {
        return dataSource;
    }

}
