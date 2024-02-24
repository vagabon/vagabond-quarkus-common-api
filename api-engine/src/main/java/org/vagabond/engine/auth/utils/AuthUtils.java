package org.vagabond.engine.auth.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.wildfly.security.password.interfaces.BCryptPassword;

import io.quarkus.elytron.security.common.BcryptUtil;

public class AuthUtils {

    private static final int ENCRYPTE_COUNT = 10;

    private static final Random rand = new SecureRandom();

    private AuthUtils() {}

    public static final String encrypePassword(String password) {
        var salt = new byte[BCryptPassword.BCRYPT_SALT_SIZE];
        var random = new SecureRandom();
        random.nextBytes(salt);
        return BcryptUtil.bcryptHash(password, ENCRYPTE_COUNT, salt);
    }

    public static final String generateIdentityToken() {
        var identityToken = new StringBuilder();
        for (var i = 0; i < 6; i++) {
            identityToken.append(rand.nextInt(10) + 0);
        }
        return identityToken.toString();
    }
}
