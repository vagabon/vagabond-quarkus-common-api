package org.vagabond.notification;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.notification.entity.NotificationEntity;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class NotificationResourceTest extends BaseDataTest {

    @Test
    void sendNotification() {

        given().body(user.id())
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .post("/notification/send").then().statusCode(200);
    }

    @Test
    @SuppressWarnings("null")
    @TestSecurity(user = "admin")
    void notificationCrud() {
        var notification = new NotificationEntity();
        notification.userId = user.id();
        var notificationPersist = given().body(notification)
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .post("/notification")
            .then().statusCode(200).extract().body().as(NotificationEntity.class);

        given().body(notificationPersist)
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .put("/notification").then().statusCode(200);

        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .get("/notification/" + notificationPersist.id).then()
            .statusCode(200);
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when().get("/notification/").then()
            .statusCode(200);
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when().get("/notification/count")
            .then().statusCode(200);
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .get("/notification/count/" + user.id()).then().statusCode(200);

        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .get("/notification/search?search=search").then().statusCode(200);
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .get("/notification/search?search=search&entityId=1").then()
            .statusCode(200);

        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when().put("/notification/read-all")
            .then().statusCode(200);
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .put("/notification/readAll/" + user.id()).then().statusCode(200);

        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when()
            .delete("/notification/desactivate?id=" + notificationPersist.id)
            .then()
            .statusCode(200);
    }
}
