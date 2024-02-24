package org.vagabond.engine.auth.payload.response;

public record AuthResponse<T>(T user, String jwt, String jwtRefresh) {}
