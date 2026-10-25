package org.vagabond.engine.gateway.user;

import jakarta.enterprise.context.RequestScoped;

import org.vagabond.engine.user.CurrentUser;

@RequestScoped
public class GatewayUser {

    private CurrentUser user;

    public CurrentUser get() {
        return user;
    }

    public void set(CurrentUser user) {
        this.user = user;
    }
}