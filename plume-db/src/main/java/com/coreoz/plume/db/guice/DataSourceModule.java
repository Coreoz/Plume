package com.coreoz.plume.db.guice;

import javax.sql.DataSource;

import com.coreoz.plume.db.transaction.HikariDataSourceProvider;
import com.google.inject.AbstractModule;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataSource.class).toProvider(HikariDataSourceProvider.class);
        bind(HikariDataSource.class).toProvider(HikariDataSourceProvider.class);
    }
}
