package com.coreoz.plume.jersey.security.permission;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * Force explicit access control being added on each exposed API.
 * The goal of this is to avoid creating an API without any proper access control mechanism:
 * if an API must be accessed only by providing a valid Authorization header value,
 * then this API must have been annotated, for example with {@link RestrictTo}.
 * A public API can be annotated with {@link PublicApi}.<br>
 * <br>
 * Usage when configuring Jersey is:<br>
 * {@code resourceConfig.register(RequireExplicitAccessControlFeature.accessControlAnnotations(PublicApi.class, RestrictToAdmin.class));}
 */
public class RequireExplicitAccessControlFeature implements DynamicFeature {

	private static final Logger logger = LoggerFactory.getLogger(RequireExplicitAccessControlFeature.ForbiddenAccessFilter.class);

	private final Set<Class<? extends Annotation>> registeredAccessControlAnnotations;

	public RequireExplicitAccessControlFeature(Set<Class<? extends Annotation>> registeredAccessControlAnnotations) {
		this.registeredAccessControlAnnotations = registeredAccessControlAnnotations;
	}

	@Override
	public void configure(ResourceInfo methodResourceInfo, FeatureContext methodResourceContext) {
		// Ignore Jersey internals
		if(methodResourceInfo.getResourceClass().getName().startsWith(
			"org.glassfish.jersey.server.wadl"
		)) {
			return;
		}

		if(!hasAccessControlAnnotation(methodResourceInfo.getResourceMethod(), methodResourceContext)
			&& !hasAccessControlAnnotation(methodResourceInfo.getResourceClass(), methodResourceContext)) {
			logger.warn(
				"The API {}.{} is not annotated with any registered access control annotations, "
				+ "to make this API usable, it needs either to be annoted with one of these valid annotations ({}), "
				+ "or the access control annotation annotation it is using must be registered "
				+ "in the Jersey configuration in the RequireExplicitAccessControlFeature configuration.",
				methodResourceInfo.getResourceClass(),
				methodResourceInfo.getResourceMethod(),
				registeredAccessControlAnnotations
			);
			methodResourceContext.register(new ForbiddenAccessFilter());
		}
	}

	@SafeVarargs
	public static RequireExplicitAccessControlFeature accessControlAnnotations(Class<? extends Annotation> ...annotations) {
		return new RequireExplicitAccessControlFeature(ImmutableSet.copyOf(annotations));
	}

	private boolean hasAccessControlAnnotation(AnnotatedElement annotatedElement, FeatureContext methodResourceContext) {
		return registeredAccessControlAnnotations
			.stream()
			.anyMatch(registeredAccessControlAnnotation -> annotatedElement.getAnnotation(registeredAccessControlAnnotation) != null);
	}

	private static class ForbiddenAccessFilter implements ContainerRequestFilter {
		@Override
		public void filter(ContainerRequestContext requestContext) throws IOException {
			throw new ForbiddenException();
		}
	}

}
