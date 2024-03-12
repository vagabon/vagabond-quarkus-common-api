package org.vagabond.common.email;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.vagabond.common.email.payload.EmailRequest;

@Path("/email")
public class EmailResource {

    @Inject
    EmailService emailService;

    @GET
    @Path("/produce")
    public Response produce() {
        EmailRequest mail = emailService.sendEmailOutgoing(new EmailRequest("gonzague.clement@gmail.com", "test", "test", null));
        return Response.ok(mail).build();
    }

}
