package org.vagabond.common.auth.payload.response;

public record FacebookResponse(String id, String email, String name, FacebookResponsePicture picture) {
}