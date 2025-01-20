package com.lightswitch.infrastructure.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@Entity
class FeatureFlag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var defaultConditionId: Long? = null,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val type: String,
    @Column(nullable = false)
    var enabled: Boolean,
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    var createdBy: User,
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    var updatedBy: User,
) : BaseEntity()
