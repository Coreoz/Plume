package com.coreoz.plume.db.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GuiceTestRunner implements BeforeTestExecutionCallback {
    private static final Map<String, Injector> injectorCache = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Optional<Object> test = context.getTestInstance();

        if (test.isPresent()) {
            GuiceTest guiceTest = test.get().getClass().getAnnotation(GuiceTest.class);

            if (guiceTest != null) {
                Injector injector = computeInjector(guiceTest);
                injector.injectMembers(test.get());
            }
        }
    }

    private static Injector computeInjector(GuiceTest guiceTest) {
        if (guiceTest.cacheInjector()) {
            return injectorCache.computeIfAbsent(Arrays.toString(guiceTest.value()), cacheKey -> createInjector(guiceTest.value()));
        }
        return createInjector(guiceTest.value());
    }

    private static Injector createInjector(Class<? extends Module>[] modules) {
        return Guice.createInjector(Arrays.stream(modules).map(clazz -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toArray(Module[]::new));
    }
}
