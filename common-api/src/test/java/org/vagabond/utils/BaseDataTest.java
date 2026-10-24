package org.vagabond.utils;

import org.junit.jupiter.api.BeforeEach;

public abstract class BaseDataTest {

    protected CurrentUserTest admin;
    protected CurrentUserTest user;

    @BeforeEach
    public void prepareTest() {

        admin = getUser(1L, "admin", "USER,ADMIN");
        user = getUser(2L, "user", "USER");
    }

    private CurrentUserTest getUser(Long id, String username, String profiles) {
        return new CurrentUserTest(id, username, profiles);
    }

    public record CurrentUserTest(
        Long id,
        String username,
        String roles) {
    }

}
