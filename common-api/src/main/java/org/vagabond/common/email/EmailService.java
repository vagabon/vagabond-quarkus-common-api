package org.vagabond.common.email;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.vagabond.common.email.payload.EmailRequest;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

@ApplicationScoped
public class EmailService extends BaseService<EmailEntity> {

    @Inject
    private EmailRepository emailRepository;

    @Inject
    @Channel("mail-out")
    @Broadcast
    Emitter<EmailEntity> emailEmitter;

    @Inject
    ReactiveMailer reactiveMailer;

    public EmailRequest sendEmailOutgoing(EmailRequest emailRequest) {
        var email = createEmail(emailRequest);
        emailEmitter.send(email);
        return emailRequest;
    }

    @Transactional
    public EmailEntity createEmail(EmailRequest emailRequest) {
        var email = new EmailEntity();
        email.to = emailRequest.to();
        email.subject = emailRequest.subject();
        email.text = emailRequest.html();
        email.userId = emailRequest.user() != null ? emailRequest.user().id : null;
        email.send = false;
        persist(email);
        return email;
    }

    @Transactional
    public EmailEntity finishEmail(EmailEntity email) {
        email.send = true;
        email.error = false;
        persist(email);
        return email;
    }

    @Transactional
    public EmailEntity finishEmailWithError(EmailEntity email) {
        email.send = false;
        email.error = true;
        persist(email);
        return email;
    }

    public Uni<Void> sendMail(String to, String subject, String text) {
        Log.infof("%s %s %s", to, subject, text);
        return reactiveMailer.send(Mail.withHtml(to, subject, text));
    }

    @Override
    public BaseRepository<EmailEntity> getRepository() {
        return emailRepository;
    }
}
