package org.vagabond.common.api.stripe;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.stripe.configuration.StripeConfiguration;
import org.vagabond.common.stripe.payload.StripePayloadRequest;
import org.vagabond.common.stripe.payment.UserPaymentService;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.resource.BaseSecurityResource;

import io.quarkus.logging.Log;

public abstract class AbstractStripeResource extends BaseSecurityResource<UserEntity, UserEntity> {

    @ConfigProperty(name = "stripe.plan.amount")
    protected int amount;

    @ConfigProperty(name = "website.url")
    protected String websiteUrl;

    @ConfigProperty(name = "website.url.payment.ok")
    protected String websiteUrlPaymentOk;

    @ConfigProperty(name = "website.url.payment.ko")
    protected String websiteUrlPaymentKo;

    @Inject
    protected StripeConfiguration stripeConfiguration;

    @Inject
    protected UserPaymentService userPaymentService;

    @POST
    @Path("/payment-intent")
    public String createPaymentIntent() throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "eur");
        PaymentIntent intent = stripeConfiguration.create(params);
        return intent.getClientSecret();
    }

    @GET
    @Path("/payment-intent")
    @PermitAll
    public Response validatePaypmentIntent(@QueryParam("payment_intent") String paymentIntent,
            @QueryParam("payment_intent_client_secret") String pamentIntentCLientSecret,
            @QueryParam("redirect_status") String redirectStatus) throws StripeException {
        PaymentIntent intent = stripeConfiguration.retrieve(paymentIntent);
        String htmlPageUrl = websiteUrl + websiteUrlPaymentKo;
        if (intent != null && pamentIntentCLientSecret.equals(intent.getClientSecret())) {
            htmlPageUrl = websiteUrl + websiteUrlPaymentOk + paymentIntent + "/" + pamentIntentCLientSecret;
        }
        return Response.status(Response.Status.FOUND).header("Location", htmlPageUrl).build();
    }

    @POST
    @Path("/validate")
    public Response validatePayment(@Context SecurityContext contexte, StripePayloadRequest stripePayload) throws StripeException {
        var intent = stripeConfiguration.retrieve(stripePayload.intent());
        if (intent != null && stripePayload.secret().equals(intent.getClientSecret())) {
            Log.infof("%s %s ", stripePayload.secret(), intent.getClientSecret());
        }
        UserEntity userConnected = hasRole(contexte, "USER");
        userPaymentService.createPayment(stripePayload.intent(), stripePayload.secret(), intent);
        doAfterCreatePayment(userConnected);
        return responseOk(userConnected);
    }

    public void doAfterCreatePayment(UserEntity user) {
    }

}
