package org.vagabond.common.api.auth;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.profile.ProfileRepository;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserRepository;
import org.vagabond.engine.auth.payload.request.AuthRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.InjectMock;

class AuthResourceMockTest {

    @InjectMock
    EntityManager entityManager;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    ProfileRepository profileRepository;

    @Inject
    AuthResource authResource;

    void testSignin() {

        var user = new UserEntity();
        user.id = 1L;
        user.username = "test";
        user.password = "test";
        user.connectionTrials = 0;
        user.emailActivation = true;
        Mockito.when(userRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(userRepository.findByOneField("username", "test")).thenReturn(user);
        var profile = new ProfileEntity();
        profile.id = 1L;
        profile.name = "USER";
        Mockito.when(profileRepository.getEntityManager()).thenReturn(entityManager);
        Mockito.when(profileRepository.findByOneField("name", "USER")).thenReturn(profile);
        MockedStatic<BcryptUtil> mockedStatic = Mockito.mockStatic(BcryptUtil.class);
        mockedStatic.when(() -> BcryptUtil.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        Response response = authResource.signin(new AuthRequest("test", "test"));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
}
