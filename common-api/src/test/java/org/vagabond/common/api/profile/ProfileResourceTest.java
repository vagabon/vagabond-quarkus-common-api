package org.vagabond.common.api.profile;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class ProfileResourceTest extends BaseDataTest {

    @Test
    @TestSecurity(user = "admin")
    void crud() {
        var profileRequest = new ProfileEntity();
        profileRequest.name = "name";
        profileRequest.roles = "NAME";
        var profile = given().body(profileRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/profile")
                .then().statusCode(200).extract().body().as(ProfileEntity.class);

        profileRequest.id = profile.id;
        given().body(profile).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/profile").then().statusCode(200);

        given().when().delete("/profile/?id=" + profile.id).then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "user")
    void find() {
        given().when().get("/profile/1").then().statusCode(200);
        given().when().get("/profile/findBy?fields=active&values=true").then().statusCode(200);

    }
}
