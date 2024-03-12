package org.vagabond.common.email;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class EmailKafkaService {

    @Inject
    EmailService emailService;

    @Incoming("mail")
    @Blocking
    public Uni<Void> consumeEmail(EmailEntity email) {
        Log.infof("kafka receive mail %s", email);
        return emailService.sendMail(email.to, email.subject, email.text).onItem().transform(result -> {
            emailService.finishEmail(email);
            return result;
        }).onFailure().invoke(t -> {
            emailService.finishEmailWithError(email);
            Log.info("Oh no! We received a failure: " + t.getMessage());
        });
    }
}
