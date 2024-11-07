package com.coreoz.plume.jersey.security.bearer;

import com.coreoz.plume.jersey.security.AuthorizationSecurityFeature;
import com.google.common.net.HttpHeaders;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;

/**
 * Provide a generic resource authenticator for API-key based resource authorization
 */
@Slf4j
public class BearerAuthenticator {
    private static final String BEARER_PREFIX = "Bearer ";

    private final String authenticationSecretHeader;

    public BearerAuthenticator(@Nonnull String bearerToken) {
        this.authenticationSecretHeader = BEARER_PREFIX + bearerToken;
    }

    /**
     * Provide a {@link AuthorizationSecurityFeature} from the bearer authenticator that can be used in Jersey
     * to provide authentication on annotated resources.
     * @param bearerAnnotation The annotation that will be used to identify resources that must be authorized. For example {@link BearerRestricted} can be used if it is not already used in the project for another authorization system
     * @return The corresponding {@link AuthorizationSecurityFeature}
     * @param <A> The annotation type used to identify required bearer authenticated resources
     */
    @Nonnull
    public <A extends Annotation> AuthorizationSecurityFeature<A> toAuthorizationFeature(@Nonnull Class<A> bearerAnnotation) {
        return new AuthorizationSecurityFeature<>(
            bearerAnnotation,
            (authorizationAnnotation, requestContext) -> verifyAuthentication(requestContext)
        );
    }

    public void verifyAuthentication(@Nonnull ContainerRequestContext requestContext) {
        String bearer = parseBearerHeader(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));

        if (bearer == null || !MessageDigest.isEqual(authenticationSecretHeader.getBytes(), bearer.getBytes())) {
            logger.warn("Invalid bearer header: {}", bearer);
            throw new ForbiddenException();
        }
    }

    @Nullable
    public static String parseBearerHeader(@Nullable String authorizationHeader) {
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
