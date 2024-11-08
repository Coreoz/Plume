package com.coreoz.plume.jersey.security.size;

import com.coreoz.plume.jersey.errors.WsError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

public class ContentSizeLimitTest  extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig(TestContentSizeResource.class);
        config.register(ContentSizeLimitFeature.class);
        return config;
    }

    @Test
    public void checkContentSize_withBody_whenWithinDefaultLimit_shouldReturn200() {
        byte[] data = "12345".getBytes();
        Response response = target("/test/upload-default").request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
        Assertions.assertThat(Response.Status.OK.getStatusCode()).isEqualTo(response.getStatus());
    }

    @SneakyThrows
    @Test
    public void checkContentSize_withBody_whenBeyondDefaultLimit_shouldReturn413() {
        // Generate a byte array of ContentControlFeature.DEFAULT_MAX_SIZE + 1
        byte[] data = new byte[ContentSizeLimitFeature.DEFAULT_MAX_SIZE + 1];
        Builder request = target("/test/upload-default").request();
        Entity<byte[]> entity = Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM);
        Response response = request.post(entity);
        Assertions.assertThat(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode()).isEqualTo(response.getStatus());
        // make sure that a valid json error response is returned
        Assertions.assertThat(MediaType.APPLICATION_JSON_TYPE).isEqualTo(response.getMediaType());
        JsonNode responseBody = new ObjectMapper().readTree(response.readEntity(String.class));
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.get("errorCode").asText()).isEqualTo(WsError.CONTENT_SIZE_LIMIT_EXCEEDED.name());
    }

    @Test
    public void checkContentSize_withBody_whenContentLengthIsWrong_shouldReturn413() {
        // Generate a byte array of ContentControlFeature.DEFAULT_MAX_SIZE + 1
        byte[] data = new byte[ContentSizeLimitFeature.DEFAULT_MAX_SIZE + 1];
        Builder request = target("/test/upload-default").request();
        request.header(HttpHeaders.CONTENT_LENGTH, null);
        Assertions.assertThat(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode()).isEqualTo(request.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM)).getStatus());
    }

    @Test
    public void checkContentSize_withoutBody_whenDefaultLimit_shouldReturn200() {
        Builder request = target("/test/upload-default").request();
        Assertions.assertThat(Response.Status.OK.getStatusCode()).isEqualTo(request.get().getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenWithinCustomLimit_shouldReturn200() {
        byte[] data = "12345".getBytes();
        Response response = target("/test/upload-custom").request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
        Assertions.assertThat(Response.Status.OK.getStatusCode()).isEqualTo(response.getStatus());
    }

    @Test
    public void checkContentSize_withBody_whenBeyondCustomLimit_shouldReturn413() {
        // Generate a byte array of CUSTOM_MAX_SIZE + 1
        byte[] data = new byte[TestContentSizeResource.CUSTOM_MAX_SIZE + 1];
        Builder request = target("/test/upload-custom").request();
        Entity<byte[]> entity = Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM);
        Assertions.assertThat(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode()).isEqualTo(request.post(entity).getStatus());
    }

    @Test
    public void checkContentSize_withoutBody_whenCustomLimit_shouldReturn200() {
        Builder request = target("/test/upload-custom").request();
        Assertions.assertThat(Response.Status.OK.getStatusCode()).isEqualTo(request.get().getStatus());
    }

    @Test
    public void checkMaxSize_whenCustomControlFeature_shouldSuccess() {
        // Custom max size
        Integer customMaxSize = 300;
        ContentSizeLimitFeatureFactory contentControlFeatureFactory = new ContentSizeLimitFeatureFactory(customMaxSize);
        ContentSizeLimitFeature contentSizeLimitFeature = contentControlFeatureFactory.provide();
        Assertions.assertThat(customMaxSize).isEqualTo(contentSizeLimitFeature.getContentSizeLimit());
    }
}
