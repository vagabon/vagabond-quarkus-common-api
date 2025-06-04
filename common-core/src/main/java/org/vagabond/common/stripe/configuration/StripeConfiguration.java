package org.vagabond.common.stripe.configuration;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class StripeConfiguration {

    @ConfigProperty(name = "stripe.secret-key")
    private String stripeSecretKey;

    public PaymentIntent create(Map<String, Object> params) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        return PaymentIntent.create(params);
    }

    public PaymentIntent retrieve(String paymentIntent) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        return PaymentIntent.retrieve(paymentIntent);
    }
}
