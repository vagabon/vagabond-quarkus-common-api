package org.vagabond.notification.token;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.notification.token.payload.NotificationTokenRequest;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class NotificationTokenResourceTest extends BaseDataTest {

    @Test
    void createNotificationuser() {

        var entity = new NotificationTokenRequest(user.id(), "token");
        given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).body(entity)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .when().put("/notification/token/user").then()
            .statusCode(200);
    }
}
