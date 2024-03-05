package org.vagabond.engine.email.service;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmailService {

    @Inject
    Mailer mailer;

    @Inject
    ReactiveMailer reactiveMailer;

    @Transactional
    public Uni<Void> send(String to, String subject, String text) {
        Log.infof("%s %s %s", to, subject, text);
        return reactiveMailer.send(Mail.withHtml(to, subject, text));
    }
}
