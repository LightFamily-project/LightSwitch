package com.lightswitch.presentation.model.flag

import com.lightswitch.infrastructure.database.entity.FeatureFlag
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
) {
    companion object {
        fun from(flag: FeatureFlag): FeatureFlagResponse {
            return FeatureFlagResponse(
                key = flag.name,
                status = flag.enabled,
                type = flag.type.name,
                defaultValue = mapOf(flag.defaultCondition!!.key to flag.defaultCondition!!.value),
                description = flag.description,
                variants = flag.conditions.map { mapOf(it.key to it.value) },
                createdBy = flag.createdBy.username,
                updatedBy = flag.updatedBy.username,
                createdAt = flag.createdAt,
                updatedAt = flag.updatedAt,
            )
        }
    }
}
