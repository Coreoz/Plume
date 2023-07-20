package com.coreoz.plume.jersey.info.monitoring.info;

import com.coreoz.plume.jersey.info.configuration.JerseyInfoConfigurationService;
import com.coreoz.plume.jersey.info.monitoring.info.beans.AppInformation;
import com.coreoz.plume.jersey.info.json.JerseyInfoObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileReader;
import java.util.Map;

@Singleton
public class InfoService {
    private final JerseyInfoConfigurationService configurationService;
    private final ObjectMapper objectMapper = JerseyInfoObjectMapper.get();

    @Inject
    private InfoService(JerseyInfoConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public String getAppInfo() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader("pom.xml"));
            Map<String, Object> additionalInformation = this.configurationService.getCustomInfo();
            return this.objectMapper.writeValueAsString(
                new AppInformation(
                    model.getName(),
                    model.getDescription(),
                    model.getVersion(),
                    additionalInformation.isEmpty() ? null : additionalInformation
                )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve the application information", e);
        }
    }
}
