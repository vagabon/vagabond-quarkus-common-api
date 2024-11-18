package org.vagabond.common.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleIdentityResponse {
    public String iss;
    public String azp;
    public String aud;
    public String sub;
    public String email;
    @JsonAlias("email_verified")
    public boolean verifiedEmail;
    public String nbf;
    public String name;
    public String picture;
    @JsonAlias("given_name")
    public String givenName;
    @JsonAlias("family_name")
    public String familyName;
    public String iat;
    public String exp;
    public String jti;
    public String alg;
    public String kid;
    public String typ;

}
