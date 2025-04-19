package org.vagabond.engine.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.vagabond.engine.auth.entity.BaseProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.auth.utils.AuthUtils;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.exeption.MetierException;
import org.vagabond.engine.exeption.TechnicalException;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;

public abstract class BaseAuthService<T extends BaseUserEntity<P>, P extends BaseProfileEntity> extends BaseService<T> {

    public static final String LOGIN_ERROR = "AUTH:ERROR.LOGIN_ERROR";
    public static final String ATTEMPT_TOO_SOON = "AUTH:ERROR.ATTEMPT_TOO_SOON";
    private static final String USERNAME = "username";

    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "JWT_ISUSSER")
    public String issuer;

    @ConfigProperty(name = "mp.jwt.duration", defaultValue = "10000")
    public Long duration;

    @ConfigProperty(name = "auth.attemp-max", defaultValue = "5")
    public int attemptMax;

    @ConfigProperty(name = "auth.waiting-time", defaultValue = "10")
    public int waitingTime;

    public String generateTokenJwt(T user) {
        return generateTokenJwt(user, duration);
    }

    public String generateTokenJwt(T user, Long customDuration) {
        var profiles = user.getProfiles();
        if (user.getProfiles() == null || user.getProfiles().isEmpty()) {
            var userProfile = getProfileRepository().findByOneField("name", "USER");
            profiles = new ArrayList<>();
            profiles.add(userProfile);
            user.setProfiles(profiles);
        }
        return Jwt.issuer(issuer).upn(user.username).groups(profiles.stream().map(profile -> profile.name).collect(Collectors.toSet()))
                .expiresIn(customDuration).sign();
    }

    public T findByUsername(String username) {
        return getRepository().findByOneField(USERNAME, username);
    }

    public T signIn(String username, String password) {

        var user = getRepository().findByOneField(USERNAME, username);

        if (user == null) {
            throw new MetierException(LOGIN_ERROR);
        }

        if (!checkAttemptDelay(user, attemptMax, waitingTime)) {
            throw new MetierException(ATTEMPT_TOO_SOON);
        }

        try {
            if (!BcryptUtil.matches(password, user.password)) {
                doConnectionTrials(user);
                throw new MetierException("AUTH:ERROR.PASSWORD_ERROR");
            }
        } catch (MetierException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new TechnicalException("ERRORS:TECHNICAL_PASSWORD", exception);
        }

        doBeforeSignin(user);
        return resetConnectionTrials(user);
    }

    @Transactional
    public T doConnectionTrials(T user) {
        user.lastFailedTrialDate = LocalDateTime.now();
        var connectionTrials = user.connectionTrials != null ? user.connectionTrials : 0;
        user.connectionTrials = connectionTrials + 1;
        return getRepository().getEntityManager().merge(user);
    }

    @Transactional
    public T resetConnectionTrials(T user) {
        user.connectionTrials = 0;
        user.lastConnexionDate = LocalDateTime.now();
        return getRepository().getEntityManager().merge(user);
    }

    public abstract void doBeforeSignin(T user);

    private boolean checkAttemptDelay(T user, int attemptMax, int trialWaitingTime) {
        if (null == user.lastFailedTrialDate) {
            return true;
        }

        var trials = user.connectionTrials;
        if (null == trials) {
            trials = 0;
        }
        var waitingDelayTime = LocalDateTime.now().minus(trialWaitingTime, ChronoUnit.MINUTES);

        var tooManyAttempts = trials >= attemptMax;
        var isAttemptTooSoon = waitingDelayTime.isBefore(user.lastFailedTrialDate);

        if (tooManyAttempts && !isAttemptTooSoon) {
            user.connectionTrials = 0;
            user.lastFailedTrialDate = null;
        }
        return !(tooManyAttempts && isAttemptTooSoon);
    }

    public T signup(T user) {
        if (getRepository().existBy(USERNAME, user.username)) {
            throw new MetierException("ERRORS:USERNAME_ALREADY_EXIST");
        }

        if (getRepository().existBy("email", user.email) && !user.email.equals("gonzague.clement@gmail.com")) {
            throw new MetierException("ERRORS:EMAIL_ALREADY_EXIST");
        }

        user.password = AuthUtils.encrypePassword(user.password);

        doBeforeSignUp(user);
        user = persistUser(user);
        doAfterSignUp(user);

        return user;
    }

    @Transactional
    public T persistUser(T user) {
        return getRepository().getEntityManager().merge(user);
    }

    public abstract void doBeforeSignUp(T user);

    public abstract void doAfterSignUp(T user);

    public abstract BaseRepository<P> getProfileRepository();
}
