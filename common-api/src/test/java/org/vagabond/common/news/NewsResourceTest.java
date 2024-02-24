package org.vagabond.common.news;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.vagabond.utils.BaseDataTest;

@QuarkusTest
class NewsResourceTest extends BaseDataTest {

    @Test
    @TestSecurity(user = "admin")
    void crud() {
        var newsRequest = new NewsEntity();
        newsRequest.description = "description";
        var news = given().body(newsRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/news").then()
                .statusCode(200).extract().body().as(NewsEntity.class);

        given().when().get("/news/" + news.id).then().statusCode(200);

        newsRequest.id = news.id;
        given().body(news).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().put("/news").then().statusCode(200);
    }

    private final File FILE = new File("./src/test/resources/application.properties");

    @Test
    @TestSecurity(user = "admin")
    void upload() {
        var image = given().header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA).multiPart("file", FILE, "text/plain").when()
                .post("/news/upload?id=1").then().statusCode(200).extract().body().asString();

        given().when().get("/download?fileName=" + image).then().statusCode(200);
    }

    @Test
    @TestSecurity(user = "user")
    void find() {
        given().when().get("/news/1").then().statusCode(200);
        given().when().get("/news/findBy?fields=active&values=true").then().statusCode(200);

    }
}
