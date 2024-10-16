package com.coreoz.plume.jersey.security.bearer;

import com.coreoz.plume.jersey.security.AuthorizationVerifier;
import com.coreoz.plume.jersey.security.basic.BasicRestricted;
import com.google.common.net.HttpHeaders;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;

/**
 * Provide a generic resource authenticator for API-key based resource authorization
 */
@Slf4j
public class BearerAuthenticator {
    private static final String BEARER_PREFIX = "Bearer ";

    private final String authenticationSecretHeader;

    public BearerAuthenticator(String bearerToken) {
        this.authenticationSecretHeader = BEARER_PREFIX + bearerToken;
    }

    /**
     * Provide an {@link AuthorizationVerifier} from the bearer authenticator to provide annotation based request authorization using {@link com.coreoz.plume.jersey.security.AuthorizationSecurityFeature}
     * @param annotation The annotation that will be used to identify resources that must be authorized. For example {@link BasicRestricted} can be used if it is not already used in the project for another authorization system
     * @return The basic authenticator corresponding {@link AuthorizationVerifier}
     * @param <A> The annotation type
     */
    public <A extends Annotation> AuthorizationVerifier<A> toAuthorizationVerifier(A annotation) {
        return (authorizationAnnotation, requestContext) -> verifyAuthentication(requestContext);
    }

    public void verifyAuthentication(@NotNull ContainerRequestContext requestContext) {
        String bearer = parseBearerHeader(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));

        if (bearer == null || !MessageDigest.isEqual(authenticationSecretHeader.getBytes(), bearer.getBytes())) {
            throw new ForbiddenException("Invalid bearer header: " + bearer);
        }
    }

    public static String parseBearerHeader(String authorizationHeader) {
        if(authorizationHeader == null) {
            logger.debug("Missing Authorization header");
            return null;
        }

        if(!authorizationHeader.startsWith(BEARER_PREFIX)) {
            logger.debug("Bearer Authorization header must starts with '{}'", BEARER_PREFIX);
            return null;
        }

        return authorizationHeader;
    }
}
