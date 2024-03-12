package org.vagabond.common.email.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailResource;
import org.vagabond.common.email.EmailService;

@QuarkusTest
class EmailResourceTest {

    @Inject
    EmailService emailService;

    @Inject
    EmailResource emailResource;

    @Test
    void testProduce() {

        // Mockito.when(emailService.sendEmailOutgoing(Mockito.any())).thenReturn(new EmailRequest("test@gmail.com", "subject", "html",
        // null));

        Response response = emailResource.produce();

        var email = new EmailEntity();
        email.to = "vagabond.git@gmail.com";
        email.subject = "subject";
        email.text = "text";
        email.userId = 1L;
        emailService.finishEmail(email);
        emailService.finishEmailWithError(email);
        emailService.sendMail(email.to, email.subject, email.text);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
}
