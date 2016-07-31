package com.coreoz.plume.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Handle the configuration loading.
 *
 * <p>By default, the <code>application.conf</code> file located in src/main/resources/ is loaded</p>
 *
 * <p>The configuration can be externalize, to do so the system property <code>-Dconfig.file</code> should be set.</p>
 *
 * <p>More information: {@link ConfigFactory#defaultApplication()}
 * or <a href="https://github.com/typesafehub/config#standard-behavior">https://github.com/typesafehub/config#standard-behavior</a></p>
 */
@Singleton
public class ConfigProvider implements Provider<Config> {

	private Config config;

	public ConfigProvider() {
		this.config = ConfigFactory.load();
	}

	@Override
	public Config get() {
		return config;
	}

}
