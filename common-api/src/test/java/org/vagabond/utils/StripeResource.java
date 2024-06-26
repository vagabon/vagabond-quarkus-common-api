package org.vagabond.utils;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.vagabond.common.api.stripe.AbstractStripeResource;

@Path("/stripe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StripeResource extends AbstractStripeResource {

    public StripeResource() {
        super(100, "pro");
    }

}
