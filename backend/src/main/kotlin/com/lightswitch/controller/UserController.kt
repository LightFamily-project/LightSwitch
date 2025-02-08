package com.lightswitch.controller

import com.lightswitch.controller.request.UserAccount
import com.lightswitch.exception.BusinessException
import com.lightswitch.security.AuthService
import com.lightswitch.security.jwt.JwtToken
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val authService: AuthService) {

    @PostMapping("/login")

    fun userLogin(@RequestBody @Valid body: UserAccount): ResponseEntity<JwtToken> {
        val token = authService.login(body.username, body.password)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/initialize")
    fun userInitialize(@RequestBody @Valid body: UserAccount): ResponseEntity<String> {
        return try {
            val user = authService.signup(body.username, body.password)
            ResponseEntity.ok("SignUp Completed: ${user.username}")
        } catch (e: BusinessException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PutMapping("/auth/refresh")
    fun refreshUserToken(@RequestHeader("Authorization") @NotEmpty refreshToken: String): ResponseEntity<JwtToken> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val token =
            authService.reissue(refreshToken.removePrefix("Bearer "), authentication.name.toLong())
        return ResponseEntity.ok(token)
    }

    @PostMapping("/logout")
    fun userLogout(
        @RequestHeader("Authorization") @NotEmpty accessToken: String,
        @RequestBody @Valid body: UserAccount
    ): ResponseEntity<String> {
        return try {
            val authentication: Authentication = SecurityContextHolder.getContext().authentication
            val userId = authentication.name
            authService.logout(userId.toLong())
            ResponseEntity.ok("Log out Completed: ${body.username}")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}