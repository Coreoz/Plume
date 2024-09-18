package com.coreoz.plume.jersey.guice;

import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.InjectionManagerProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.google.inject.Injector;

/**
 * Configure the bridge between HK2 and Guice for Jersey.<br>
 * <br>
 * The bridge enable to use the package scanning feature from Jersey with Guice
 * annotated classes. This feature enable Jersey to detect newly created
 * web-service classes without any special declaration.<br>
 * If you prefer to manually declare each web-service classes to keep a better
 * control over what Jersey is doing, then do not use this feature.
 */
public class JerseyGuiceFeature implements Feature {

	private final Injector injector;

	public JerseyGuiceFeature(Injector injector) {
		this.injector = injector;
	}

	@Override
	public boolean configure(FeatureContext context) {
		ServiceLocator serviceLocator = InjectionManagerProvider
			.getInjectionManager(context)
			.getInstance(ServiceLocator.class);

		GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
		GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
		guiceBridge.bridgeGuiceInjector(injector);

		return true;
	}

}
