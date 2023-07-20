package com.coreoz.plume.jersey.info.monitoring.info.beans;

import java.util.Map;

public class AppInformation {
    private final String name;
    private final String description;
    private final String version;
    private final Map<String, Object> additionalInformation;

    public AppInformation(String name, String description, String version, Map<String, Object> additionalInformation) {
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
