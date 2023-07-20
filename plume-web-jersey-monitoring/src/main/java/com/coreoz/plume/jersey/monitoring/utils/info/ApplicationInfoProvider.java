package com.coreoz.plume.jersey.monitoring.utils.info;

import com.coreoz.plume.jersey.monitoring.configuration.JerseyMonitoringConfigurationService;
import com.coreoz.plume.jersey.monitoring.utils.info.beans.ApplicationInfo;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.FileReader;
import java.util.Map;

@Singleton
public class ApplicationInfoProvider implements Provider<ApplicationInfo> {
    private final ApplicationInfo applicationInfo;

    @Inject
    private ApplicationInfoProvider(JerseyMonitoringConfigurationService configurationService) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml"));
            Map<String, Object> additionalInformation = configurationService.getCustomInfo();
            this.applicationInfo = new ApplicationInfo(
                model.getName(),
                model.getDescription(),
                model.getVersion(),
                additionalInformation.isEmpty() ? null : additionalInformation
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve the application information", e);
        }
    }

    @Override
    public ApplicationInfo get() {
        return this.applicationInfo;
    }
}
