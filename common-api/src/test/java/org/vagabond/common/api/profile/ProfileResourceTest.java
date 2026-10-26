package org.vagabond.common.api.profile;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.profile.entity.ProfileEntity;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class ProfileResourceTest extends BaseDataTest {

    @Test
    @TestSecurity(user = "admin")
    @SuppressWarnings("null")
    void crud() {
        var profileRequest = new ProfileEntity();
        profileRequest.name = "name";
        profileRequest.roles = "NAME";
        var profile = given().body(profileRequest)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .post("/roles").then().statusCode(200).extract().body()
            .as(ProfileEntity.class);

        profileRequest.id = profile.id;
        given().body(profile).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .when()
            .put("/roles").then().statusCode(200);

        given().when().delete("/roles/?id=" + profile.id).then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "user")
    void find() {
        given().when().get("/roles/1").then().statusCode(200);
        given().when().get("/roles/findBy?fields=active&values=true").then()
            .statusCode(200);

    }
}
