package com.coreoz.plume.jersey.security.bearer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation that can be used to set up an {@link com.coreoz.plume.jersey.security.AuthorizationSecurityFeature}
 * with a {@link BearerAuthenticator}
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface BearerRestricted {
}
