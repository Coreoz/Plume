package com.coreoz.plume.jersey.security.control;

import org.glassfish.hk2.api.Factory;

public class ContentControlFeatureFactory implements Factory<ContentControlFeature> {
    private final Integer maxSize;

    public ContentControlFeatureFactory(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public ContentControlFeature provide() {
        return new ContentControlFeature(maxSize);
    }

    @Override
    public void dispose(ContentControlFeature instance) {
        // unused
    }
}
