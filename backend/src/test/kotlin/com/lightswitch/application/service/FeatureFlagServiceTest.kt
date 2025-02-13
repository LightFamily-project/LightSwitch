package com.lightswitch.application.service

import com.lightswitch.infrastructue.database.repository.BaseRepositoryTest
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.repository.ConditionRepository
import com.lightswitch.infrastructure.database.repository.FeatureFlagRepository
import com.lightswitch.infrastructure.database.repository.UserRepository
import com.lightswitch.presentation.exception.BusinessException
import com.lightswitch.presentation.model.flag.CreateFeatureFlagRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class FeatureFlagServiceTest : BaseRepositoryTest() {
    @Autowired
    private lateinit var featureFlagRepository: FeatureFlagRepository

    @Autowired
    private lateinit var conditionRepository: ConditionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var featureFlagService: FeatureFlagService

    @BeforeEach
    fun setUp() {
        featureFlagService = FeatureFlagService(conditionRepository, featureFlagRepository)
        conditionRepository.deleteAllInBatch()
        featureFlagRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
    }

    @Test
    fun `create feature flag can save feature flag & conditions`() {
        val request =
            CreateFeatureFlagRequest(
                key = "user-limit",
                status = true,
                type = "number",
                defaultValue = mapOf("number" to 10),
                description = "User Limit Flag",
                variants =
                    listOf(
                        mapOf("free" to 10),
                        mapOf("pro" to 100),
                        mapOf("enterprise" to 1000),
                    ),
            )
        val user =
            userRepository.save(
                User(
                    username = "username",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )

        val flag = featureFlagService.create(user, request)

        assertThat(flag.name).isEqualTo("user-limit")
        assertThat(flag.description).isEqualTo("User Limit Flag")
        assertThat(flag.type).isEqualTo("number")
        assertThat(flag.enabled).isTrue()
        assertThat(flag.createdBy).isEqualTo(user)
        assertThat(flag.updatedBy).isEqualTo(user)
        assertThat(flag.defaultCondition)
            .extracting("flag", "key", "value")
            .containsOnly(flag, "number", 10)
        assertThat(flag.conditions)
            .hasSize(4)
            .extracting("key", "value")
            .containsExactlyInAnyOrder(
                tuple("number", 10),
                tuple("free", 10),
                tuple("pro", 100),
                tuple("enterprise", 1000),
            )
    }

    @Test
    fun `create feature flag with only defaultValue`() {
        val request =
            CreateFeatureFlagRequest(
                key = "user-limit",
                status = true,
                type = "number",
                defaultValue = mapOf("number" to 10),
                description = "User Limit Flag",
                variants = null,
            )
        val user =
            userRepository.save(
                User(
                    username = "username",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )

        val flag = featureFlagService.create(user, request)

        assertThat(flag.name).isEqualTo("user-limit")
        assertThat(flag.description).isEqualTo("User Limit Flag")
        assertThat(flag.type).isEqualTo("number")
        assertThat(flag.enabled).isTrue()
        assertThat(flag.createdBy).isEqualTo(user)
        assertThat(flag.updatedBy).isEqualTo(user)
        assertThat(flag.defaultCondition)
            .extracting("flag", "key", "value")
            .containsOnly(flag, "number", 10)
        assertThat(flag.conditions)
            .hasSize(1)
            .extracting("key", "value")
            .containsExactly(tuple("number", 10))
    }

    @Test
    fun `create feature flag throw BusinessException when duplicate key exists`() {
        val request =
            CreateFeatureFlagRequest(
                key = "duplicate-key",
                status = true,
                type = "number",
                defaultValue = mapOf("number" to 10),
                description = "Duplicate Key Test",
                variants = listOf(mapOf("free" to 10)),
            )
        val user =
            userRepository.save(
                User(
                    username = "username",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )

        featureFlagService.create(user, request)

        assertThatThrownBy { featureFlagService.create(user, request) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining("FeatureFlag with key duplicate-key already exists")
    }
}
