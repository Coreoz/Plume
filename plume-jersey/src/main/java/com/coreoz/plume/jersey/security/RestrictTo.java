package com.coreoz.plume.jersey.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Restraint l'accès à une ressource/web-service aux utilisateurs ayant la permission nécéssaire
 */
@Documented
@Retention (RUNTIME)
@Target({TYPE, METHOD})
public @interface RestrictTo {

	/**
	 * Retourne la valeur de la permission qu'un utilisateur doit avoir pour accéder à la ressource/web-service
	 */
	String value();

}
