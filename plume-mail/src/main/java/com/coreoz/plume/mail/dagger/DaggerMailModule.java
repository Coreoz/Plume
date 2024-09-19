package com.coreoz.plume.mail.dagger;

import jakarta.inject.Singleton;

import org.simplejavamail.api.mailer.Mailer;

import com.coreoz.plume.mail.MailerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerMailModule {

	@Provides
	@Singleton
	static Mailer provideMailer(MailerProvider mailerProvider) {
		return mailerProvider.get();
	}

}
