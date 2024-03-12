package org.vagabond.common.api.email;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EmailResourceTest {

    @Inject
    EmailService emailService;

    @Inject
    EmailResource emailResource;

    @Test
    void testProduce() {

        Response response = emailResource.produce();

        var email = new EmailEntity();
        email.to = "vagabond.git@gmail.com";
        email.subject = "subject";
        email.text = "text";
        email.userId = 1L;
        emailService.finishEmail(email);
        emailService.finishEmailWithError(email);
        emailService.sendMail(email);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
}
