package org.vagabond.resource;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.crud.resource.BaseCrudUserResource;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@Path("/crud-user")
class CrudUserResource extends BaseCrudUserResource<UserEntity, UserEntity> {

    @Inject
    private UserService userService;

    @PostConstruct
    public void postConstruct() {
        service = userService;
        responseClass = UserResponse.class;
    }
}

@QuarkusTest
class BaseCrudUserResourceTest extends BaseDataTest {

    @Test
    @TestSecurity(user = "user")
    void testCreate() {
        var url = "/crud-user";

        var user = new UserEntity();
        user.username = "blablablu";
        user.password = "password";
        var userPersist = given().body(user).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post(url).then()
                .statusCode(200).extract().body().as(UserEntity.class);

        userPersist.password = "password2";
        given().body(userPersist).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put(url).then().statusCode(200);

        given().when().delete(url + "/desactivate?id=" + userPersist.id).then().statusCode(200);
    }
}
