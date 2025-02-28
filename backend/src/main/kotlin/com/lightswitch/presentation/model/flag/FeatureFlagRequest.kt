package com.lightswitch.presentation.model.flag

interface FeatureFlagRequest {
    val defaultValue: Map<String, Any>
    val variants: List<Map<String, Any>>?
}

fun FeatureFlagRequest.defaultValueAsPair(): Pair<String, Any> = defaultValue.entries.first().toPair()

fun FeatureFlagRequest.variantPairs(): List<Pair<String, Any>>? = variants?.map { it.entries.first().toPair() }
