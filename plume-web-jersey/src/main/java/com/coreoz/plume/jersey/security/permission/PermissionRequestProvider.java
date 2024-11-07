package com.coreoz.plume.jersey.security.permission;

import java.util.Collection;

import jakarta.annotation.Nonnull;
import jakarta.ws.rs.container.ContainerRequestContext;

/**
 * Extract permissions from the user corresponding to the current HTTP request
 */
public interface PermissionRequestProvider {

	/**
	 * Fetch user permissions corresponding to the current HTTP request.
	 * If the user has no permission or if no user is attached to the HTTP request,
	 * then an empty collection must be returned.
	 */
    @Nonnull
	Collection<String> correspondingPermissions(@Nonnull ContainerRequestContext requestContext);

	/**
	 * Fetch user information. It will be used to monitor or debug unauthorized access
	 */
    @Nonnull
	String userInformation(@Nonnull ContainerRequestContext requestContext);

}
