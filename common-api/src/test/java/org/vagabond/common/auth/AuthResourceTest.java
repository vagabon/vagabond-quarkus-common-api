package org.vagabond.common.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import com.google.common.net.HttpHeaders;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vagabond.common.auth.payload.request.ActivationRequest;
import org.vagabond.common.auth.payload.request.EmailRequest;
import org.vagabond.common.auth.payload.request.FacebookRequest;
import org.vagabond.common.auth.payload.request.GoogleRequest;
import org.vagabond.common.auth.payload.response.FacebookResponse;
import org.vagabond.common.auth.payload.response.FacebookResponsePicture;
import org.vagabond.common.auth.payload.response.FacebookResponsePictureData;
import org.vagabond.common.auth.payload.response.GoogleResponse;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.auth.payload.request.AuthRequest;
import org.vagabond.engine.auth.payload.request.RefreshTokenRequest;
import org.vagabond.engine.auth.payload.response.AuthResponse;
import org.vagabond.engine.http.HttpComponent;
import org.vagabond.utils.BaseDataTest;

@QuarkusTest
class AuthResourceTest extends BaseDataTest {

        @Test
        @TestSecurity(user = "admin", roles = { "ADMIN" })
        void auth() {

                // User SignUp
                var user = new UserEntity();
                user.username = "newUserAuth";
                user.password = "password";
                user.email = "test@gmail.com";

                var newUser = given().body(user).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signup")
                                .then().statusCode(200).extract().body().as(UserEntity.class);

                newUser = given().get("/user/" + newUser.id).then().statusCode(200).extract().body().as(UserEntity.class);

                // User Activation
                var activationRequest = new ActivationRequest(newUser.activationToken);
                given().body(activationRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/activation")
                                .then().statusCode(200);

                // User signin
                var authRequest = new AuthRequest("newUserAuth", "password");
                var authReponse = given().body(authRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/signin").then().statusCode(200).extract().body().as(AuthResponse.class);

                // User Refresh Token
                var RefreshTokenRequest = new RefreshTokenRequest(authReponse.jwtRefresh());
                given().body(RefreshTokenRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/refresh-token").then().statusCode(200);

                // User createIdentityToken
                var emailRequest = new EmailRequest(user.email);
                given().body(emailRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/createIdentityToken");
                newUser = given().get("/user/" + newUser.id).then().statusCode(200).extract().body().as(UserEntity.class);

                // User checkIdentityToken
                activationRequest = new ActivationRequest(newUser.identityToken);
                given().body(activationRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/checkIdentityToken").then().statusCode(200);

                // User createIdentityToken
                emailRequest = new EmailRequest(user.email);
                given().body(emailRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/createIdentityToken");
                newUser = given().get("/user/" + newUser.id).then().statusCode(200).extract().body().as(UserEntity.class);

                // User resetPassword
                activationRequest = new ActivationRequest(newUser.identityToken);
                given().body(activationRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/resetPassword").then().statusCode(200);
        }

        @Test
        void checkAttemps() {

                // User Creation
                var user = new UserEntity();
                user.username = "username2";
                user.password = "password";
                user.email = "test2@gmail.com";

                given().body(user).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signup").then()
                                .statusCode(200);

                given().body(user).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signup").then()
                                .statusCode(400).body("debugMessage", is("Username is already taken"));

                user.username = "username3";
                given().body(user).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signup").then()
                                .statusCode(400).body("debugMessage", is("Email is already in use"));

                // User : wrong username
                var authRequest = new AuthRequest("", "password");
                given().body(authRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signin").then()
                                .statusCode(400).body("debugMessage", is("AUTH:ERROR.LOGIN_ERROR"));

                // User : wrong password
                authRequest = new AuthRequest("username2", "password2");
                given().body(authRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signin").then()
                                .statusCode(400).body("debugMessage", is("AUTH:ERROR.PASSWORD_ERROR"));
                ;
                given().body(authRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/signin").then()
                                .statusCode(400).body("debugMessage", is("AUTH:ERROR.ATTEMPT_TOO_SOON"));
        }

        @InjectMock
        HttpComponent httpComponent;

        @Test
        @TestSecurity(user = "admin")
        void googleAndFacebookConnect() throws InterruptedException {

                var googleRequest = new GoogleRequest("token");
                var googleResponse = new GoogleResponse();
                googleResponse.email = "google@gmail.com";
                googleResponse.name = "googleName";
                googleResponse.id = "googleId";
                googleResponse.picture = "picture";
                Mockito.when(httpComponent.httpGet(Mockito.anyString(), Mockito.any())).thenReturn(googleResponse);
                given().body(googleRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when().post("/auth/google-connect")
                                .then().statusCode(200);

                var facebookRequest = new FacebookRequest("token");
                var pictureData = new FacebookResponsePictureData("url", 100, 100, false);
                var picture = new FacebookResponsePicture(pictureData);
                var facebookResponse = new FacebookResponse("facebookId", "facebook@gmail.com", "facebookName", picture);

                Mockito.when(httpComponent.httpGet(Mockito.anyString(), Mockito.any())).thenReturn(facebookResponse);
                given().body(facebookRequest).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).when()
                                .post("/auth/facebook-connect").then().statusCode(200);
        }
}
