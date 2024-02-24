package org.vagabond.engine.email.kafka;

import org.vagabond.engine.email.kafka.payload.EmailKafkaRequest;
import org.vagabond.engine.email.kafka.service.EmailKafkaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/kafka")
public class EmailKafkaResource {

    @Inject
    EmailKafkaService emailKafkaService;

    @GET
    @Path("/produce")
    public Response produce() {
        EmailKafkaRequest mail = emailKafkaService.sendEmailOutgoing(
                new EmailKafkaRequest("gonzague.clement@gmail.com", "test", "test"));
        return Response.ok(mail).build();
    }

}
