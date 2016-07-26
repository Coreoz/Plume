package com.coreoz.plume;

import java.io.File;

import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Gère le chargement des valeurs de configuration.<br>
 * <br>
 * Les fichiers de configuration se trouve dans src/main/resources/conf/<br>
 * Par défaut, le fichier de configuration choisi est "application.conf", pour changer ce fichier de configuration il faut utiliser le paramètre java
 * <strong>"-Dconfiguration.file"</strong>, par exemple : <code>-Dconfiguration.file=dev.conf</code><br>
 * <br>
 * Pour externaliser la configuration, il est possible d'utiliser le paramètre java <strong>"-Dconfiguration.path"</strong>, par exemple :
 * <code>-Dconfiguration.path="D:\dev\dev.conf"</code><br>
 * Dans le cas de la configuration externalisée, il est possible de référencer une configuration du projet avec la directive include, par exemple :
 * <code>include "D:/workspace/myproject/src/main/resources/conf/application.conf"</code><br>
 * <br>
 * Le service de configuration est basé sur la librairie config : <a href="https://github.com/typesafehub/config">https://github.com/typesafehub/config</a>
 */
@Singleton
public class ConfigProvider implements Provider<Config> {

	private static final Logger logger = LoggerFactory.getLogger(ConfigProvider.class);

	private Config config;

	public ConfigProvider() {
		this.config = initializeConfig();
	}

	@Override
	public Config get() {
		return config;
	}

	private Config initializeConfig() {
		String configurationParameterFile = System.getProperty("configuration.file");
		String configurationParameterPath = System.getProperty("configuration.path");

		if (configurationParameterPath == null) {
			String configurationFile = MoreObjects.firstNonNull(configurationParameterFile, "application.conf");
			logger.info("Configuration utilisée : {}", configurationFile);
			return ConfigFactory.load("conf/" + configurationFile);
		}
		logger.info("Configuration utilisée : {}", configurationParameterPath);
		return ConfigFactory.parseFile(new File(configurationParameterPath));
	}

}
