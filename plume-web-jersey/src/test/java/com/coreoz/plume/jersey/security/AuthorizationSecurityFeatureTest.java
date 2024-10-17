package com.coreoz.plume.jersey.security;

import com.google.common.net.HttpHeaders;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationSecurityFeatureTest extends JerseyTest {
    private final AtomicInteger verifierCounter = new AtomicInteger(0);

    @Path("/test-resource")
    public static class TestResourceNotAnnotated {
        @GET
        public String testMethod() {
            return "Success";
        }
    }

    @Path("/test-resource-method")
    public static class TestResourceMethod {
        @GET
        @MyTestAnnotation
        public String testMethod() {
            return "Success";
        }
    }

    @MyTestAnnotation
    @Path("/test-resource-class")
    public static class TestResourceClass {
        @GET
        public String testMethod() {
            return "Success";
        }
    }

    // Define a custom annotation for testing purposes
    @Retention(RetentionPolicy.RUNTIME)
    @Target({TYPE, METHOD})
    public @interface MyTestAnnotation {}

    @Override
    protected Application configure() {
        AuthorizationVerifier<MyTestAnnotation> mockVerifier = (authorizationAnnotation, requestContext) -> {
            verifierCounter.incrementAndGet();
            if (requestContext.getHeaderString(HttpHeaders.AUTHORIZATION) == null) {
                throw new ForbiddenException();
            }
        };

        // Register the feature and the test resource class
        return new ResourceConfig()
            .register(new AuthorizationSecurityFeature<>(MyTestAnnotation.class, mockVerifier))
            .register(TestResourceNotAnnotated.class)
            .register(TestResourceMethod.class)
            .register(TestResourceClass.class);
    }

    @Test
    public void shouldThrowForbiddenIfAuthorizationFails() {
        // Act: Call the test resource and expect a forbidden status
        int baseCount = verifierCounter.get();
        WebTarget target = target("/test-resource-method");
        Response response = target.request().get();

        // Assert: The response should be 403 Forbidden
        assertThat(response.getStatus()).isEqualTo(403);

        // Verify that the AuthorizationVerifier was called with the correct parameters
        assertThat(verifierCounter.get() - baseCount).isOne();
    }

    @Test
    public void shouldApplyFilterForMethodAnnotation() {
        // Act: Call the test resource
        int baseCount = verifierCounter.get();
        WebTarget target = target("/test-resource-method");
        Response response = target.request().header(HttpHeaders.AUTHORIZATION, "valid auth").get();

        // Assert: The response should be successful
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("Success");

        // Verify that the AuthorizationVerifier was called with the correct annotation
        assertThat(verifierCounter.get() - baseCount).isOne();
    }

    @Test
    public void shouldApplyFilterForClassAnnotation() {
        // Act: Call the test resource
        int baseCount = verifierCounter.get();
        WebTarget target = target("/test-resource-class");
        Response response = target.request().header(HttpHeaders.AUTHORIZATION, "valid auth").get();

        // Assert: The response should be successful
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("Success");

        // Verify that the AuthorizationVerifier was called with the correct annotation
        assertThat(verifierCounter.get() - baseCount).isOne();
    }

    @Test
    public void shouldNotApplyFilterWhenNotAnnotated() {
        // Act: Call the test resource
        int baseCount = verifierCounter.get();
        WebTarget target = target("/test-resource");
        Response response = target.request().get();

        // Assert: The response should be successful
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("Success");

        // Verify that the AuthorizationVerifier was not called
        assertThat(verifierCounter.get() - baseCount).isZero();
    }
}

