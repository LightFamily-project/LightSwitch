package com.lightswitch.presentation.model.flag

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class UpdateFeatureFlagRequest(
    @field:NotBlank(message = "Key is required.")
    val key: String,
    @field:NotBlank(message = "Type is required.")
    @field:Pattern(regexp = "(?i)^(number|boolean|string)$", message = "Type must be one of: number, boolean, string")
    val type: String,
    @field:NotEmpty(message = "Default value is required.")
    override val defaultValue: Map<String, Any>,
    @field:NotBlank(message = "Description is required.")
    val description: String,
    override val variants: List<Map<String, Any>>? = null,
) : FeatureFlagRequest
