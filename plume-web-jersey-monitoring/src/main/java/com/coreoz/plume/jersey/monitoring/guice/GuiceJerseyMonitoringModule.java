package com.coreoz.plume.jersey.monitoring.guice;

import com.coreoz.plume.jersey.monitoring.utils.info.ApplicationInfoProvider;
import com.coreoz.plume.jersey.monitoring.utils.info.beans.ApplicationInfo;
import com.google.inject.AbstractModule;

public class GuiceJerseyMonitoringModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ApplicationInfo.class).toProvider(ApplicationInfoProvider.class);
    }
}
