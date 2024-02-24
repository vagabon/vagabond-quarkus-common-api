package org.vagabond.common.user;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.vagabond.common.user.payload.PasswordRequest;
import org.vagabond.utils.BaseDataTest;

@QuarkusTest
class UserResourceTest extends BaseDataTest {

    private final File FILE = new File("./src/test/resources/application.properties");

    @Test
    @TestSecurity(user = "admin")
    void findAll() {
        given().when().get("/user/?page=0&max=10&sort=id").then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin")
    void findBy() {
        given().when().get("/user/findBy?fields=username&values=username&first=0&max=10").then().statusCode(200);

        given().when().get("/user/find50?username=username").then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin")
    void createAndUpdateAndDelete() {

        var userRequest = user;
        userRequest.id = null;
        userRequest.username = "newUser";
        userRequest.password = "password";
        var newUser = given().body(userRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/user").then()
                .statusCode(200).extract().body().as(UserEntity.class);

        given().when().get("/user/" + newUser.id).then().statusCode(200);

        userRequest.id = newUser.id;
        userRequest.password = newUser.password;
        given().body(newUser).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/user").then().statusCode(200);

        newUser.email = "newEmail@google.fr";
        given().body(newUser).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/user/email").then().statusCode(200);

        var passwordRequest = new PasswordRequest(newUser.id, "password", "newPassword");
        given().body(passwordRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/user/password").then()
                .statusCode(200);

        given().header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA).multiPart("file", FILE, "text/plain").when()
                .post("/user/upload?id=" + newUser.id).then().statusCode(200);

        given().when().delete("/user/?id=" + newUser.id).then().statusCode(200);

        given().when().get("/user/find50").then().statusCode(200);
    }
}
