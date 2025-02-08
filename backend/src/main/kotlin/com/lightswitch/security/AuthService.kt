package com.lightswitch.security

import com.lightswitch.exception.BusinessException
import com.lightswitch.infrastructure.database.entity.RefreshToken
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.repository.RefreshTokenRepository
import com.lightswitch.infrastructure.database.repository.UserRepository
import com.lightswitch.security.jwt.JwtToken
import com.lightswitch.security.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    val userRepository: UserRepository,
    val refreshTokenRepository: RefreshTokenRepository,
    val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun login(username: String, password: String): JwtToken {
        val user = userRepository.findByUsername(username)
            ?: throw BusinessException("User with username $username not found")

        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw BusinessException("Password is incorrect")
        }

        user.lastLoginAt = LocalDateTime.now()
        userRepository.save(user)
        val jwtToken = jwtTokenProvider.generateJwtToken(user.id!!, user)
        refreshTokenRepository.save(RefreshToken(user.id!!, jwtToken.refreshToken!!))

        return jwtToken
    }

    @Transactional
    fun signup(username: String, password: String): User {
        if (userRepository.existsByUsername(username)) {
            throw BusinessException("Username already exists")
        }
        val passwordHash = passwordEncoder.encode(password)

        val newUser = User(
            username = username,
            passwordHash = passwordHash
        )
        return userRepository.save(newUser)
    }

    @Transactional
    fun reissue(jwtToken: String, userId: Long): JwtToken? {
        val refreshToken: RefreshToken = refreshTokenRepository.findById(userId)
            .orElseThrow { BusinessException("Log-out user") }

        if (refreshToken.value != jwtToken) {
            throw BusinessException("Refresh Token is Not Valid")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException("User not found") }

        return when {
            jwtTokenProvider.isRefreshTokenRenewalRequired(refreshToken.value) -> {
                jwtTokenProvider.generateJwtToken(userId, user).also {
                    refreshToken.value = it.refreshToken.toString()
                }
            }
            else -> {
                jwtTokenProvider.generateJwtAccessToken(userId, user, Date(), refreshToken.value)
            }
        }
    }

    @Transactional
    fun logout(userId: Long) {
        refreshTokenRepository.deleteById(userId)
    }
}