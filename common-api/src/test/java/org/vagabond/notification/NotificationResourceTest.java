package org.vagabond.notification;

import java.util.Arrays;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.notification.NotificationEntity;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
class NotificationResourceTest extends BaseDataTest {

    @Test
    @TestSecurity(user = "user")
    void sendNotification() {
        given().body(Arrays.asList(1L)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                .post("/notification/send-notification").then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin")
    void notificationCrud() {

        var notification = new NotificationEntity();
        notification.user = user;
        var notificationPersist = given().body(notification).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                .post("/notification").then().statusCode(200).extract().body().as(NotificationEntity.class);

        given().body(notificationPersist).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/notification").then()
                .statusCode(200);

        given().when().get("/notification/" + notificationPersist.id).then().statusCode(200);
        given().when().get("/notification/").then().statusCode(200);
        given().when().get("/notification/count/" + user.id).then().statusCode(200);

        given().when().put("/notification/readAll/" + user.id).then().statusCode(200);

        given().when().delete("/notification/desactivate?id=" + notificationPersist.id).then().statusCode(200);
    }
}
