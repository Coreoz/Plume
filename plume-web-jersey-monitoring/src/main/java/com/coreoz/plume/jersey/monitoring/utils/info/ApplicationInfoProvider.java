package com.coreoz.plume.jersey.monitoring.utils.info;

import com.coreoz.plume.jersey.monitoring.configuration.JerseyMonitoringConfigurationService;
import com.coreoz.plume.jersey.monitoring.utils.info.beans.ApplicationInfo;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

@Singleton
public class ApplicationInfoProvider implements Provider<ApplicationInfo> {
    private final ApplicationInfo applicationInfo;

    @Inject
    private ApplicationInfoProvider(JerseyMonitoringConfigurationService configurationService) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        Map<String, Object> additionalInformation = configurationService.getCustomInfo();
        this.applicationInfo = new ApplicationInfo(
            model.getName(),
            model.getDescription(),
            model.getVersion(),
            additionalInformation.isEmpty() ? null : additionalInformation
        );
    }

    @Override
    public ApplicationInfo get() {
        return this.applicationInfo;
    }
}
