package com.lightswitch.infrastructure.database.repository

import com.lightswitch.infrastructure.database.entity.Condition
import com.lightswitch.infrastructure.database.entity.FeatureFlag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ConditionRepository : JpaRepository<Condition, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Condition c where c.flag = :flag")
    fun deleteByFlag(flag: FeatureFlag)
}
