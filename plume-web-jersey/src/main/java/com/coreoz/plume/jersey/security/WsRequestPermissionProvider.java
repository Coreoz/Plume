package com.coreoz.plume.jersey.security;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.Collection;

/**
 * Permet d'extraire les permissions d'un utilisateur associé à la requête HTTP courante
 */
public interface WsRequestPermissionProvider {

	/**
	 * Récupère les permissions de l'utilisateur liées à la requête courante.
	 * Si l'utilisateur n'a aucune permission, une collection vide doit être retournée.
	 */
	Collection<String> correspondingPermissions(ContainerRequestContext requestContext);

	/**
	 * Récupère des informations sur l'utilisateur pour le debug en cas d'accès non autorisé
	 */
	String userInformation(ContainerRequestContext requestContext);

}
