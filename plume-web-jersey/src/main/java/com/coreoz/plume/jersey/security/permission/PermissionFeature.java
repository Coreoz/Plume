package com.coreoz.plume.jersey.security.permission;

import com.coreoz.plume.jersey.security.AuthorizationSecurityFeature;
import com.coreoz.plume.jersey.security.AuthorizationVerifier;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

/**
 * Add a permission mechanism to restrict web-service access.
 *
 * @param <A> The annotation type from which a permission will be fetched
 */
@Slf4j
public class PermissionFeature<A extends Annotation> implements DynamicFeature {
    private final AuthorizationSecurityFeature<A> authorizationSecurityFeature;

	public PermissionFeature(@Nonnull PermissionRequestProvider requestPermissionProvider,
                             @Nonnull Class<A> permissionAnnotationType, @Nonnull Function<A, String> permissionAnnotationExtractor) {
        this.authorizationSecurityFeature = new AuthorizationSecurityFeature<A>(
            permissionAnnotationType,
            makeAuthorizationVerifier(requestPermissionProvider, permissionAnnotationExtractor)
        );
	}

    private static <A extends Annotation> AuthorizationVerifier<A> makeAuthorizationVerifier(
        PermissionRequestProvider requestPermissionProvider,
        Function<A, String> permissionAnnotationExtractor
    ) {
        return (authorizationAnnotation, requestContext) -> {
            if(!authorize(requestPermissionProvider, requestContext, permissionAnnotationExtractor.apply(authorizationAnnotation))) {
                throw new ForbiddenException();
            }
        };
    }

    private static boolean authorize(PermissionRequestProvider requestPermissionProvider, ContainerRequestContext requestContext, String permissionRequiredToAccessResource) {
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

	public static PermissionFeature<RestrictTo> restrictTo(PermissionRequestProvider requestPermissionProvider) {
		return new PermissionFeature<>(
			requestPermissionProvider,
			RestrictTo.class,
			RestrictTo::value
		);
	}

	@Override
	public void configure(ResourceInfo methodResourceInfo, FeatureContext methodResourceContext) {
        authorizationSecurityFeature.configure(methodResourceInfo, methodResourceContext);
	}
}
