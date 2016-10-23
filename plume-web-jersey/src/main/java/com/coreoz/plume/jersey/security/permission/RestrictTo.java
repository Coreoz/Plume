package com.coreoz.plume.jersey.security.permission;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Limit access to a web-service only to users having the required permission.
 */
@Documented
@Retention (RUNTIME)
@Target({TYPE, METHOD})
public @interface RestrictTo {

	/**
	 * The permission a user must have to access the web-service
	 */
	String value();

}
