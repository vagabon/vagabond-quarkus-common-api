package org.vagabond.utils;

import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.common.profile.ProfileService;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.engine.auth.utils.AuthUtils;
import org.vagabond.engine.exeption.MetierException;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;

public abstract class BaseDataTest {

    @Inject
    protected ProfileService profileService;
    @Inject
    protected UserService userService;

    protected ProfileEntity profileAdmin;
    protected ProfileEntity profileUser;
    protected ProfileEntity profilePremium;

    protected UserEntity admin;
    protected UserEntity user;

    @WithTransaction
    @BeforeEach
    public void prepareTest() {
        profileAdmin = getProfile("ADMIN", "ADMIN, USER");
        profileUser = getProfile("USER", "USER");
        profilePremium = getProfile("PREMIUM", "USER,PREMIUM");

        admin = getUser("admin", Arrays.asList(profileAdmin, profileUser));
        user = getUser("user", Arrays.asList(profileUser));
    }

    private ProfileEntity getProfile(String name, String roles) {
        var profiles = profileService.findBy("WHERE name = ?1 ", name).await().indefinitely();
        if (profiles.isEmpty()) {
            var profile = new ProfileEntity();
            profile.active = true;
            profile.name = name;
            profile.roles = roles;
            return profileService.persist(profile);
        }
        return profiles.get(0);
    }

    private UserEntity getUser(String username, List<ProfileEntity> profiles) {
        try {
            return userService.findByUsername(username);

        } catch (MetierException exception) {
            var newUser = new UserEntity();
            newUser.active = true;
            newUser.username = username;
            newUser.password = AuthUtils.encrypePassword("password");
            newUser.profiles = profiles;
            return userService.persist(newUser);
        }
    }

}
