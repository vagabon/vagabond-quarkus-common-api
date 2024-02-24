package org.vagabond.common.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.profile.ProfileRepository;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserRepository;
import org.vagabond.engine.auth.payload.request.AuthRequest;

@QuarkusTest
class AuthResourceMockTest {

    @InjectMock
    EntityManager entityManager;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    ProfileRepository profileRepository;

    @Inject
    AuthResource authResource;

    @Test
    void testSignin() {

        var user = new UserEntity();
        user.id = 1L;
        user.username = "test";
        user.password = "test";
        user.connectionTrials = 0;
        user.emailActivation = true;
        Mockito.when(userRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(userRepository.findBy("username", "test")).thenReturn(user);
        var profile = new ProfileEntity();
        profile.id = 1L;
        profile.name = "USER";
        Mockito.when(profileRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(profileRepository.findBy("name", "USER")).thenReturn(profile);
        MockedStatic<BcryptUtil> mockedStatic = Mockito.mockStatic(BcryptUtil.class);
        mockedStatic.when(() -> BcryptUtil.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        Response response = authResource.signin(new AuthRequest("test", "test"));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
}
