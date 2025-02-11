package com.lightswitch.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Represents a user's account credentials including username and password.")
data class UserAccount(
    @NotNull @Schema(description = "The username of the user.") val username: String,
    @NotNull @Schema(description = "The password of the user.")val password: String
)