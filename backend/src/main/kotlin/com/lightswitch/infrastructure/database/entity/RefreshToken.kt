package com.lightswitch.infrastructure.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class RefreshToken (
    @Id
    @Column(name = "rt_key", nullable = false)
    val key: Long,
    @Column(name = "rt_value", nullable = false)
    var value: String
): BaseEntity()