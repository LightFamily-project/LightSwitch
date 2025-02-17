package com.lightswitch.application.service

import com.lightswitch.infrastructure.database.entity.Condition
import com.lightswitch.infrastructure.database.entity.FeatureFlag
import com.lightswitch.infrastructure.database.entity.User
import com.lightswitch.infrastructure.database.repository.ConditionRepository
import com.lightswitch.infrastructure.database.repository.FeatureFlagRepository
import com.lightswitch.presentation.exception.BusinessException
import com.lightswitch.presentation.model.flag.CreateFeatureFlagRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeatureFlagService(
    private val conditionRepository: ConditionRepository,
    private val featureFlagRepository: FeatureFlagRepository,
) {
    fun getFlags(): List<FeatureFlag> {
        return featureFlagRepository.findAll()
    }

    fun getFlagOrThrow(key: String): FeatureFlag {
        return featureFlagRepository.findByName(key) ?: throw BusinessException("Feature flag $key does not exist")
    }

    @Transactional
    fun create(
        user: User,
        request: CreateFeatureFlagRequest,
    ): FeatureFlag {
        featureFlagRepository.findByName(request.key)?.let {
            throw BusinessException("FeatureFlag with key ${request.key} already exists")
        }

        val flag =
            featureFlagRepository.save(
                FeatureFlag(
                    name = request.key,
                    type = request.type,
                    enabled = request.status,
                    description = request.description,
                    createdBy = user,
                    updatedBy = user,
                ),
            )

        request.defaultValueAsPair()
            .let { (key, value) -> Condition(flag = flag, key = key, value = value) }
            .let { conditionRepository.save(it) }
            .also {
                flag.defaultCondition = it
                flag.conditions.add(it)
            }

        request.variantPairs()
            ?.map { variant -> Condition(flag = flag, key = variant.first, value = variant.second) }
            ?.let { conditionRepository.saveAll(it) }
            ?.also { flag.conditions.addAll(it) }

        return featureFlagRepository.save(flag)
    }
}
