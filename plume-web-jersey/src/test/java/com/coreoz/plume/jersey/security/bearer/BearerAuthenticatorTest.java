package com.coreoz.plume.jersey.security.bearer;

import com.google.common.net.HttpHeaders;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BearerAuthenticatorTest {

    private static final String VALID_BEARER_TOKEN = "validBearerToken";
    private static final String VALID_AUTH_HEADER = "Bearer " + VALID_BEARER_TOKEN;
    private static final String INVALID_BEARER_TOKEN = "invalidBearerToken";
    private BearerAuthenticator authenticator;

    @Before
    public void setUp() {
        authenticator = new BearerAuthenticator(VALID_BEARER_TOKEN);
    }

    // Test for parseBearerHeader() method

    @Test
    public void parseBearerHeader__when_authorizationHeader_is_null__should_return_null() {
        String result = BearerAuthenticator.parseBearerHeader(null);
        assertThat(result).isNull();
    }

    @Test
    public void parseBearerHeader__when_authorizationHeader_does_not_start_with_bearer__should_return_null() {
        String result = BearerAuthenticator.parseBearerHeader("Basic abc123");
        assertThat(result).isNull();
    }

    @Test
    public void parseBearerHeader__when_authorizationHeader_starts_with_bearer__should_return_token() {
        String result = BearerAuthenticator.parseBearerHeader(VALID_AUTH_HEADER);
        assertThat(result).isEqualTo(VALID_AUTH_HEADER);
    }

    // Test for verifyAuthentication() method

    @Test
    public void verifyAuthentication__when_authorizationHeader_is_valid__should_not_throw_exception() {
        // Mock the request context to simulate a valid header
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(VALID_AUTH_HEADER);

        // Verify that no exception is thrown
        assertThatCode(() -> authenticator.verifyAuthentication(requestContext))
            .doesNotThrowAnyException();
    }

    @Test
    public void verifyAuthentication__when_authorizationHeader_is_null__should_throw_forbiddenException() {
        // Mock the request context with a null authorization header
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Verify that a ForbiddenException is thrown
        assertThatThrownBy(() -> authenticator.verifyAuthentication(requestContext))
            .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void verifyAuthentication__when_authorizationHeader_does_not_start_with_bearer__should_throw_forbiddenException() {
        // Mock the request context with an invalid authorization header
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Basic abc123");

        // Verify that a ForbiddenException is thrown
        assertThatThrownBy(() -> authenticator.verifyAuthentication(requestContext))
            .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void verifyAuthentication__when_authorizationHeader_is_invalid__should_throw_forbiddenException() {
        // Mock the request context with an invalid bearer token
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + INVALID_BEARER_TOKEN);

        // Verify that a ForbiddenException is thrown
        assertThatThrownBy(() -> authenticator.verifyAuthentication(requestContext))
            .isInstanceOf(ForbiddenException.class);
    }
}
