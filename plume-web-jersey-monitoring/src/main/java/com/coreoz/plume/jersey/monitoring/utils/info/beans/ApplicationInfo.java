package com.coreoz.plume.jersey.monitoring.utils.info.beans;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Map;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ApplicationInfo {
    String name;
    String description;
    String version;
    Map<String, Object> additionalInformation;
}
