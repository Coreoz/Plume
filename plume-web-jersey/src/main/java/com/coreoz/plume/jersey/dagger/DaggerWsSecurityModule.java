package com.coreoz.plume.jersey.dagger;

import com.coreoz.plume.jersey.security.WsAuthenticator;
import com.coreoz.plume.jersey.security.WsPermissionAuthenticator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaggerWsSecurityModule {

	@Provides
	@Singleton
	static WsAuthenticator provideWsAuthenticator(WsPermissionAuthenticator wsPermissionAuthenticator) {
		return wsPermissionAuthenticator;
	}

}
