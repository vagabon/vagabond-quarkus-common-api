package org.vagabond.engine.auth.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.wildfly.security.password.interfaces.BCryptPassword;

import io.quarkus.elytron.security.common.BcryptUtil;

public class AuthUtils {

    private static final int ENCRYPTE_COUNT = 10;

    private static final Random random = new SecureRandom();

    private AuthUtils() {
    }

    public static final String encrypePassword(String password) {
        var salt = new byte[BCryptPassword.BCRYPT_SALT_SIZE];
        random.nextBytes(salt);
        return BcryptUtil.bcryptHash(password, ENCRYPTE_COUNT, salt);
    }

    public static final String generateIdentityToken() {
        return generatToken(6);
    }

    public static final String generatToken(int nb) {
        var identityToken = new StringBuilder();
        for (var i = 0; i < nb; i++) {
            identityToken.append(random.nextInt(10) + 0);
        }
        return identityToken.toString();
    }

    public static final String getUsername(String login) {
        if (StringUtils.isEmpty(login)) {
            return null;
        }
        return login.trim().replace(" ", "_").toLowerCase() + "#" + AuthUtils.generatToken(4);
    }
}
