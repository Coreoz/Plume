package com.coreoz.plume.jersey.security.permission;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to specify that an exposed API can be accessed without any sort of authentication mechanism.
 *
 * See {@link RequireExplicitAccessControlFeature} for details.
 */
@Documented
@Retention (RUNTIME)
@Target({TYPE, METHOD})
public @interface PublicApi {

}
