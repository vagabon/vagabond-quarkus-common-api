package org.vagabond.common.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.BooleanUtils;
import org.vagabond.common.auth.payload.response.FacebookResponse;
import org.vagabond.common.auth.payload.response.GoogleResponse;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.profile.ProfileRepository;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserRepository;
import org.vagabond.engine.auth.service.BaseAuthService;
import org.vagabond.engine.auth.utils.AuthUtils;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class AuthService extends BaseAuthService<UserEntity, ProfileEntity> {

    @Inject
    UserRepository userRepository;

    @Inject
    ProfileRepository profileRepository;

    @Inject
    AuthEmailService authEmailService;

    @Override
    public void doBeforeSignin(UserEntity user) {
        if (!BooleanUtils.isTrue(user.emailActivation)) {
            throw new MetierException("AUTH:ERROR.EMAIL_NOT_ACTIVATED");
        }
    }

    @Override
    public void doBeforeSignUp(UserEntity user) {
        user.activationToken = UUID.randomUUID().toString();
        user.emailActivation = false;

        var profiles = user.profiles;
        if (profiles == null || profiles.isEmpty()) {
            ProfileEntity userRole = profileRepository.getProfileUser();
            user.profiles = new ArrayList<>(Arrays.asList(userRole));
        }
    }

    @Override
    public void doAfterSignUp(UserEntity user) {
        authEmailService.sendCreationMail(user);
    }

    @Override
    public BaseRepository<ProfileEntity> getProfileRepository() {
        return profileRepository;
    }

    @Override
    public BaseRepository<UserEntity> getRepository() {
        return userRepository;
    }

    @Transactional
    public UserEntity activationUser(String token) {
        var user = userRepository.findByOneField("activationToken", token);
        user.emailActivation = true;
        user.activationToken = "";
        persist(user);
        return user;
    }

    public UserEntity createIdentityToken(String email) {
        var user = addIdentityToken(email);
        authEmailService.sendIdentityTokenMail(user);
        return user;
    }

    @Transactional
    protected UserEntity addIdentityToken(String email) {
        var user = userRepository.findByOneField("email", email);
        user.identityToken = AuthUtils.generateIdentityToken();
        var now = LocalDateTime.now();
        user.identityTokenDateEnd = now.plus(10, ChronoUnit.MINUTES);
        persist(user);
        return user;
    }

    @Transactional
    public String checkIdentityToken(String token) {
        var user = userRepository.getUserFromIdentityToken(token);
        return user.identityToken;
    }

    public UserEntity resetPassword(String token) {
        var user = userRepository.getUserFromIdentityToken(token);
        String newPassword = UUID.randomUUID().toString();
        user.password = AuthUtils.encrypePassword(newPassword);
        resetIdentityToken(user);
        authEmailService.sendResetPassword(user, newPassword);
        return user;
    }

    @Transactional
    public void resetIdentityToken(UserEntity user) {
        user.identityToken = "";
        persist(user);
    }

    @Transactional
    public UserEntity googleConnect(GoogleResponse googleResponse) {
        UserEntity user = userRepository.findByOneField("googleId", googleResponse.id);
        if (user == null) {
            return saveNewUser(googleResponse.id, null, googleResponse.name, googleResponse.email, googleResponse.picture);
        } else if (user.avatar == null) {
            user.avatar = googleResponse.picture;
            persist(user);
        }
        return user;
    }

    @Transactional
    public UserEntity facebookConnect(FacebookResponse facebookResponse) {
        UserEntity user = userRepository.findByOneField("facebookId", facebookResponse.id());
        if (user == null) {
            return saveNewUser(null, facebookResponse.id(), facebookResponse.name(), facebookResponse.email(),
                    facebookResponse.picture().data().url());
        }
        return user;
    }

    private UserEntity saveNewUser(String googleId, String facebookId, String name, String email, String avatar) {
        UserEntity googleUser = new UserEntity();
        googleUser.googleId = googleId;
        googleUser.facebookId = facebookId;
        googleUser.username = name;
        googleUser.email = email;
        googleUser.emailActivation = true;
        googleUser.avatar = avatar;
        ProfileEntity userProfile = profileRepository.getProfileUser();
        if (userProfile != null) {
            googleUser.profiles = new ArrayList<>(Arrays.asList(userProfile));
        }
        persist(googleUser);
        return googleUser;
    }

}
