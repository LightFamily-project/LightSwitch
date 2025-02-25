package com.lightswitch.infrastructure.security

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description =
        "Represents the JWT token response containing both access and refresh tokens, " +
            "as well as the expiration date of the access token.",
)
data class JwtToken(
    @Schema(
        description = "The access token used to authenticate the user in subsequent requests. This token is valid for 30 minutes.",
    )
    val accessToken: String? = null,
    @Schema(
        description = "The refresh token used to obtain a new access token once it expires. This token is valid for 7 days.",
    )
    val refreshToken: String? = null,
    @Schema(
        description = "The expiration date (timestamp in milliseconds) of the access token. The token becomes invalid after this time.",
    )
    val accessTokenExpiredDate: Long? = null,
)
