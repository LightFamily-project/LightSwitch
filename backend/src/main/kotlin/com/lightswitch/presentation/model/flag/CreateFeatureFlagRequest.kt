package com.lightswitch.presentation.model.flag

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class CreateFeatureFlagRequest(
    @field:NotBlank(message = "Key is required.")
    val key: String,
    @field:NotNull(message = "Status is required.")
    val status: Boolean,
    @field:NotBlank(message = "Type is required.")
    @field:Pattern(regexp = "(?i)^(number|boolean|string)$", message = "Type must be one of: number, boolean, string")
    val type: String,
    @field:NotEmpty(message = "Default value is required.")
    val defaultValue: Map<String, Any>,
    @field:NotBlank(message = "Description is required.")
    val description: String,
    val variants: List<Map<String, Any>>? = null,
) {
    fun defaultValueAsPair(): Pair<String, Any> = defaultValue.entries.first().toPair()
    fun variantPairs(): List<Pair<String, Any>>? = variants?.map { it.entries.first().toPair() }
}
