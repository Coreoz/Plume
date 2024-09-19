package com.coreoz.plume.conf;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

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

	@Inject
	public ConfigProvider() {
		// Should be changed when See https://github.com/lightbend/config/issues/587 is resolved
		this.config = ConfigFactory.load(ConfigParseOptions.defaults().setAllowMissing(false).setIncluder(new RequiredIncluder()));
	}

	@Override
	public Config get() {
		return config;
	}

}
