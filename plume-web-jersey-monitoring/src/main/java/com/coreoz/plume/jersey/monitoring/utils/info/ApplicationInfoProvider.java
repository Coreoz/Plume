package com.coreoz.plume.jersey.monitoring.utils.info;

import com.coreoz.plume.jersey.monitoring.configuration.JerseyMonitoringConfigurationService;
import com.coreoz.plume.jersey.monitoring.utils.info.beans.ApplicationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
@Singleton
public class ApplicationInfoProvider implements Provider<ApplicationInfo> {
    private static final String POM_FILE_NAME = "pom.xml";
    private final JerseyMonitoringConfigurationService configurationService;
    private final ApplicationInfo applicationInfo;

    @Inject
    private ApplicationInfoProvider(@Nonnull JerseyMonitoringConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.applicationInfo = this.fetchApplicationInfo();
    }

    @Override
    public ApplicationInfo get() {
        return this.applicationInfo;
    }

    /* PRIVATE */
    private ApplicationInfo fetchApplicationInfo() {
        Model model = this.readPom();

        if (model == null) {
            model = this.readMetaInfPom();
        }

        if (model == null) {
            logger.warn("Failed to read pom.xml file on root project and in META-INF folder");
            model = new Model();
        }

        Map<String, Object> additionalInformation = configurationService.getCustomInfo();
        return new ApplicationInfo(
            model.getName(),
            model.getDescription(),
            model.getVersion(),
            additionalInformation.isEmpty() ? null : additionalInformation
        );
    }

    private Model readPom() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        if ((new File(POM_FILE_NAME)).exists()) {
            try {
                return reader.read(new FileReader(POM_FILE_NAME));
            } catch (Exception e) {
                logger.error("Failed to read {}", POM_FILE_NAME, e);
                return null;
            }
        }
        return null;
    }

    private Model readMetaInfPom() {
        MavenXpp3Reader reader = new MavenXpp3Reader();

        try (ScanResult scanResult = new ClassGraph().acceptPaths("META-INF/maven").scan()) {
            ResourceList resourceList = scanResult.getResourcesWithLeafName(POM_FILE_NAME);

            if (resourceList == null || resourceList.isEmpty()) {
                throw new RuntimeException("Could not find " + POM_FILE_NAME);
            }

            return reader.read(new InputStreamReader(resourceList.get(0).open()));
        } catch (Exception e) {
            logger.error("Failed to read {} from META-INF folder", POM_FILE_NAME, e);
            return null;
        }
    }
}

