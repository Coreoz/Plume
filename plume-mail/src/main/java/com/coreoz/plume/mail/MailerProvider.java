package com.coreoz.plume.mail;

import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.simplejavamail.internal.util.ConfigLoader;
import org.simplejavamail.mailer.Mailer;

import com.google.common.base.Charsets;
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
			new ByteArrayInputStream(readMailConfiguration(config).getBytes(Charsets.ISO_8859_1)),
			true
		);

		return new Mailer();
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
