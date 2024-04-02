package org.vagabond.utils;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.vagabond.common.api.stripe.AbstractStripeResource;

import io.quarkus.security.Authenticated;

@Path("/stripe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class StripeResource extends AbstractStripeResource {

}
