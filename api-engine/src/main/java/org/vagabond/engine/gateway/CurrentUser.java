package org.vagabond.engine.gateway;

import java.util.Set;

public record CurrentUser(
    Long id,
    String username,
    String email,
    Set<String> roles) {

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}