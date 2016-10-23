package com.coreoz.plume.jersey.security.permission;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.function.Function;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add a permission mechanism to restrict web-service access.
 *
 * @param <A> The annotation type from which a permission will be fetched
 */
public class WsPermissionFeature<A extends Annotation> implements DynamicFeature {

	private static final Logger logger = LoggerFactory.getLogger(WsPermissionFeature.class);

	private final WsPermissionRequestProvider requestPermissionProvider;
	private final Class<A> permissionAnnotationType;
	private final Function<A, String> permissionAnnotationExtractor;

	public WsPermissionFeature(WsPermissionRequestProvider requestPermissionProvider,
			Class<A> permissionAnnotationType, Function<A, String> permissionAnnotationExtractor) {
		this.requestPermissionProvider = requestPermissionProvider;
		this.permissionAnnotationType = permissionAnnotationType;
		this.permissionAnnotationExtractor = permissionAnnotationExtractor;
	}

	public static WsPermissionFeature<RestrictTo> restictTo(WsPermissionRequestProvider requestPermissionProvider) {
		return new WsPermissionFeature<>(
			requestPermissionProvider,
			RestrictTo.class,
			RestrictTo::value
		);
	}

	@Override
	public void configure(ResourceInfo methodResourceInfo, FeatureContext methodResourcecontext) {
		// if the method is annotated with RestrictTo,
		// then this annotation value will be used instead of the class annotation
		if(!addPermissionFilter(methodResourceInfo.getResourceMethod(), methodResourcecontext)) {
			// if the method isn't annotated with RestrictTo,
			// then the class annotation will be used if present
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
				requestPermissionProvider
			));
			return true;
		}
		return false;
	}

	private static class WsSecurityRequestFilter implements ContainerRequestFilter {

		private final String resourcePermission;
		private final WsPermissionRequestProvider requestPermissionProvider;

		public WsSecurityRequestFilter(String resourcePermission,
			WsPermissionRequestProvider requestPermissionProvider) {
			this.resourcePermission = resourcePermission;
			this.requestPermissionProvider = requestPermissionProvider;
		}

		@Override
		public void filter(ContainerRequestContext requestContext) throws IOException {
			if(!authorize(requestContext, resourcePermission)) {
				throw new ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
			}
		}

		boolean authorize(ContainerRequestContext requestContext, String permissionRequiredToAccessResource) {
			Collection<String> userPermissions = requestPermissionProvider.correspondingPermissions(requestContext);
			boolean isAuthorized = userPermissions.contains(permissionRequiredToAccessResource);

			if(!isAuthorized) {
				logger.warn(
					"Unauthorized access to {} by {}, required permission '{}' not found among {}",
					requestContext.getUriInfo().getAbsolutePath(),
					requestPermissionProvider.userInformation(requestContext),
					permissionRequiredToAccessResource,
					userPermissions
				);
			}

			return isAuthorized;
		}

	}

}
