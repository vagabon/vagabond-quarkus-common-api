package org.vagabond.common.stripe.payload;

public record StripePayloadRequest(String intent, String secret) {

}
