package org.vagabond.engine.email.kafka.service;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.vagabond.engine.email.kafka.payload.EmailKafkaRequest;
import org.vagabond.engine.email.service.EmailService;

@ApplicationScoped
public class EmailKafkaService {

    @Inject
    @Channel("mail")
    @Broadcast
    Emitter<EmailKafkaRequest> emailEmitter;

    @Inject
    EmailService emailService;

    @Inject
    Mailer mailer;

    @Incoming("mail")
    @Blocking
    public Uni<Void> consume(EmailKafkaRequest email) {
        Log.infof("kafka receive mail %s", email);
        return emailService.send(email.to(), email.subject(), email.html()).onFailure()
                .invoke(t -> Log.info("Oh no! We received a failure: " + t.getMessage()));
    }

    public EmailKafkaRequest sendEmailOutgoing(EmailKafkaRequest email) {
        emailEmitter.send(email);
        return email;
    }
}
