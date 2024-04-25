package org.vagabond.engine.crud.utils;

import java.util.ArrayList;
import java.util.List;

import org.vagabond.engine.auth.entity.BaseUserEntity;
import org.vagabond.engine.exeption.MetierException;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static void hasRole(String roles, List<String> groups) {
        if (!roles.isEmpty()) {
            if (groups == null) {
                groups = new ArrayList<>();
            }
            var simpleGrantedAuthorities = groups.stream().filter(authoritie -> {
                String[] roleSpits = roles.split(",");
                boolean find = false;
                for (String role : roleSpits) {
                    if (authoritie.equals(role)) {
                        find = true;
                        break;
                    }
                }
                return find;
            }).toList();
            if (simpleGrantedAuthorities.isEmpty()) {
                throw new MetierException("ACCES DENIED - ROLE(S) '" + roles + "' NEEDED");
            }
        }
    }

    public static <U extends BaseUserEntity<?>> boolean hasRole(U user, String role) {
        if (user != null && user.getProfiles() != null) {
            var profiles = user.getProfiles().stream().map(profile -> profile.name).toList();
            return profiles.contains(role);

        }
        return false;
    }
}
