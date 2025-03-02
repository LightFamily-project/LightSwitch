package com.lightswitch.presentation.model

data class StatusResponse(
    val status: String,
    val message: String,
) {
    companion object {
        fun success(message: String): StatusResponse {
            return StatusResponse("Success", message)
        }
    }
}
