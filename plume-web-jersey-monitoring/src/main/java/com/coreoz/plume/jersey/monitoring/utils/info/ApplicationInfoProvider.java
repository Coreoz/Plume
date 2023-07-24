package com.coreoz.plume.jersey.monitoring.utils.info;

import com.coreoz.plume.jersey.monitoring.configuration.JerseyMonitoringConfigurationService;
import com.coreoz.plume.jersey.monitoring.utils.info.beans.ApplicationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
@Singleton
public class ApplicationInfoProvider implements Provider<ApplicationInfo> {
    private static final String POM_FILE_NAME = "pom.xml";
    private final JerseyMonitoringConfigurationService configurationService;
    private final ApplicationInfo applicationInfo;

    @Inject
    private ApplicationInfoProvider(JerseyMonitoringConfigurationService configurationService) {
        this.configurationService = configurationService;
        ApplicationInfo tmpApplicationInfo = new ApplicationInfo();
        try {
            tmpApplicationInfo = this.fetchApplicationInfo();
        } catch (Exception e) {
            log.error("Failed to retrieve application info", e);
        }

        this.applicationInfo = tmpApplicationInfo;
    }

    @Override
    public ApplicationInfo get() {
        return this.applicationInfo;
    }

    /* PRIVATE */
    private ApplicationInfo fetchApplicationInfo() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;

        if ((new File(POM_FILE_NAME)).exists()) {
            model = reader.read(new FileReader(POM_FILE_NAME));
        } else {
            try (ScanResult scanResult = new ClassGraph().acceptPaths("META-INF/maven").scan()) {
                ResourceList resourceList = scanResult.getResourcesWithLeafName(POM_FILE_NAME);

                if(resourceList == null || resourceList.isEmpty()) {
                    throw new RuntimeException("Could not find " + POM_FILE_NAME);
                }

                model = reader.read(new InputStreamReader(resourceList.get(0).open()));
            } catch (Exception e) {
                log.error("Failed to read {}", POM_FILE_NAME, e);
                return new ApplicationInfo();
            }
        }

        Map<String, Object> additionalInformation = configurationService.getCustomInfo();
        return new ApplicationInfo(
            model.getName(),
            model.getDescription(),
            model.getVersion(),
            additionalInformation.isEmpty() ? null : additionalInformation
        );
    }
}

