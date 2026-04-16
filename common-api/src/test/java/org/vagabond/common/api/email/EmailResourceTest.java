package org.vagabond.common.api.email;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailService;
import org.vagabond.utils.BaseDataTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class EmailResourceTest extends BaseDataTest {

    @Inject
    EmailService emailService;

    @Inject
    EmailResource emailResource;

    @Mock
    Mailer mailer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        emailService.setMailer(mailer);
    }

    @Test
    @TestSecurity(user = "admin")
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

    @Test
    void testSendMailException() {
        EmailEntity email = new EmailEntity();
        email.to = "test@example.com";
        email.subject = "Test Subject";
        email.text = "<p>Test HTML</p>";

        doThrow(new RuntimeException("Failed to send email")).when(mailer).send(any(Mail.class));

        boolean result = emailService.sendMail(email);

        assertEquals(false, result);
    }
}
