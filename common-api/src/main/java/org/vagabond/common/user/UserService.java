package org.vagabond.common.user;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.vagabond.common.auth.service.AuthEmailService;
import org.vagabond.common.profile.ProfileRepository;
import org.vagabond.common.user.payload.UserResponse;
import org.vagabond.engine.auth.utils.AuthUtils;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.crud.utils.QueryUtils;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.mapper.MapperUtils;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class UserService extends BaseService<UserEntity> {

    public static final String USERNAME = "username";

    @Inject
    private UserRepository userRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    AuthEmailService authEmailService;

    @Override
    public BaseRepository<UserEntity> getRepository() {
        return userRepository;
    }

    public UserEntity findByUsername(String name) {
        return userRepository.findByOrThrow(USERNAME, name);
    }

    @Override
    public void doBeforeCreate(UserEntity entity) {
        entity.password = AuthUtils.encrypePassword(entity.password);
    }

    @Override
    public void doBeforeMerge(UserEntity entity, UserEntity entityNew) {
        try {
            if (!entity.password.equals(entityNew.password) && !BcryptUtil.matches(entity.password, entityNew.password)) {
                entity.password = AuthUtils.encrypePassword(entity.password);
            } else {
                entity.password = entityNew.password;
            }
        } catch (RuntimeException exception) {
            throw new MetierException("AUTH:ERROR.PASSWORD_ERROR", exception);
        }
    }

    @Transactional
    public UserEntity updateEmail(Long userId, String email) {
        var user = userRepository.findById(userId);
        user.email = email;
        user.emailActivation = false;
        user.activationToken = UUID.randomUUID().toString();
        userRepository.getEntityManager().merge(user);
        authEmailService.sendCreationMail(user);
        return user;
    }

    @Transactional
    public UserEntity updatePassword(Long userId, String password, String newPassword) {
        var user = userRepository.findById(userId);
        if (!BcryptUtil.matches(password, user.password)) {
            throw new MetierException("AUTH:ERROR.PASSWORD_ERROR");
        }
        user.password = AuthUtils.encrypePassword(newPassword);
        userRepository.getEntityManager().merge(user);
        return user;
    }

    @Transactional
    public void addProfileToUser(UserEntity user, String profileName) {
        var profileCreator = profileRepository.findByOneField("name", profileName);
        var profiles = user.getProfiles();
        var profileCreatorFind = profiles.stream().filter(profile -> profileName.equals(profile.name)).count();
        if (profileCreatorFind == 0) {
            user.getProfiles().add(profileCreator);
        }
        persist(user);
    }

    public List<UserResponse> findTop50(String username) {
        var query = userRepository.find("where UPPER(username) like ?1 order by username", QueryUtils.getLike(username));
        var users = query.page(Page.of(0, 50)).list();
        return MapperUtils.toList(users, UserResponse.class);
    }
}
