package com.coreoz.plume.jersey.security.size;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Modify the default Content size limit handled by the backend
 */
@Documented
@Retention (RUNTIME)
@Target({TYPE, METHOD})
public @interface ContentSizeLimit {

    /**
    * The maximum size of the content in bytes
    */
    int value();
}
