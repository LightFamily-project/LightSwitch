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
import com.lightswitch.presentation.model.flag.UpdateFeatureFlagRequest
import jakarta.persistence.EntityNotFoundException
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
        val user = saveTestUser()
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
        val user = saveTestUser()
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
        val user = saveTestUser()
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
    fun `getFlagOrThrow should throw EntityNotFoundException when key does not exist`() {
        val nonExistentKey = "non-existent-key"

        assertThatThrownBy { featureFlagService.getFlagOrThrow(nonExistentKey) }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Feature flag $nonExistentKey does not exist")
    }

    @Test
    fun `getFlagOrThrow should not return when feature flag is deleted`() {
        val user = saveTestUser()
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
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Feature flag user-limit does not exist")
    }

    @Test
    fun `create feature flag can save feature flag & conditions`() {
        val user = saveTestUser()
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
        val user = saveTestUser()
        val request =
            CreateFeatureFlagRequest(
                key = "user-limit",
                status = true,
                type = "number",
                defaultValue = mapOf("number" to 10),
                description = "User Limit Flag",
                variants = null,
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
        val user = saveTestUser()

        featureFlagService.create(user, request)

        assertThatThrownBy { featureFlagService.create(user, request) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining("FeatureFlag with key duplicate-key already exists")
    }

    @Test
    fun `create feature flag throw IllegalArgumentException when type is not supported`() {
        val user = saveTestUser()
        val request =
            CreateFeatureFlagRequest(
                key = "invalid-type",
                status = true,
                type = "json",
                defaultValue = mapOf("json" to 10),
                description = "Invalid Type Test",
                variants = listOf(mapOf("free" to 10)),
            )

        assertThatThrownBy { featureFlagService.create(user, request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported type: json")
    }

    @Test
    fun `update feature flag can update feature flag`() {
        val user = saveTestUser()
        val flag =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "user-limit",
                    description = "description",
                    type = Type.BOOLEAN,
                    enabled = true,
                    createdBy = user,
                    updatedBy = user,
                ),
            ).apply {
                this.defaultCondition = Condition(flag = this, key = "boolean", value = true)
                this.conditions.add(this.defaultCondition!!)
                this.conditions.add(Condition(flag = this, key = "free", value = true))
            }.also {
                featureFlagRepository.save(it)
            }
        val request =
            UpdateFeatureFlagRequest(
                key = "user-limit-updated",
                type = "number",
                description = "Updated description",
                defaultValue = mapOf("number" to 123),
                variants =
                    listOf(
                        mapOf("free" to 10),
                        mapOf("pro" to 100),
                    ),
            )

        val updated = featureFlagService.update(user, "user-limit", request)

        assertThat(flag.id).isEqualTo(updated.id)
        assertThat(updated.name).isEqualTo("user-limit-updated")
        assertThat(updated.description).isEqualTo("Updated description")
        assertThat(updated.type).isEqualTo(Type.NUMBER)
        assertThat(updated.defaultCondition)
            .extracting("key", "value")
            .containsExactly("number", 123)
        assertThat(updated.conditions)
            .hasSize(3)
            .extracting("key", "value")
            .containsExactlyInAnyOrder(
                tuple("number", 123),
                tuple("free", 10),
                tuple("pro", 100),
            )
        assertThat(updated.updatedBy.id).isEqualTo(user.id)
    }

    @Test
    fun `update feature flag throw BusinessException if trying to rename to an existing key`() {
        val user = saveTestUser()
        featureFlagRepository.save(
            FeatureFlag(
                name = "flag-one",
                description = "Flag One",
                type = Type.STRING,
                enabled = true,
                createdBy = user,
                updatedBy = user,
            ),
        )
        featureFlagRepository.save(
            FeatureFlag(
                name = "flag-two",
                description = "Flag Two",
                type = Type.STRING,
                enabled = true,
                createdBy = user,
                updatedBy = user,
            ),
        )
        val request =
            UpdateFeatureFlagRequest(
                key = "flag-two",
                type = "boolean",
                description = "Try rename",
                defaultValue = mapOf("boolean" to true),
                variants = null,
            )

        assertThatThrownBy {
            featureFlagService.update(user, "flag-one", request)
        }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining("FeatureFlag with key flag-two already exists")
    }

    @Test
    fun `update feature flag throw EntityNotFoundException if trying to update non-existent flag`() {
        val user = saveTestUser()
        val nonExistentKey = "not-exist"
        val request =
            UpdateFeatureFlagRequest(
                key = "not-exist",
                type = "boolean",
                description = "Desc",
                defaultValue = mapOf("boolean" to false),
                variants = null,
            )

        assertThatThrownBy {
            featureFlagService.update(user, nonExistentKey, request)
        }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Feature flag $nonExistentKey does not exist")
    }

    @Test
    fun `update feature flag throw IllegalArgumentException if type is invalid`() {
        val user = saveTestUser()
        featureFlagRepository.save(
            FeatureFlag(
                name = "flag-invalid-type",
                description = "Test Flag",
                type = Type.BOOLEAN,
                enabled = true,
                createdBy = user,
                updatedBy = user,
            ),
        )
        val request =
            UpdateFeatureFlagRequest(
                key = "flag-invalid-type",
                type = "json",
                description = "Invalid Type",
                defaultValue = mapOf("json" to "whatever"),
                variants = null,
            )

        assertThatThrownBy {
            featureFlagService.update(user, "flag-invalid-type", request)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported type: json")
    }

    @Test
    fun `update should update feature flag status`() {
        val user = saveTestUser()
        val flag =
            featureFlagRepository.save(
                FeatureFlag(
                    name = "test-flag",
                    description = "description",
                    type = Type.BOOLEAN,
                    enabled = false,
                    createdBy = user,
                    updatedBy = user,
                ),
            )

        featureFlagService.update(user, "test-flag", true)

        val updatedFlag = featureFlagRepository.findById(flag.id!!.toInt()).orElseThrow()

        assertThat(updatedFlag.enabled).isTrue()
    }

    @Test
    fun `update should throw EntityNotFoundException when key does not exist`() {
        val user = saveTestUser()

        assertThatThrownBy {
            featureFlagService.update(user, "flag-one", true)
        }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("Feature flag flag-one does not exist")
    }

    private fun saveTestUser() =
        userRepository.save(
            User(
                username = "test-user",
                passwordHash = "test-pass",
                lastLoginAt = LocalDate.of(2025, 1, 1).atStartOfDay(),
            ),
        )
}
