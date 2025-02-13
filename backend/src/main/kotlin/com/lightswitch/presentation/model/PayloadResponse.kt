package com.lightswitch.presentation.model

data class PayloadResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null,
)
