package com.lightswitch.presentation.controller

import com.lightswitch.infrastructure.database.model.SdkType
import com.lightswitch.presentation.model.PayloadResponse
import com.lightswitch.presentation.model.StatusResponse
import com.lightswitch.presentation.model.sdk.CreateSdkKeyRequest
import com.lightswitch.presentation.model.sdk.SdkKeyResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/sdks")
class SdkController {
    @Operation(
        summary = "Create SDK key",
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createKey(
        @RequestBody request: CreateSdkKeyRequest,
    ): PayloadResponse<SdkKeyResponse> {
        return PayloadResponse<SdkKeyResponse>(
            status = "status",
            message = "message",
            data = null,
        )
    }

    @Operation(
        summary = "Get SDK key",
    )
    @GetMapping
    fun getKey(
        @RequestParam sdkType: String,
    ): PayloadResponse<SdkKeyResponse> {
        checkSdkType(sdkType)
        return PayloadResponse<SdkKeyResponse>(
            status = "status",
            message = "message",
            data = null,
        )
    }

    @Operation(
        summary = "Connect to SSE",
    )
    @GetMapping("/sse-connect")
    fun connectSse(
        @RequestParam sdkKey: String,
    ): StatusResponse {
        return StatusResponse(
            status = "status",
            message = "message",
        )
    }

    fun checkSdkType(sdkType: String) {
        SdkType.from(sdkType)
    }
}
