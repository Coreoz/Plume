package com.coreoz.plume.jersey.monitoring.utils.info.beans;

import java.util.Map;

public class ApplicationInfo {
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> additionalInformation;

    public ApplicationInfo(String name, String description, String version, Map<String, Object> additionalInformation) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.additionalInformation = additionalInformation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }
}
