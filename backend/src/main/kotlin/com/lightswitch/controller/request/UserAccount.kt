package com.lightswitch.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Represents a user's account credentials including username and password.")
data class UserAccount(
    @Schema(description = "The username of the user.")
    @field:NotBlank(message = "Username is required.")
    val username: String,
    @Schema(description = "The password of the user.")
    @field:NotBlank(message = "Password is required.")
    val password: String,
)
