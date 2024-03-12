package org.vagabond.common.user.payload;

public record PasswordRequest(Long id, String password, String newPassword) {
}
