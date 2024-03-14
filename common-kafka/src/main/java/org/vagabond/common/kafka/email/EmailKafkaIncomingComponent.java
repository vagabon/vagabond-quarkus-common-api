package org.vagabond.common.kafka.email;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailService;
import org.vagabond.common.kafka.engine.IKafkaUniService;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class EmailKafkaIncomingComponent implements IKafkaUniService<EmailEntity> {

    @Inject
    private EmailService emailService;

    @Incoming("mail")
    @Blocking
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    public Uni<Void> consumeIncoming(Message<EmailEntity> emailMessage) {
        var email = emailMessage.getPayload();
        Log.infof("kafka receive mail %s", email);
        emailService.finishEmail(email);
        return emailService.sendMailReactiv(email);
    }
}
