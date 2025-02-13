package com.lightswitch.infrastructure.database.repository

import com.lightswitch.infrastructure.database.entity.Condition
import org.springframework.data.jpa.repository.JpaRepository

interface ConditionRepository : JpaRepository<Condition, Long>
