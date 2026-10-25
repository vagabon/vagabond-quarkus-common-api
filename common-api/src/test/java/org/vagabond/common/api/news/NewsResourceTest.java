package org.vagabond.common.api.news;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.vagabond.common.file.entity.FileEntity;
import org.vagabond.common.news.entity.NewsEntity;
import org.vagabond.engine.gateway.CurrentUser;
import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class NewsResourceTest extends BaseDataTest {

    private static final File FILE = new File(
        "./src/test/resources/application.properties");

    @Inject
    NewsResource newsResource;

    @Test
    void doSearch() {
        var currentUser = new CurrentUser(user.id(), user.username(), "",
            Arrays.asList(user.roles().split(",")).stream()
                .collect(Collectors.toSet()));
        var response = newsResource.doSearch(currentUser,
            new StringBuilder("from NewsEntity e where e.title like ?1"), ">>id", "null",
            0,
            10);
        assertNotNull(response);
    }

    @Test
    @SuppressWarnings("null")
    void crud() {
        var newsRequest = new NewsEntity();
        newsRequest.description = "description";
        var news = given().body(newsRequest)
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
            .post("/news")
            .then().statusCode(200).extract().body().as(NewsEntity.class);

        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).when().get("/news/" + news.id).then()
            .statusCode(200);

        newsRequest.id = news.id;
        given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles()).body(news)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .when()
            .put("/news").then().statusCode(200);
    }

    @Test
    @SuppressWarnings("null")
    void upload() {
        var image = given()
            .header("X-User-Id", admin.id())
            .header("X-User-Roles", admin.roles())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
            .multiPart("file", FILE, "text/plain").when()
            .post("/files/upload?directory=/news")
            .then().statusCode(200).extract().body().as(FileEntity.class);

        given().when().get("/files/download?fileName=" + image.path).then()
            .statusCode(200);
    }

    @Test
    void find() {
        given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).when().get("/news/1").then()
            .statusCode(200);
        given()
            .header("X-User-Id", user.id())
            .header("X-User-Roles", user.roles()).when()
            .get("/news/findBy?fields=active&values=true").then()
            .statusCode(200);

    }
}
