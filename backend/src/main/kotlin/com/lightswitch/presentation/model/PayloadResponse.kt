package com.lightswitch.presentation.model

data class PayloadResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null,
) {
    companion object {
        fun <T> success(message: String, data: T): PayloadResponse<T> {
            return PayloadResponse("Success", message, data)
        }
    }
}
