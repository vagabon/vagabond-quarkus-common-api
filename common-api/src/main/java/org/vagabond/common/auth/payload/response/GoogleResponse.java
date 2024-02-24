package org.vagabond.common.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleResponse {
    public String email;
    public String id;
    public String locale;
    public String name;
    public String picture;
    @JsonAlias("verified_email")
    public boolean verifiedEmail;
    @JsonAlias("given_name")
    public String givenName;
    @JsonAlias("family_name")
    public String familyName;
}
