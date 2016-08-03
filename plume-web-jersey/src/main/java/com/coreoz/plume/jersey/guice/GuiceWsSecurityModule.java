package com.coreoz.plume.jersey.guice;

import com.coreoz.plume.jersey.security.WsAuthenticator;
import com.coreoz.plume.jersey.security.WsPermissionAuthenticator;
import com.coreoz.plume.jersey.security.WsRequestPermissionProvider;
import com.coreoz.plume.jersey.security.WsSecurityFeature;
import com.google.inject.AbstractModule;

/**
 * Le module Guice pour activer le module de sécurité sur les web-services Jersey.
 * Pour l'utiliser, il faut :
 * <ol>
 * 	<li>que l'application fournisse une implémentation de {@link WsRequestPermissionProvider}</li>
 * 	<li>que la configuration de Jersey inscrive l'instance de {@link WsSecurityFeature} : {@code register(guiceFactory.getInstance(WsSecurityFeature.class));} </li>
 * </ol>
 */
public class GuiceWsSecurityModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WsAuthenticator.class).to(WsPermissionAuthenticator.class);
	}

}
