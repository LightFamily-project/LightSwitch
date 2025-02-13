package com.lightswitch.presentation.model.flag

import java.time.Instant

data class FeatureFlagResponse(
    val key: String,
    val status: Boolean,
    val type: String,
    val defaultValue: Map<String, Any>,
    val description: String,
    val variants: List<Map<String, Any>>? = null,
    val createdBy: String,
    val updatedBy: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
