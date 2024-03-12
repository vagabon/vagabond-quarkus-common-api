package org.vagabond.common.kafka.email;

import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;
import org.vagabond.common.email.EmailEntity;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EmailKafkaIncomingServiceTest {

    @Inject
    EmailKafkaIncomingService emailKafkaService;

    @Test
    void consume() {
        var email = new EmailEntity();
        email.to = "vagabond.git@gmail.com";
        email.subject = "subject";
        email.text = "text";
        email.userId = 1L;

        var message = Message.of(email);
        emailKafkaService.consumeIncoming(message);

        assertNotEquals(true, email.send);
    }
}
