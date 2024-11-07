package com.coreoz.plume.jersey.security;

import jakarta.annotation.Nonnull;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Provide a general way of implementing a Jersey request authentication filter
 * that will be active on a method or a class depending on the presence of an annotation.<br>
 * <br>
 * The priority will be:<br>
 * 1. If the annotation is present on the resource method, then this annotation will be used when calling the {@link AuthorizationVerifier}<br>
 * 2. Else if the annotation is present on the resource class, then this annotation will be used when calling the {@link AuthorizationVerifier}<br>
 * 3. Else the authentication filter will not be active for the whole resource
 * @param <A> The annotation type that will be looked to on the resource to enable the authentication filter
 * @see com.coreoz.plume.jersey.security.basic.BasicAuthenticator
 * @see com.coreoz.plume.jersey.security.bearer.BearerAuthenticator
 * @see com.coreoz.plume.jersey.security.permission.PermissionFeature
 */
@Slf4j
public class AuthorizationSecurityFeature<A extends Annotation> implements DynamicFeature {
    private final Class<A> annotation;
    private final AuthorizationVerifier<A> authorizationVerifier;

    /**
     * @param annotation The annotation type that will hold the authentication
     * @param authorizationVerifier The function that will verify that the request is authorized, else it should throw {@link jakarta.ws.rs.ForbiddenException} or {@link jakarta.ws.rs.ClientErrorException}
     */
    public AuthorizationSecurityFeature(@Nonnull Class<A> annotation, @Nonnull AuthorizationVerifier<A> authorizationVerifier) {
        this.annotation = annotation;
        this.authorizationVerifier = authorizationVerifier;
    }

    @Override
    public void configure(@Nonnull ResourceInfo methodResourceInfo, @Nonnull FeatureContext methodResourceContext) {
        A methodAnnotation = getAnnotation(methodResourceInfo.getResourceMethod());
        if (methodAnnotation != null) {
            methodResourceContext.register(new AuthorizationFilter(methodAnnotation));
        } else {
            A classAnnotation = getAnnotation(methodResourceInfo.getResourceClass());
            if (classAnnotation != null) {
                methodResourceContext.register(new AuthorizationFilter(classAnnotation));
            }
        }
    }

    private A getAnnotation(AnnotatedElement annotatedElement) {
        return annotatedElement.getAnnotation(annotation);
    }

    private class AuthorizationFilter implements ContainerRequestFilter {
        private final A authorizationAnnotation;

        public AuthorizationFilter(A authorizationAnnotation) {
            this.authorizationAnnotation = authorizationAnnotation;
        }

        @Override
        public void filter(ContainerRequestContext requestContext) {
            authorizationVerifier.verifyAuthentication(authorizationAnnotation, requestContext);
        }
    }
}
