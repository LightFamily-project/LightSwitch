package com.lightswitch.presentation.model.flag

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateFeatureFlagRequest(
    @field:NotBlank(message = "Key is required.")
    val key: String,
    @field:NotNull(message = "Status is required.")
    val status: Boolean,
    @field:NotNull(message = "Type is required.")
    val type: String,
    @field:NotEmpty(message = "Default value is required.")
    val defaultValue: Map<String, Any>,
    @field:NotBlank(message = "Description is required.")
    val description: String,
    val variants: List<Map<String, Any>>? = null,
    @field:NotBlank(message = "CreatedBy is required.")
    val createdBy: String,
)
