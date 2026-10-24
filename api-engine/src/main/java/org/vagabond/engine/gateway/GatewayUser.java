package org.vagabond.engine.gateway;

import jakarta.enterprise.context.RequestScoped;

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