package org.vagabond.resource;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.email.entity.EmailEntity;
import org.vagabond.common.email.service.EmailService;
import org.vagabond.engine.crud.resource.BaseCrudUserResource;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;

@Path("/email-test")
class CrudUserResource extends BaseCrudUserResource<EmailEntity> {

    @Inject
    private EmailService emailService;

    @PostConstruct
    public void postConstruct() {
        service = emailService;
    }
}

@QuarkusTest
class BaseCrudUserResourceTest extends BaseDataTest {

    @Test
    @SuppressWarnings("null")
    void testCreate() {
        var url = "/email-test";

        var email = new EmailEntity();
        email.to = "to";
        email.subject = "subject";
        var emailPersist = given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).body(email)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post(url)
            .then().statusCode(200).extract().body().as(EmailEntity.class);

        emailPersist.subject = "subject2";
        given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).body(emailPersist)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .when().put(url).then().statusCode(200);

        given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).when()
            .delete(url + "/desactivate?id=" + emailPersist.id).then()
            .statusCode(200);
    }
}
