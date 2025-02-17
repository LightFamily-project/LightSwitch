package com.lightswitch.presentation.controller

import com.lightswitch.application.service.FeatureFlagService
import com.lightswitch.infrastructure.database.repository.UserRepository
import com.lightswitch.presentation.exception.BusinessException
import com.lightswitch.presentation.model.PayloadResponse
import com.lightswitch.presentation.model.StatusResponse
import com.lightswitch.presentation.model.flag.CreateFeatureFlagRequest
import com.lightswitch.presentation.model.flag.FeatureFlagResponse
import com.lightswitch.presentation.model.flag.UpdateFeatureFlagRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/flags")
class FeatureFlagController(
    private val featureFlagService: FeatureFlagService,
    private val userRepository: UserRepository,
) {

    @Operation(
        summary = "Retrieve all feature flags",
    )
    @GetMapping
    fun getFlags(): PayloadResponse<List<FeatureFlagResponse>> {
        return PayloadResponse<List<FeatureFlagResponse>>(
            status = "status",
            message = "message",
            data = listOf()
        )
    }

    @Operation(
        summary = "Retrieve a specific feature flag by key",
    )
    @GetMapping("/{key}")
    fun getFlag(
        @PathVariable key: String,
    ): PayloadResponse<FeatureFlagResponse> {
        // TODO: Improve the way finding the authenticated user.
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw BusinessException("User not found")

        val flag = featureFlagService.getFlagOrThrow(key)

        return PayloadResponse.success(
            message = "Fetched a flag successfully",
            data = FeatureFlagResponse.from(flag)
        )
    }

    @Operation(
        summary = "Create a new feature flag",
    )
    @PostMapping
    fun createFlag(
        @RequestBody @Valid request: CreateFeatureFlagRequest,
    ): PayloadResponse<FeatureFlagResponse> {
        // TODO: Improve the way finding the authenticated user.
        val authentication = SecurityContextHolder.getContext().authentication
        val userId = authentication.name
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw BusinessException("User not found")

        val flag = featureFlagService.create(user, request)

        return PayloadResponse.success(
            message = "Created flag successfully",
            data = FeatureFlagResponse.from(flag)
        )
    }

    @Operation(
        summary = "Update an existing feature flag",
    )
    @PutMapping("/{key}")
    fun updateFlag(
        @PathVariable key: String,
        @RequestBody request: UpdateFeatureFlagRequest,
    ): PayloadResponse<FeatureFlagResponse> {
        return PayloadResponse<FeatureFlagResponse>(
            status = "status",
            message = "message",
            data = null
        )
    }

    @Operation(
        summary = "Update the status of a feature flag",
    )
    @PatchMapping("/{key}")
    fun updateFlagStatus(
        @PathVariable key: String,
        @RequestParam status: String,
    ): PayloadResponse<FeatureFlagResponse> {
        return PayloadResponse<FeatureFlagResponse>(
            status = "status",
            message = "message",
            data = null
        )
    }

    @Operation(
        summary = "Delete a feature flag",
    )
    @DeleteMapping("/{key}")
    fun deleteFlag(
        @PathVariable key: String,
    ): StatusResponse {
        return StatusResponse(
            status = "status",
            message = "message",
        )
    }
}
