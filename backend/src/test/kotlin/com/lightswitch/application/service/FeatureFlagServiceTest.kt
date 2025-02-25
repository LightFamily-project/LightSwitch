package com.lightswitch.application.service

import com.lightswitch.infrastructue.database.repository.BaseRepositoryTest
import com.lightswitch.infrastructure.database.entity.Condition
import com.lightswitch.infrastructure.database.entity.FeatureFlag
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.model.Type
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
import java.time.Instant
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
        conditionRepository.deleteAll()
        featureFlagRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `getFlags should return all feature flags`() {
        val user =
            userRepository.save(
                User(
                    username = "test-user",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )
        val flag1 =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "feature-1",
                    description = "Feature Flag 1",
                    type = Type.BOOLEAN,
                    enabled = true,
                    createdBy = user,
                    updatedBy = user,
                ),
            )
        val flag2 =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "feature-2",
                    description = "Feature Flag 2",
                    type = Type.NUMBER,
                    enabled = false,
                    createdBy = user,
                    updatedBy = user,
                ),
            )
        val flag3 =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "feature-3",
                    description = "Feature Flag 3",
                    type = Type.STRING,
                    enabled = true,
                    createdBy = user,
                    updatedBy = user,
                ),
            )
        flag1.defaultCondition = Condition(flag = flag1, key = "boolean", value = true)
        flag2.defaultCondition = Condition(flag = flag2, key = "number", value = 10)
        flag3.defaultCondition = Condition(flag = flag3, key = "string", value = "value")

        val flags = featureFlagService.getFlags()

        assertThat(flags).hasSize(3)
        assertThat(flags)
            .extracting("name", "description", "type", "enabled", "createdBy", "updatedBy")
            .containsExactly(
                tuple("feature-1", "Feature Flag 1", Type.BOOLEAN, true, user, user),
                tuple("feature-2", "Feature Flag 2", Type.NUMBER, false, user, user),
                tuple("feature-3", "Feature Flag 3", Type.STRING, true, user, user),
            )
        assertThat(flags)
            .extracting("defaultCondition.key", "defaultCondition.value")
            .containsExactlyInAnyOrder(
                tuple("boolean", true),
                tuple("number", 10),
                tuple("string", "value"),
            )
    }

    @Test
    fun `getFlags should return empty list when feature flag not exists`() {
        assertThat(featureFlagService.getFlags()).isEmpty()
    }

    @Test
    fun `getFlags should return empty list when all feature flags are deleted`() {
        val user =
            userRepository.save(
                User(
                    username = "test-user",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )
        val flag =
            FeatureFlag(
                name = "user-limit",
                description = "User Limit Flag",
                type = Type.NUMBER,
                enabled = true,
                createdBy = user,
                updatedBy = user,
            ).apply {
                this.deletedAt = Instant.now()
            }
        featureFlagRepository.save(flag)

        assertThat(featureFlagService.getFlags()).isEmpty()
    }

    @Test
    fun `getFlagOrThrow should return feature flag when key exists`() {
        val user =
            userRepository.save(
                User(
                    username = "test-user",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )
        val savedFlag =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "user-limit",
                    description = "User Limit Flag",
                    type = Type.NUMBER,
                    enabled = true,
                    createdBy = user,
                    updatedBy = user,
                ),
            )
        val defaultCondition = Condition(flag = savedFlag, key = "number", value = 10)
        val conditions =
            listOf(
                Condition(flag = savedFlag, key = "free", value = 10),
                Condition(flag = savedFlag, key = "pro", value = 100),
                Condition(flag = savedFlag, key = "enterprise", value = 1000),
            )
        featureFlagRepository.save(
            savedFlag.apply {
                this.defaultCondition = defaultCondition
                this.conditions.addAll(conditions)
                this.conditions.add(defaultCondition)
            },
        )

        val flag = featureFlagService.getFlagOrThrow("user-limit")

        assertThat(flag.id).isNotNull()
        assertThat(flag.name).isEqualTo("user-limit")
        assertThat(flag.description).isEqualTo("User Limit Flag")
        assertThat(flag.type).isEqualTo(Type.NUMBER)
        assertThat(flag.enabled).isTrue()
        assertThat(flag.createdBy).isEqualTo(user)
        assertThat(flag.updatedBy).isEqualTo(user)
        assertThat(flag.defaultCondition)
            .extracting("key", "value")
            .containsOnly("number", 10)
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
    fun `getFlagOrThrow should throw BusinessException when key does not exist`() {
        val nonExistentKey = "non-existent-key"

        assertThatThrownBy { featureFlagService.getFlagOrThrow(nonExistentKey) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining("Feature flag $nonExistentKey does not exist")
    }

    @Test
    fun `getFlagOrThrow should not return when feature flag is deleted`() {
        val user =
            userRepository.save(
                User(
                    username = "test-user",
                    passwordHash = "passwordHash",
                    lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
                ),
            )
        val flag =
            FeatureFlag(
                name = "user-limit",
                description = "User Limit Flag",
                type = Type.NUMBER,
                enabled = true,
                createdBy = user,
                updatedBy = user,
            ).apply {
                this.deletedAt = Instant.now()
            }
        featureFlagRepository.save(flag)

        assertThatThrownBy { featureFlagService.getFlagOrThrow("user-limit") }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining("Feature flag user-limit does not exist")
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
        assertThat(flag.type).isEqualTo(Type.NUMBER)
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
        assertThat(flag.type).isEqualTo(Type.NUMBER)
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

    @Test
    fun `create feature flag throw IllegalArgumentException when type is not supported`() {
        val request =
            CreateFeatureFlagRequest(
                key = "invalid-type",
                status = true,
                type = "json",
                defaultValue = mapOf("json" to 10),
                description = "Invalid Type Test",
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

        assertThatThrownBy { featureFlagService.create(user, request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported type: json")
    }
}
