package com.coreoz.plume.conf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.typesafe.config.Config;

public class ConfigProviderTest {

	@Test
	public void check_that_the_configuration_is_correctly_loaded() {
		Config config = new ConfigProvider().get();

		assertThat(config.getString("generic.property")).isEqualTo("Text property");
		assertThat(config.getIntList("generic.to-be-overriden")).containsExactly(4, 5);
	}
	
	@Test
	public void check_that_failed_include_raises_exception() {
		try {
			System.setProperty("config.resource", "failed-import.conf");
			new ConfigProvider().get();
			fail("Should raise an exception");
		} catch (Exception e) {
			// as excepted
		}
	}

}
