package com.coreoz.plume.conf;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.typesafe.config.Config;

public class ConfigProviderTest {

	@Test
	public void check_that_the_configuration_is_correctly_loaded() {
		Config config = new ConfigProvider().get();

		assertThat(config.getString("generic.property")).isEqualTo("Text property");
		assertThat(config.getIntList("generic.to-be-overriden")).containsExactly(4, 5);
	}

}
