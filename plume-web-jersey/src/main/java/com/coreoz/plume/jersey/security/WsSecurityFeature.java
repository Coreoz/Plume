package com.coreoz.plume.jersey.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.model.AnnotatedMethod;

/**
 * Decrit la fonctionnalité Jersey qui va permettre de vérifier les droits des utilisateurs
 * pour accéder à une ressource/web-service par requête HTTP
 */
@Singleton
public class WsSecurityFeature implements DynamicFeature {

	private final WsAuthenticator authenticator;

	@Inject
	public WsSecurityFeature(WsAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());

		// si l'annotation RestrictTo est présente sur la méthode de WS
		// alors c'est celle-là qui sera utilisée
		RestrictTo restrictTo = am.getAnnotation(RestrictTo.class);
		if (restrictTo != null) {
			context.register(new WsSecurityRequestFilter(restrictTo.value(), authenticator));
			return;
		}

		// si l'annotation RestrictTo n'est pas présente sur la méthode de WS
		// mais qu'elle est présente sur la classe
		// alors la valeur de l'annotation de la classe sera utilisée sur la méthode
		restrictTo = resourceInfo.getResourceClass().getAnnotation(RestrictTo.class);
		if (restrictTo != null) {
			context.register(new WsSecurityRequestFilter(restrictTo.value(), authenticator));
			return;
		}
	}

	private static class WsSecurityRequestFilter implements ContainerRequestFilter {

		private final String resourcePermission;
		private final WsAuthenticator authenticator;

		public WsSecurityRequestFilter(String resourcePermission, WsAuthenticator authenticator) {
			this.resourcePermission = resourcePermission;
			this.authenticator = authenticator;
		}

		@Override
		public void filter(ContainerRequestContext requestContext) throws IOException {
			if(!authenticator.authorize(requestContext, resourcePermission)) {
				throw new ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
			}
		}

	}

}
