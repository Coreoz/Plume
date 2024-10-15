package com.coreoz.plume.jersey.security.size;

import org.glassfish.hk2.api.Factory;

public class ContentSizeLimitFeatureFactory implements Factory<ContentSizeLimitFeature> {
    private final Integer maxSize;

    public ContentSizeLimitFeatureFactory(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public ContentSizeLimitFeature provide() {
        return new ContentSizeLimitFeature(maxSize);
    }

    @Override
    public void dispose(ContentSizeLimitFeature instance) {
        // unused
    }
}
