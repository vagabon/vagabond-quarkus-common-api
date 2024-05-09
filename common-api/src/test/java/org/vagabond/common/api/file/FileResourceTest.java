package org.vagabond.common.api.file;

import org.vagabond.utils.BaseDataTest;

import static io.restassured.RestAssured.given;

import io.quarkus.test.security.TestSecurity;

//@QuarkusTest
class FileResourceTest extends BaseDataTest {

    // @Test
    @TestSecurity(user = "user")
    void crud() {
        given().when().get("/file/download?fileName=/news/image.png").then().statusCode(200);
    }
}
