package com.geoparty.spring_boot.security.jwt;

public enum JWTValType {
    VALID_JWT,
    INVALID_JWT_SIGNATURE,
    INVALID_JWT_TOKEN,
    EXPIRED_JWT_TOKEN,
    UNSUPPORTED_JWT_TOKEN,
    EMPTY_JWT
}
