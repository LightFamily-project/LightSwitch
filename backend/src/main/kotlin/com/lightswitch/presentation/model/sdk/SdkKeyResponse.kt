package com.lightswitch.presentation.model.sdk

import com.lightswitch.infrastructure.database.entity.SdkClient
import java.time.Instant

class SdkKeyResponse(
    val key: String,
    val type: String,
    val connectedAt: Instant,
) {
    companion object {
        fun from(sdk: SdkClient): SdkKeyResponse {
            return SdkKeyResponse(
                key = sdk.sdkKey,
                type = sdk.sdkType,
                connectedAt = sdk.connectedAt,
            )
        }
    }
}
