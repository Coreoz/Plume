package com.coreoz.plume.mail;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.mailer.MailerBuilder;

import com.typesafe.config.Config;

@Singleton
public class MailerProvider implements Provider<Mailer> {

	private final Mailer mailer;

	@Inject
	public MailerProvider(Config config) {
		this.mailer = initializeMailer(config);
	}

	@Override
	public Mailer get() {
		return mailer;
	}

	private static Mailer initializeMailer(Config config) {
		ConfigLoader.loadProperties(
			// Properties are in ISO 8859 1
			new ByteArrayInputStream(readMailConfiguration(config).getBytes(StandardCharsets.ISO_8859_1)),
			true
		);

		return MailerBuilder.buildMailer();
	}

	// visible for testing
	static String readMailConfiguration(Config config) {
		return config
			.getObject("mail")
			.entrySet()
			.stream()
			.map(entry -> "simplejavamail." + entry.getKey() + "=" + entry.getValue().unwrapped().toString())
			.collect(Collectors.joining("\n"));
	}

}
