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
    @TestSecurity(user = "admin")
    void sendNotification() {
        given().body(user.id).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .when()
            .post("/notifications/send").then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "admin")
    @SuppressWarnings("null")
    void notificationCrud() {

        var notification = new NotificationEntity();
        notification.user = user;
        var notificationPersist = given().body(notification)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .post("/notifications")
            .then().statusCode(200).extract().body().as(NotificationEntity.class);

        given().body(notificationPersist)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .put("/notifications").then().statusCode(200);

        given().when().get("/notifications/" + notificationPersist.id).then()
            .statusCode(200);
        given().when().get("/notifications/").then().statusCode(200);
        given().when().get("/notifications/count").then().statusCode(200);
        given().when().get("/notifications/count/" + user.id).then().statusCode(200);

        given().when().get("/notifications/search?search=search").then().statusCode(200);
        given().when().get("/notifications/search?search=search&entityId=1").then()
            .statusCode(200);

        given().when().put("/notifications/read-all").then().statusCode(200);
        given().when().put("/notifications/readAll/" + user.id).then().statusCode(200);

        given().when().delete("/notifications/desactivate?id=" + notificationPersist.id)
            .then().statusCode(200);
    }
}
