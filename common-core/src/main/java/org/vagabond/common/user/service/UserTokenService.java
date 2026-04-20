package org.vagabond.common.user.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.common.user.entity.UserEntity;
import org.vagabond.common.user.entity.UserTokenEntity;
import org.vagabond.common.user.repository.UserTokenRepository;
import org.vagabond.engine.auth.BaseAuthResource;
import org.vagabond.engine.auth.service.BaseAuthTokenService;
import org.vagabond.engine.exeption.MetierException;

import lombok.Getter;

@ApplicationScoped
public class UserTokenService extends BaseAuthTokenService<UserTokenEntity, UserEntity> {

    private static final String ENCRYPT_ALGORITHM = "SHA-256";

    @ConfigProperty(name = "refresh.token.ttl")
    @Getter
    private int refreshTokenTtl;

    @Inject
    @Getter
    private UserTokenRepository repository;

    @Override
    @Transactional
    public String generateRefreshToken(UserEntity user) {
        var userToken = new UserTokenEntity();
        userToken.user = user;
        var raw = UUID.randomUUID().toString() + System.nanoTime();
        try {
            var digest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            var hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            userToken.token = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            userToken.ipAddress = "";
            userToken.revoked = false;
            userToken.expiredDate = LocalDateTime.now().plusSeconds(refreshTokenTtl);
            repository.persist(userToken);
            return userToken.token;
        } catch (NoSuchAlgorithmException _) {
            throw new MetierException("REFRESH_HASH_ERROR");
        }
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        repository.deleteRefreshToken(refreshToken);
    }

    @Override
    public UserEntity findUserByRefreshToken(String refreshToken) {
        var userToken = repository.findByToken(refreshToken);
        if (userToken == null) {
            throw new MetierException(BaseAuthResource.REFRESH_TOKEN_ERROR);
        }
        return userToken.user;
    }

    @Transactional
    public Long deleteExpiredTokens() {
        return repository.deleteExpiredTokens();
    }
}
