package com.lightswitch.presentation.controller

import com.lightswitch.presentation.model.PayloadResponse
import com.lightswitch.presentation.model.StatusResponse
import com.lightswitch.presentation.model.sdk.SdkKeyResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/sdks")
class SdkController {
    @Operation(
        summary = "Create SDK key",
    )
    @PostMapping
    fun createKey(): PayloadResponse<SdkKeyResponse> {
        return PayloadResponse<SdkKeyResponse>(
            status = "status",
            message = "message",
            data = null
        )
    }

    @Operation(
        summary = "Get SDK key",
    )
    @GetMapping
    fun getKey(): PayloadResponse<SdkKeyResponse> {
        return PayloadResponse<SdkKeyResponse>(
            status = "status",
            message = "message",
            data = null
        )
    }

    @Operation(
        summary = "Connect to SSE",
    )
    @GetMapping("/sse-connect")
    fun connectSse(): StatusResponse {
        return StatusResponse(
            status = "status",
            message = "message"
        )
    }
}
