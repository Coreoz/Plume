package com.coreoz.plume.mail.guice;

import org.simplejavamail.api.mailer.Mailer;

import com.coreoz.plume.mail.MailerProvider;
import com.google.inject.AbstractModule;

public class GuiceMailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Mailer.class).toProvider(MailerProvider.class);
	}

}
