package com.coreoz.plume.jersey.control;

import org.junit.Test;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Invocation.Builder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import static org.junit.Assert.assertEquals;

import com.coreoz.plume.jersey.security.control.ContentControlFeature;
import com.coreoz.plume.jersey.security.control.ContentControlFeatureFactory;

public class ContentSizeLimitTest  extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(TestContentSizeResource.class);
        config.register(ContentControlFeature.class);
        return config;
    }

    @Test
    public void checkContentSize_withBody_whenWithinDefaultLimit_shouldReturn200() {
        byte[] data = "12345".getBytes();
        Response response = target("/test/upload-default").request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenBeyondDefaultLimit_shouldReturn413() {
        // Generate a byte array of ContentControlFeature.DEFAULT_MAX_SIZE + 1
        byte[] data = new byte[ContentControlFeature.DEFAULT_MAX_SIZE + 1];
        Builder request = target("/test/upload-default").request();
        Entity<byte[]> entity = Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM);
        assertEquals(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode(), request.post(entity).getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenContentLengthIsWrong_shouldReturn411() {
        // Generate a byte array of ContentControlFeature.DEFAULT_MAX_SIZE + 1
        Builder request = target("/test/upload-default").request();
        request.header(HttpHeaders.CONTENT_LENGTH, null);
        assertEquals(Response.Status.LENGTH_REQUIRED.getStatusCode(), request.post(null).getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenWithinCustomLimit_shouldReturn200() {
        byte[] data = "12345".getBytes();
        Response response = target("/test/upload-custom").request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenBeyondCustomLimit_shouldReturn413() {
        // Generate a byte array of CUSTOM_MAX_SIZE + 1
        byte[] data = new byte[TestContentSizeResource.CUSTOM_MAX_SIZE + 1];
        Builder request = target("/test/upload-custom").request();
        Entity<byte[]> entity = Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM);
        assertEquals(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode(), request.post(entity).getStatus());
    }

    @Test
    public void checkMaxSize_whenCustomControlFeature_shouldSuccess() {
        // Custom max size
        Integer customMaxSize = 300;
        ContentControlFeatureFactory contentControlFeatureFactory = new ContentControlFeatureFactory(customMaxSize);
        ContentControlFeature contentControlFeature = contentControlFeatureFactory.provide();
        assertEquals(customMaxSize, contentControlFeature.getContentSizeLimit());
    }
}
