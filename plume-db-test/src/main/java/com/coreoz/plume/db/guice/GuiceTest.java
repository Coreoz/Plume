package com.coreoz.plume.db.guice;

import com.google.inject.Module;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Make a unit test be injected by Guice
 */
@ExtendWith(GuiceTestRunner.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GuiceTest {
    /**
     * The Guice modules used to execute the tests
     */
    Class<? extends Module>[] value();
    /**
     * If the injector created for the modules should be reused in other tests (or use the already created injector)
     */
    boolean cacheInjector() default true;
}
