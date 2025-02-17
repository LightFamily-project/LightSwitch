package com.lightswitch.infrastructure.security

data class JwtToken(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiredDate: Long? = null
)
