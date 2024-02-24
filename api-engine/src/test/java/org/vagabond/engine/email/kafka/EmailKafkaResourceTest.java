package org.vagabond.engine.email.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vagabond.engine.email.kafka.payload.EmailKafkaRequest;
import org.vagabond.engine.email.kafka.service.EmailKafkaService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class EmailKafkaResourceTest {

    @InjectMock
    EmailKafkaService emailKafkaService;

    @Inject
    EmailKafkaResource emailKafkaResource;

    @Test
    void testProduce() {

        Mockito.when(emailKafkaService.sendEmailOutgoing(Mockito.any()))
                .thenReturn(new EmailKafkaRequest("test@gmail.com", "subject", "html"));

        Response response = emailKafkaResource.produce();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
}
