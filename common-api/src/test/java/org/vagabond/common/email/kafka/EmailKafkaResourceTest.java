package org.vagabond.common.email.kafka;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailKafkaService;
import org.vagabond.common.email.EmailService;

@QuarkusTest
class EmailKafkaResourceTest {

    @InjectMock
    EmailService emailService;

    @Inject
    EmailKafkaService emailKafkaService;

    @Test
    void consume() {
        var email = new EmailEntity();
        email.to = "vagabond.git@gmail.com";
        email.subject = "subject";
        email.text = "text";
        email.userId = 1L;
        emailKafkaService.consumeEmail(email);

        assertNotEquals(true, email.send);
    }
}
