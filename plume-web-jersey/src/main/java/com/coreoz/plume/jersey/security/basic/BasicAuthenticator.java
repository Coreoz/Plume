package com.coreoz.plume.jersey.security.basic;

import com.coreoz.plume.jersey.security.AuthorizationVerifier;
import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.Function;

/**
 * Enable to easily manage HTTP Basic Authentication with Jersey.
 *
 * @param <U> The user type handled by the authentication
 */
@Slf4j
public class BasicAuthenticator<U> {
	private static final String BASIC_PREFIX = "Basic ";

	private final Function<Credentials, U> authenticator;
	private final String realm;

	// constructors

	/**
	 * @param authenticator The function that will verify the identity of a user with its
	 * credentials and returned the corresponding User object only if the user has successfuly
	 * been authenticated. If the user identity cannot be established, null must be returned.
	 * @param realm The title of the browser pop-up that will ask the user to enter his credentials,
	 * the realm is mandatory,
	 * see <a href="https://tools.ietf.org/html/rfc2617#section-2">RFC 2617</a>.
	 */
	public BasicAuthenticator(Function<Credentials, U> authenticator, String realm) {
		this.authenticator = authenticator;
		this.realm = realm;
	}

	/**
	 * Create a new {@link BasicAuthenticator} for a unique credentials.
	 * This should be used to protect a non-strategic resource since users
	 * will all share the same username and password.
	 */
	public static BasicAuthenticator<String> fromSingleCredentials(String singleUsername,
			String password, String realm) {
		return new BasicAuthenticator<>(
			credentials -> MessageDigest.isEqual(singleUsername.getBytes(), credentials.getUsername().getBytes())
                && MessageDigest.isEqual(password.getBytes(),credentials.getPassword().getBytes()) ?
					"Basic user"
					: null,
			realm
		);
	}

	// API

    /**
     * Provide an {@link AuthorizationVerifier} from the basic authenticator to provide annotation based request authorization using {@link com.coreoz.plume.jersey.security.AuthorizationSecurityFeature}
     * @param annotation The annotation that will be used to identify resources that must be authorized. For example {@link BasicRestricted} can be used if it is not already used in the project for another authorization system
     * @return The basic authenticator corresponding {@link AuthorizationVerifier}
     * @param <A> The annotation type
     */
    public <A extends Annotation> AuthorizationVerifier<A> toAuthorizationVerifier(A annotation) {
        return (authorizationAnnotation, requestContext) -> requireAuthentication(requestContext);
    }

	/**
	 * Check that users accessing the resource are well authenticated.
	 * A {@link ClientErrorException} will be raised if a user is not authenticated.
	 * This error is usually interpreted by Jersey,
	 * so an appropriate {@link Response} will be sent to the user.
	 *
	 * @return The authenticated user object
	 */
	public U requireAuthentication(ContainerRequestContext requestContext) {
		Credentials credentials = parseBasicHeader(
			requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)
		);

		if(credentials == null) {
			throw new ClientErrorException(
				Response
					.status(Status.UNAUTHORIZED)
					.header(
						HttpHeaders.WWW_AUTHENTICATE,
						// currently Firefox does not support UTF-8 encoding for Basic Auth
						// see https://bugzilla.mozilla.org/show_bug.cgi?id=41489
						"Basic realm=\""+realm+"\", charset=\"UTF-8\""
					)
					.build()
			);
		}

		U authenticatedUser = authenticator.apply(credentials);

		if(authenticatedUser == null) {
			logger.warn("Wrong Basic credentials, received: {}:{}", credentials.getUsername(), credentials.getPassword());
			throw new ForbiddenException();
		}

		return authenticatedUser;
	}

	/**
	 * Ensure that the authenticatedRequestHandler is called only if the
	 * user accessing the resource is well authenticated.
	 */
	public<T> T authenticated(ContainerRequestContext requestContext,
			Function<U, T> authenticatedRequestHandler) {
		return authenticatedRequestHandler.apply(requireAuthentication(requestContext));
	}

	// utils API

	public static Credentials parseBasicHeader(String authorizationHeader) {
		if(authorizationHeader == null) {
			logger.debug("Missing Authorization header");
			return null;
		}

		if(!authorizationHeader.startsWith(BASIC_PREFIX)) {
			logger.debug("Basic Authorization header must starts with '{}'", BASIC_PREFIX);
			return null;
		}

		String[] decodedCredentials = new String(
			Base64.getDecoder().decode(authorizationHeader.substring(BASIC_PREFIX.length())),
			StandardCharsets.UTF_8
		).split(":");

		if(decodedCredentials.length != 2) {
			logger.debug(
				"Basic Authorization header must use the form: {}base64(username:password)",
				BASIC_PREFIX
			);
			return null;
		}

		return new Credentials(decodedCredentials[0], decodedCredentials[1]);
	}
}
