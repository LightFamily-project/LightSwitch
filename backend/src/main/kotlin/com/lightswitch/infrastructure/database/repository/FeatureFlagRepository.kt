package com.lightswitch.infrastructure.database.repository

import com.lightswitch.infrastructure.database.entity.FeatureFlag
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface FeatureFlagRepository : JpaRepository<FeatureFlag, Int> {
    @EntityGraph(attributePaths = ["createdBy", "updatedBy", "defaultCondition", "conditions"])
    fun findByName(name: String): FeatureFlag?

    @EntityGraph(attributePaths = ["createdBy", "updatedBy", "defaultCondition", "conditions"])
    override fun findAll(): List<FeatureFlag>
}
