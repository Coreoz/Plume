package com.coreoz.plume.conf;

import com.typesafe.config.Config;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ConfigProviderTest {

	@Test
	public void check_that_the_configuration_is_correctly_loaded() {
		Config config = new ConfigProvider().get();

		assertThat(config.getString("generic.property")).isEqualTo("Text property");
		assertThat(config.getIntList("generic.to-be-overriden")).containsExactly(4, 5);
	}

}
