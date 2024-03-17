package org.vagabond.common.api.email;

import java.security.Principal;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vagabond.common.email.EmailEntity;
import org.vagabond.common.email.EmailService;
import org.vagabond.utils.BaseDataTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EmailResourceTest extends BaseDataTest {

    @Inject
    EmailService emailService;

    @Inject
    EmailResource emailResource;

    @Test
    void testProduce() {

        var contexteMock = Mockito.mock(SecurityContext.class);
        var principale = Mockito.mock(Principal.class);
        when(contexteMock.getUserPrincipal()).thenReturn(principale);
        when(principale.getName()).thenReturn("admin");
        Response response = emailResource.produce(contexteMock);

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
