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
import com.lightswitch.presentation.model.flag.UpdateFeatureFlagRequest
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional
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

    private val user = User(id = 1L, username = "username", passwordHash = "passwordHash")
    private val flag =
        FeatureFlag(
            id = 1L,
            name = "test-flag",
            description = "Test Flag",
            type = Type.BOOLEAN,
            enabled = true,
            createdBy = user,
            updatedBy = user,
        )

    @BeforeEach
    fun setUp() {
        flag.addDefaultCondition("boolean", false)
        flag.addCondition("US", true)
        flag.addCondition("KR", true)
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should create feature flag successfully`() {
        val request =
            CreateFeatureFlagRequest(
                key = "test-flag",
                status = true,
                type = "BOOLEAN",
                defaultValue = mapOf("default" to true),
                description = "Test feature flag",
            )
        val condition = Condition(id = 1L, key = "default", value = true, flag = flag)
        flag.defaultCondition = condition

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.create(user, request)).thenReturn(flag)

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Created flag successfully"))
            .andExpect(jsonPath("$.data.key").value("test-flag"))
            .andExpect(jsonPath("$.data.status").value(true))
            .andExpect(jsonPath("$.data.type").value("BOOLEAN"))
            .andExpect(jsonPath("$.data.defaultValue.default").value(true))
            .andExpect(jsonPath("$.data.description").value("Test Flag"))
            .andExpect(jsonPath("$.data.createdBy").value("username"))
            .andExpect(jsonPath("$.data.updatedBy").value("username"))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should return 400 when request validation fails due to empty key`() {
        val request =
            CreateFeatureFlagRequest(
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
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should return 400 when request validation fails due to empty type`() {
        val request =
            CreateFeatureFlagRequest(
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
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should return 400 when request validation fails due to empty defaultValue`() {
        val request =
            CreateFeatureFlagRequest(
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
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Default value is required."))
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should return 400 when request validation fails due to empty description`() {
        val request =
            CreateFeatureFlagRequest(
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
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Description is required."))
    }

    @Test
    @WithMockUser(username = "1")
    fun `POST flags should return 400 when request validation fails due to invalid type`() {
        val request =
            CreateFeatureFlagRequest(
                key = "new-feature",
                status = true,
                type = "invalid-type",
                defaultValue = mapOf("default" to true),
                description = "description",
            )

        mockMvc.perform(
            post("/api/v1/flags")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Type must be one of: number, boolean, string"))
    }

    @Test
    @WithMockUser(username = "1")
    fun `GET flags should get all feature flags successfully`() {
        `when`(featureFlagService.getFlags()).thenReturn(listOf(flag))

        mockMvc.perform(get("/api/v1/flags"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Fetched all feature flags successfully"))
            .andExpect(jsonPath("$.data[0].key").value("test-flag"))
            .andExpect(jsonPath("$.data[0].status").value(true))
            .andExpect(jsonPath("$.data[0].type").value("BOOLEAN"))
            .andExpect(jsonPath("$.data[0].defaultValue.boolean").value(false))
            .andExpect(jsonPath("$.data[0].description").value("Test Flag"))
            .andExpect(jsonPath("$.data[0].variants[0].boolean").value(false))
            .andExpect(jsonPath("$.data[0].variants[1].US").value(true))
            .andExpect(jsonPath("$.data[0].variants[2].KR").value(true))
            .andExpect(jsonPath("$.data[0].createdBy").value("username"))
            .andExpect(jsonPath("$.data[0].updatedBy").value("username"))
            .andExpect(jsonPath("$.data[0].createdAt").exists())
            .andExpect(jsonPath("$.data[0].updatedAt").exists())
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `GET flags by key should get a specific feature flag successfully`() {
        `when`(featureFlagService.getFlagOrThrow("test-flag")).thenReturn(flag)

        mockMvc.perform(get("/api/v1/flags/test-flag"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Fetched a flag successfully"))
            .andExpect(jsonPath("$.data.key").value("test-flag"))
            .andExpect(jsonPath("$.data.status").value(true))
            .andExpect(jsonPath("$.data.type").value("BOOLEAN"))
            .andExpect(jsonPath("$.data.defaultValue.boolean").value(false))
            .andExpect(jsonPath("$.data.description").value("Test Flag"))
            .andExpect(jsonPath("$.data.variants[0].boolean").value(false))
            .andExpect(jsonPath("$.data.variants[1].US").value(true))
            .andExpect(jsonPath("$.data.variants[2].KR").value(true))
            .andExpect(jsonPath("$.data.createdBy").value("username"))
            .andExpect(jsonPath("$.data.updatedBy").value("username"))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `GET flags by key return 400 when flag does not exists`() {
        `when`(featureFlagService.getFlagOrThrow("test-flag")).thenThrow(EntityNotFoundException())

        mockMvc.perform(get("/api/v1/flags/test-flag"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Entity not found"))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags should update a feature flag successfully`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "test-flag-updated",
                type = "BOOLEAN",
                description = "Updated description",
                defaultValue = mapOf("default" to true),
                variants = null,
            )
        val updated =
            FeatureFlag(
                id = flag.id,
                name = "test-flag-updated",
                description = "Updated description",
                type = flag.type,
                enabled = flag.enabled,
                createdBy = flag.createdBy,
                updatedBy = flag.updatedBy,
                defaultCondition = flag.defaultCondition,
                conditions = flag.conditions,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.update(user, "test-flag", request)).thenReturn(updated)

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Updated flag successfully"))
            .andExpect(jsonPath("$.data.key").value("test-flag-updated"))
            .andExpect(jsonPath("$.data.description").value("Updated description"))
            .andExpect(jsonPath("$.data.status").value(true))
            .andExpect(jsonPath("$.data.type").value("BOOLEAN"))
            .andExpect(jsonPath("$.data.defaultValue.boolean").value(false))
            .andExpect(jsonPath("$.data.description").value("Updated description"))
            .andExpect(jsonPath("$.data.variants[0].boolean").value(false))
            .andExpect(jsonPath("$.data.variants[1].US").value(true))
            .andExpect(jsonPath("$.data.variants[2].KR").value(true))
            .andExpect(jsonPath("$.data.createdBy").value("username"))
            .andExpect(jsonPath("$.data.updatedBy").value("username"))
            .andExpect(jsonPath("$.data.createdAt").exists())
            .andExpect(jsonPath("$.data.updatedAt").exists())
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags return 400 when flag does not exists`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "test-flag-updated",
                type = "BOOLEAN",
                description = "Updated description",
                defaultValue = mapOf("default" to true),
                variants = null,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.update(user, "test-flag", request)).thenThrow(EntityNotFoundException())

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Entity not found"))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags should return 400 when request validation fails due to blank key`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "",
                type = "BOOLEAN",
                description = "Updated description",
                defaultValue = mapOf("default" to true),
                variants = null,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Key is required."))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags should return 400 when request validation fails due to invalid type`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "test-flag",
                type = "INVALID_TYPE",
                description = "Updated description",
                defaultValue = mapOf("default" to true),
                variants = null,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Type must be one of: number, boolean, string"))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags should return 400 when request validation fails due to empty defaultValue`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "test-flag",
                type = "BOOLEAN",
                description = "Updated description",
                defaultValue = emptyMap(),
                variants = null,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Default value is required."))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PUT flags should return 400 when request validation fails due to blank description`() {
        val request =
            UpdateFeatureFlagRequest(
                key = "test-flag",
                type = "BOOLEAN",
                description = "",
                defaultValue = mapOf("default" to true),
                variants = null,
            )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        mockMvc.perform(
            put("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Description is required."))
    }

    @Test
    @WithMockUser(username = "1")
    fun `PATCH flags should update feature flag status successfully`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        doNothing().`when`(featureFlagService).update(user, "test-flag", false)

        mockMvc.perform(
            patch("/api/v1/flags/test-flag?status=false")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Successfully updated the status"))
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `PATCH flags return 400 when flag does not exists`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.update(user, "test-flag", false)).thenThrow(EntityNotFoundException())

        mockMvc.perform(
            patch("/api/v1/flags/test-flag?status=false")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Entity not found"))
    }

    @Test
    @WithMockUser(username = "1")
    fun `DELETE flags should delete a feature flag successfully`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        doNothing().`when`(featureFlagService).delete(user, "test-flag")

        mockMvc.perform(
            delete("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Success"))
            .andExpect(jsonPath("$.message").value("Successfully deleted the feature flag"))
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "1")
    fun `DELETE flags return 400 when flag does not exist`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(featureFlagService.delete(any(), any(), any())).thenThrow(EntityNotFoundException())

        mockMvc.perform(
            delete("/api/v1/flags/test-flag")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Entity not found"))
    }
}
