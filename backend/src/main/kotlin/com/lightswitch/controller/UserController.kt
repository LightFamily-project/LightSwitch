package com.lightswitch.controller

import com.lightswitch.application.service.AuthService
import com.lightswitch.controller.request.UserAccount
import com.lightswitch.infrastructure.security.JwtToken
import com.lightswitch.presentation.model.PayloadResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val authService: AuthService) {
    @Operation(
        summary = "Login the user",
        description = "Authenticates a user with their username and password, and returns a JWT token."
    )
    @PostMapping("/login")
    fun userLogin(@RequestBody @Valid body: UserAccount): PayloadResponse<JwtToken> {
        val token = authService.login(body.username, body.password)
        return PayloadResponse(
            "Success",
            message = "User Login Success",
            data = token
        )
    }

    @Operation(
        summary = "Initialize user account",
        description = "Registers a new user with the provided username and password. Returns a confirmation message."
    )
    @PostMapping("/initialize")
    fun userInitialize(@RequestBody @Valid body: UserAccount): PayloadResponse<String> {
        val user = authService.signup(body.username, body.password)
        return PayloadResponse(
            "Success",
            message = "Signup New User Success",
            data = user.username
        )
    }

    @Operation(
        summary = "Refresh authentication token",
        description = "Reissues a new JWT token using the provided refresh token and current user's identity."
    )
    @PutMapping("/auth/refresh")
    fun refreshUserToken(@RequestHeader("Authorization") @NotEmpty @Parameter(description = "The refresh token prefixed with 'Bearer '.") refreshToken: String): PayloadResponse<JwtToken> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val token = authService.reissue(refreshToken.removePrefix("Bearer "), authentication.name.toLong())
        return PayloadResponse("Success", "Refresh Token Success", token)
    }

    @Operation(
        summary = "Logout the user",
        description = "Logs out the user by invalidating their access token. Requires the user to provide their token."
    )
    @PostMapping("/logout")
    fun userLogout(
        @RequestHeader("Authorization") @NotEmpty @Parameter(description = "The access token prefixed with 'Bearer '.") accessToken: String,
        @RequestBody @Valid body: UserAccount,
    ): PayloadResponse<String> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name
        authService.logout(userId.toLong())
        return PayloadResponse("Success", "Logout Success", body.username)
    }
}
