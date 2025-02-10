package com.lightswitch.infrastructure.security.jwt

data class JwtToken(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiredDate: Long? = null
)
