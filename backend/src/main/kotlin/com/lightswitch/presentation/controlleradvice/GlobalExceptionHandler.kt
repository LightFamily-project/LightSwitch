package com.lightswitch.presentation.controlleradvice

import com.lightswitch.presentation.exception.BusinessException
import com.lightswitch.presentation.model.StatusResponse
import com.lightswitch.util.logging.logger
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = logger()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                StatusResponse(
                    status = HttpStatus.BAD_REQUEST.name,
                    message = ex.message ?: "Invalid request",
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                StatusResponse(
                    status = HttpStatus.BAD_REQUEST.name,
                    message = ex.message,
                )
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                StatusResponse(
                    status = HttpStatus.BAD_REQUEST.name,
                    message = ex.message ?: "Malformed JSON request",
                )
            )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                StatusResponse(
                    status = HttpStatus.BAD_REQUEST.name,
                    message = ex.message ?: "Entity not found",
                )
            )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                StatusResponse(
                    status = HttpStatus.UNAUTHORIZED.name,
                    message = "Invalid username or password",
                )
            )
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                StatusResponse(
                    status = HttpStatus.UNAUTHORIZED.name,
                    message = ex.message ?: "Authentication failed",
                )
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                StatusResponse(
                    status = HttpStatus.FORBIDDEN.name,
                    message = ex.message ?: "Authorization failed",
                )
            )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                StatusResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.name,
                    message = ex.message ?: "Business error occurred",
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<StatusResponse> {
        log.error(ex.message, ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                StatusResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.name,
                    message = "Unexpected error: ${ex.message}",
                )
            )
    }
}
