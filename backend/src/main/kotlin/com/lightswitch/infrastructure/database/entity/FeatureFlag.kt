package com.lightswitch.infrastructure.database.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@Entity
class FeatureFlag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "default_condition_id", referencedColumnName = "id", nullable = true)
    var defaultCondition: Condition? = null,
    @Column(nullable = false)
    val name: String,
    val description: String,
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
