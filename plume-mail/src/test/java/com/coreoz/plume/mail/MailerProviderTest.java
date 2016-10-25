package com.coreoz.plume.mail;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MailerProviderTest {

	@Test
	public void should_generate_well_form_property_file() {
		Config config = ConfigFactory.parseMap(ImmutableMap.of(
			"mail.\"javaxmail.debug\"", "true",
			"mail.transportstrategy", "SMTP_SSL"
		));
		String properties = MailerProvider.readMailConfiguration(config);

		Assertions.assertThat(properties).isEqualTo(
			"simplejavamail.javaxmail.debug=true\n"
			+ "simplejavamail.transportstrategy=SMTP_SSL"
		);
	}

}
