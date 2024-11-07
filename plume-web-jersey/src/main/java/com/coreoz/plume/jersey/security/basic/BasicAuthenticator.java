package com.coreoz.plume.jersey.security.basic;

import com.coreoz.plume.jersey.security.AuthorizationSecurityFeature;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;

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
	public BasicAuthenticator(@Nonnull Function<Credentials, U> authenticator, @Nonnull String realm) {
		this.authenticator = authenticator;
		this.realm = realm;
	}

	/**
	 * Create a new {@link BasicAuthenticator} for a unique credentials.
	 * This should be used to protect a non-strategic resource since users
	 * will all share the same username and password.
	 */
    @Nonnull
	public static BasicAuthenticator<String> fromSingleCredentials(@Nonnull String singleUsername,
                                                                   @Nonnull String password, @Nonnull String realm) {
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
     * Provide a {@link AuthorizationSecurityFeature} from the bearer basic that can be used in Jersey
     * to provide authentication on annotated resources.
     * @param basicAnnotation The annotation that will be used to identify resources that must be authorized. For example {@link BasicRestricted} can be used if it is not already used in the project for another authorization system
     * @return The corresponding {@link AuthorizationSecurityFeature}
     * @param <A> The annotation type used to identify required basic authenticated resources
     */
    @Nonnull
    public <A extends Annotation> AuthorizationSecurityFeature<A> toAuthorizationFeature(@Nonnull Class<A> basicAnnotation) {
        return new AuthorizationSecurityFeature<>(
            basicAnnotation,
            (authorizationAnnotation, requestContext) -> requireAuthentication(requestContext)
        );
    }

	/**
	 * Check that users accessing the resource are well authenticated.
	 * A {@link ClientErrorException} will be raised if a user is not authenticated.
	 * This error is usually interpreted by Jersey,
	 * so an appropriate {@link Response} will be sent to the user.
	 *
	 * @return The authenticated user object
	 */
    @Nonnull
	public U requireAuthentication(@Nonnull ContainerRequestContext requestContext) {
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
    @Nonnull
	public<T> T authenticated(@Nonnull ContainerRequestContext requestContext,
                              @Nonnull Function<U, T> authenticatedRequestHandler) {
		return authenticatedRequestHandler.apply(requireAuthentication(requestContext));
	}

	// utils API

    @Nullable
	public static Credentials parseBasicHeader(@Nullable String authorizationHeader) {
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
