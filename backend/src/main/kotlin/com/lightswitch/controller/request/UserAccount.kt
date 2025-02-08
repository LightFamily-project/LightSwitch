package com.lightswitch.controller.request

import jakarta.validation.constraints.NotNull

data class UserAccount(
    @NotNull val username: String,
    @NotNull val password: String
)