package com.coreoz.plume.jersey.security;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Function;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.internal.LocalizationMessages;

/**
 * Decrit la fonctionnalité Jersey qui va permettre de vérifier les droits des utilisateurs
 * pour accéder à une ressource/web-service par requête HTTP
 * @param <A> The annotation type from which a permission will be fetched 
 */
public class WsSecurityFeature<A extends Annotation> implements DynamicFeature {

	private final WsAuthenticator authenticator;
	private final Class<A> permissionAnnotationType;
	private final Function<A, String> permissionAnnotationExtractor;
	
	public WsSecurityFeature(WsAuthenticator authenticator, Class<A> permissionAnnotationType,
			Function<A, String> permissionAnnotationExtractor) {
		this.authenticator = authenticator;
		this.permissionAnnotationType = permissionAnnotationType;
		this.permissionAnnotationExtractor = permissionAnnotationExtractor;
	}

	public static WsSecurityFeature<RestrictTo> restictToBased(WsRequestPermissionProvider requestPermissionProvider) {
		return new WsSecurityFeature<>(
			new WsPermissionAuthenticator(requestPermissionProvider),
			RestrictTo.class,
			RestrictTo::value
		);
	}

	@Override
	public void configure(ResourceInfo methodResourceInfo, FeatureContext methodResourcecontext) {
		// si l'annotation RestrictTo est présente sur la méthode de WS
		// alors c'est celle-là qui sera utilisée
		if(!addPermissionFilter(methodResourceInfo.getResourceMethod(), methodResourcecontext)) {
			// si l'annotation RestrictTo n'est pas présente sur la méthode de WS
			// mais qu'elle est présente sur la classe
			// alors la valeur de l'annotation de la classe sera utilisée sur la méthode
			addPermissionFilter(methodResourceInfo.getResourceClass(), methodResourcecontext);
		}
	}
	
	/**
	 * Add a filter with the appropriate permission to the context if the annotatedElement is annotated with {@link #A}.
	 * @return true if the filter has been added to the context, else false 
	 */
	private boolean addPermissionFilter(AnnotatedElement annotatedElement, FeatureContext methodResourcecontext) {
		A permissionAnnotation = annotatedElement.getAnnotation(permissionAnnotationType);
		if (permissionAnnotation != null) {
			methodResourcecontext.register(new WsSecurityRequestFilter(
				permissionAnnotationExtractor.apply(permissionAnnotation),
				authenticator
			));
			return true;
		}
		return false;
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
