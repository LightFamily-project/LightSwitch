package com.lightswitch.infrastructure.database.repository

import com.lightswitch.infrastructure.database.entity.FeatureFlag
import org.springframework.data.jpa.repository.JpaRepository

interface FeatureFlagRepository : JpaRepository<FeatureFlag, Int> {
    fun findByName(name: String): FeatureFlag?
}
