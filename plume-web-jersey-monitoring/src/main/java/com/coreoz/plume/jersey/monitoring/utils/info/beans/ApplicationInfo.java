package com.coreoz.plume.jersey.monitoring.utils.info.beans;

import lombok.Value;

import java.util.Map;

@Value
public class ApplicationInfo {
    String name;
    String description;
    String version;
    Map<String, Object> additionalInformation;
}
