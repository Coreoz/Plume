package com.coreoz.plume.mail;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MailerProviderTest {
	@Test
	public void should_generate_well_form_property_file() {
		Config config = ConfigFactory.parseMap(Map.of(
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
