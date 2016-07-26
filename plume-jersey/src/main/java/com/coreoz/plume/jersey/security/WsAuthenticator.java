package com.coreoz.plume.jersey.security;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Permet de vérifier les droits d'accès d'un utilisateur à un web-service
 */
public interface WsAuthenticator {

	boolean authorize(ContainerRequestContext requestContext, String permissionRequiredToAccessResource);

}
