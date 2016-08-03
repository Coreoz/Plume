package com.coreoz.plume.jersey.security;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une implémentation de {@link WsAuthenticator} avec le système de permission
 */
@Singleton
public class WsPermissionAuthenticator implements WsAuthenticator {

	private static final Logger logger = LoggerFactory.getLogger(WsPermissionAuthenticator.class);

	private final WsRequestPermissionProvider requestPermissionProvider;

	@Inject
	public WsPermissionAuthenticator(WsRequestPermissionProvider requestPermissionProvider) {
		this.requestPermissionProvider = requestPermissionProvider;
	}

	@Override
	public boolean authorize(ContainerRequestContext requestContext, String permissionRequiredToAccessResource) {
		Collection<String> userPermissions = requestPermissionProvider.correspondingPermissions(requestContext);
		boolean isAuthorized = userPermissions.contains(permissionRequiredToAccessResource);

		if(!isAuthorized) {
			logger.warn(
				"Accès non autorisé à {} par {}, permission '{}' requise non trouvée parmis {}",
				requestContext.getUriInfo().getAbsolutePath(),
				requestPermissionProvider.userInformation(requestContext),
				permissionRequiredToAccessResource,
				userPermissions
			);
		}

		return isAuthorized;
	}

}
