
package org.vagabond.common.user.service;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class UserTokenServiceTest {

    @Inject
    private UserTokenService userTokenService;

    @Test
    void test_cleanupExpiredTokens() {
        long nb = userTokenService.deleteExpiredTokens();
        assertEquals(0, nb);
    }
}
