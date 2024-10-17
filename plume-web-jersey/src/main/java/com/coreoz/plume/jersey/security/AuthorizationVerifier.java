package com.coreoz.plume.jersey.security;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

/**
 * The function that will verify that an incoming request is authorized or not<br>
 * <br>
 * This function should be used within the {@link AuthorizationSecurityFeature}.
 * @param <A> The annotation type used to identify that the verifier should be applied on a resource method/class
 */
@FunctionalInterface
public interface AuthorizationVerifier<A extends Annotation> {
    /**
     * Verify that an incoming request is authorized
     *
     * @param authorizationAnnotation The annotation type used to identify that the verifier should be applied on a resource method/class
     * @param requestContext The request context that should be used to verify the authorization
     * @throws ForbiddenException If the user cannot access the resource
     * @throws ClientErrorException If there is another error about the request authorization
     */
    void verifyAuthentication(@Nonnull A authorizationAnnotation, @Nonnull ContainerRequestContext requestContext) throws ForbiddenException, ClientErrorException;
}
