package com.coreoz.plume.mail;

import com.coreoz.plume.conf.guice.GuiceConfModule;
import com.coreoz.plume.mail.guice.GuiceMailModule;
import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.google.inject.Guice;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import java.util.List;

public class MailIntegrationTest {
    @SneakyThrows
    @Test
    public void when_email_is_sent_smtp_server_must_receive_it() {
        Mailer mailer = Guice.createInjector(new GuiceMailModule(), new GuiceConfModule()).getInstance(Mailer.class);
        try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(2525)) {
            Email newEmail = EmailBuilder
                .startingBlank()
                .to("Russell Powell", "rpowell0@whitehouse.gov")
                .withSubject("Plume")
                .withPlainText("You should check this Java library out!")
                .buildEmail();
            mailer.sendMail(newEmail, false);

            List<SmtpMessage> emails = dumbster.getReceivedEmails();
            Assertions.assertThat(emails).hasSize(1);
            SmtpMessage email = emails.get(0);
            Assertions.assertThat(email.getHeaderValue("To")).isEqualTo("Russell Powell <rpowell0@whitehouse.gov>");
            Assertions.assertThat(email.getHeaderValue("Subject")).isEqualTo("Plume");
            Assertions.assertThat(email.getBody()).isEqualTo("You should check this Java library out!");
        }
    }
}
