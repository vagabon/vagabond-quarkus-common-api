package org.vagabond.common.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CaptchaResponse(Boolean success) {

}
