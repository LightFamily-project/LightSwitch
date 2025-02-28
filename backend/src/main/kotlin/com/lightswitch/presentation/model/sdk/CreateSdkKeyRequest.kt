package com.lightswitch.presentation.model.sdk

import jakarta.validation.constraints.Pattern

data class CreateSdkKeyRequest(
    @field:Pattern(regexp = "(?i)^(java|python)$", message = "Type must be one of: java, python")
    val type: String,
)
