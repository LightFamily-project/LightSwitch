package com.lightswitch.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.lightswitch.application.service.FeatureFlagService
import com.lightswitch.infrastructure.database.entity.Condition
import com.lightswitch.infrastructure.database.entity.FeatureFlag
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.model.Type
import com.lightswitch.infrastructure.database.repository.UserRepository
import com.lightswitch.infrastructure.security.JwtTokenProvider
import com.lightswitch.presentation.model.flag.CreateFeatureFlagRequest
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.Test

@WebMvcTest(controllers = [FeatureFlagController::class])
@ExtendWith(MockitoExtension::class)
class FeatureFlagControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var featureFlagService: FeatureFlagService

    @MockitoBean
    private lateinit var userRepository: UserRepository

    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    @WithMockUser(username = "1")
    fun `should create feature flag successfully`() {
        val user = User(id = 1L, username = "username", passwordHash = "passwordHash")
        val request = CreateFeatureFlagRequest(
            key = "new-feature",
            status = true,
            type = "BOOLEAN",
            defaultValue = mapOf("default" to true),
            description = "Test feature flag",
        )
        val flag = FeatureFlag(
            id = 1L,
            name = "new-feature",
            description = "Test feature flag",
            type = Type.BOOLEAN,
            enabled = true,
            createdBy = user,
            updatedBy = user
        )
        val condition = Condition(id = 1L, key = "default", value = true, flag = flag)
        flag.defaultCondition = condition

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.create(user, request)).thenReturn(flag)

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Created flag successfully"))
            .andExpect(jsonPath("$.data.key").value("new-feature"))
            .andExpect(jsonPath("$.data.status").value(true))
            .andExpect(jsonPath("$.data.type").value("BOOLEAN"))
            .andExpect(jsonPath("$.data.defaultValue.default").value(true))
            .andExpect(jsonPath("$.data.description").value("Test feature flag"))
            .andExpect(jsonPath("$.data.createdBy").value("username"))
            .andExpect(jsonPath("$.data.updatedBy").value("username"))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `should return 400 when request validation fails due to empty key`() {
        val request = CreateFeatureFlagRequest(
            key = "",
            status = true,
            type = "boolean",
            defaultValue = mapOf("default" to true),
            description = "Test feature flag",
        )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `should return 400 when request validation fails due to empty type`() {
        val request = CreateFeatureFlagRequest(
            key = "new-feature",
            status = true,
            type = "",
            defaultValue = mapOf("default" to true),
            description = "Test feature flag",
        )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `should return 400 when request validation fails due to empty defaultValue`() {
        val request = CreateFeatureFlagRequest(
            key = "new-feature",
            status = true,
            type = "boolean",
            defaultValue = emptyMap(),
            description = "Test feature flag",
        )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `should return 400 when request validation fails due to empty description`() {
        val request = CreateFeatureFlagRequest(
            key = "new-feature",
            status = true,
            type = "boolean",
            defaultValue = mapOf("default" to true),
            description = "",
        )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `should return 400 when request validation fails due to invalid type`() {
        val request = CreateFeatureFlagRequest(
            key = "new-feature",
            status = true,
            type = "invalid-type",
            defaultValue = mapOf("default" to true),
            description = "",
        )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }
}
