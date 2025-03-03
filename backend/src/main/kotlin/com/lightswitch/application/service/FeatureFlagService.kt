package com.lightswitch.application.service

import com.lightswitch.infrastructure.database.entity.FeatureFlag
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.model.Type
import com.lightswitch.infrastructure.database.repository.ConditionRepository
import com.lightswitch.infrastructure.database.repository.FeatureFlagRepository
import com.lightswitch.presentation.exception.BusinessException
import com.lightswitch.presentation.model.flag.CreateFeatureFlagRequest
import com.lightswitch.presentation.model.flag.UpdateFeatureFlagRequest
import com.lightswitch.presentation.model.flag.defaultValueAsPair
import com.lightswitch.presentation.model.flag.variantPairs
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class FeatureFlagService(
    private val conditionRepository: ConditionRepository,
    private val featureFlagRepository: FeatureFlagRepository,
) {
    fun getFlags(): List<FeatureFlag> {
        return featureFlagRepository.findAll()
    }

    fun getFlagOrThrow(key: String): FeatureFlag {
        return featureFlagRepository.findByName(key) ?: throw EntityNotFoundException("Feature flag $key does not exist")
    }

    @Transactional
    fun create(
        user: User,
        request: CreateFeatureFlagRequest,
    ): FeatureFlag {
        if (featureFlagRepository.existsByName(request.key)) {
            throw BusinessException("FeatureFlag with key ${request.key} already exists")
        }

        val flag =
            featureFlagRepository.save(
                FeatureFlag(
                    name = request.key,
                    type = Type.from(request.type),
                    enabled = request.status,
                    description = request.description,
                    createdBy = user,
                    updatedBy = user,
                ),
            )

        request.defaultValueAsPair()
            .let { (key, value) -> flag.addDefaultCondition(key = key, value = value) }
        request.variantPairs()
            ?.map { variant -> flag.addCondition(key = variant.first, value = variant.second) }

        return featureFlagRepository.save(flag)
    }

    @Transactional
    fun update(
        user: User,
        key: String,
        request: UpdateFeatureFlagRequest,
    ): FeatureFlag {
        if (request.key != key && featureFlagRepository.existsByName(request.key)) {
            throw BusinessException("FeatureFlag with key ${request.key} already exists")
        }

        val flag = getFlagOrThrow(key)
        flag.name = request.key
        flag.type = Type.from(request.type)
        flag.description = request.description
        flag.updatedBy = user

        flag.conditions.forEach { it.deletedAt = Instant.now() }
        conditionRepository.saveAllAndFlush(flag.conditions)

        flag.defaultCondition = null
        flag.conditions.clear()

        request.defaultValueAsPair()
            .let { (key, value) -> flag.addDefaultCondition(key = key, value = value) }
        request.variantPairs()
            ?.map { variant -> flag.addCondition(key = variant.first, value = variant.second) }

        return featureFlagRepository.save(flag)
    }

    @Transactional
    fun update(
        user: User,
        key: String,
        enabled: Boolean,
    ) {
        val flag = getFlagOrThrow(key)
        flag.enabled = enabled
        flag.updatedBy = user
        featureFlagRepository.save(flag)
    }

    @Transactional
    fun delete(
        user: User,
        key: String,
        deletedAt: Instant = Instant.now(),
    ) {
        val flag = getFlagOrThrow(key)

        flag.updatedBy = user
        flag.deletedAt = deletedAt
        flag.conditions.forEach { it.deletedAt = deletedAt }

        featureFlagRepository.save(flag)
        conditionRepository.saveAll(flag.conditions)
    }
}
