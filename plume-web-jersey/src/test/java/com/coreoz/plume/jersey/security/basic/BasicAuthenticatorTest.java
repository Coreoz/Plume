package com.coreoz.plume.jersey.security.basic;

import com.google.common.net.HttpHeaders;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthenticatorTest {
    private static final String REALM = "TestRealm";
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_USERNAME = "wrongUser";
    private static final String INVALID_PASSWORD = "wrongPassword";

    private ContainerRequestContext mockRequestContext;

    @Before
    public void setUp() {
        mockRequestContext = Mockito.mock(ContainerRequestContext.class);
    }

    // Test for requireAuthentication()
    @Test
    public void requireAuthentication__when_validCredentials__should_returnBasicUser() {
        BasicAuthenticator<String> authenticator = BasicAuthenticator.fromSingleCredentials(VALID_USERNAME, VALID_PASSWORD, REALM);
        // Mock the request context to simulate a valid header
        when(mockRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(createBasicAuthHeader(VALID_USERNAME, VALID_PASSWORD));
        String result = authenticator.requireAuthentication(mockRequestContext);

        assertThat(result).isEqualTo("Basic user");
    }

    @Test
    public void requireAuthentication__when_invalidCredentials__should_throw_forbidden_exception() {
        BasicAuthenticator<String> authenticator = BasicAuthenticator.fromSingleCredentials(VALID_USERNAME, VALID_PASSWORD, REALM);
        when(mockRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(createBasicAuthHeader(INVALID_USERNAME, INVALID_PASSWORD));

        assertThatThrownBy(() -> authenticator.requireAuthentication(mockRequestContext))
            .isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void requireAuthentication__when_missing_authorization_header__should_throw_client_exception() {
        BasicAuthenticator<String> authenticator = BasicAuthenticator.fromSingleCredentials(VALID_USERNAME, VALID_PASSWORD, REALM);
        when(mockRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThatThrownBy(() -> authenticator.requireAuthentication(mockRequestContext))
            .isInstanceOf(ClientErrorException.class);
    }

    // Test for parseBasicHeader()
    @Test
    public void parseBasicHeader__when_validHeader__should_returnCredentials() {
        String validAuthHeader = createBasicAuthHeader(VALID_USERNAME, VALID_PASSWORD);
        Credentials credentials = BasicAuthenticator.parseBasicHeader(validAuthHeader);

        assertThat(credentials).isNotNull();
        assertThat(credentials.getUsername()).isEqualTo(VALID_USERNAME);
        assertThat(credentials.getPassword()).isEqualTo(VALID_PASSWORD);
    }

    @Test
    public void parseBasicHeader__when_invalidHeader__should_returnNull() {
        String invalidAuthHeader = "InvalidHeaderFormat";
        Credentials credentials = BasicAuthenticator.parseBasicHeader(invalidAuthHeader);

        assertThat(credentials).isNull();
    }

    // Helper to create a Basic Auth header
    private static String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}
