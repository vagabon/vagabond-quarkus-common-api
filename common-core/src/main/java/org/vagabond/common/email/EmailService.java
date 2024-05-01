package org.vagabond.common.email;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.vagabond.common.email.payload.EmailRequest;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class EmailService extends BaseService<EmailEntity> {

    @ConfigProperty(name = "quarkus.mailer.from", defaultValue = "from_not_found")
    public String from;

    @Inject
    private EmailRepository emailRepository;

    @Inject
    @Channel("mail-out")
    @Broadcast
    private Emitter<EmailEntity> emailEmitter;

    @Inject
    private ReactiveMailer reactiveMailer;

    @Inject
    private Mailer mailer;

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
        return persist(email);
    }

    @Transactional
    public void finishEmail(EmailEntity email) {
        var hqlLink = "update EmailEntity set send = true, error = false where id = :emailId";
        getRepository().getEntityManager().createQuery(hqlLink).setParameter("emailId", email.id).executeUpdate();
    }

    @Transactional
    public EmailEntity finishEmailWithError(EmailEntity email) {
        email.send = false;
        email.error = true;
        return persist(email);
    }

    public boolean sendMail(EmailEntity email) {
        Log.infof("%s %s %s", email.to, email.subject, email.text);
        try {
            var mailWithHtml = Mail.withHtml(email.to, email.subject, email.text);
            mailWithHtml.setFrom(from);
            mailer.send(mailWithHtml);
            finishEmail(email);
            return true;
        } catch (Exception exception) {
            finishEmailWithError(email);
        }
        return false;
    }

    public Uni<Void> sendMailReactiv(EmailEntity email) {
        Log.infof("%s %s %s", email.to, email.subject, email.text);
        var mailWithHtml = Mail.withHtml(email.to, email.subject, email.text);
        mailWithHtml.setFrom(from);
        return reactiveMailer.send(mailWithHtml);
    }

    @Override
    public BaseRepository<EmailEntity> getRepository() {
        return emailRepository;
    }
}
