package com.lightswitch.infrastructure.database.entity

import com.lightswitch.infrastructure.database.model.Type
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@Entity
@SQLRestriction("deleted_at is null")
class FeatureFlag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var description: String,
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: Type,
    @Column(nullable = false)
    var enabled: Boolean,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "default_condition_id", referencedColumnName = "id", nullable = true)
    var defaultCondition: Condition? = null,
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flag", cascade = [CascadeType.ALL], orphanRemoval = true)
    val conditions: MutableList<Condition> = mutableListOf(),
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    var createdBy: User,
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    var updatedBy: User,
) : BaseEntity() {
    fun addDefaultCondition(
        key: String,
        value: Any,
    ) {
        val condition = Condition(flag = this, key = key, value = value)
        defaultCondition = condition
        conditions.add(condition)
    }

    fun addCondition(
        key: String,
        value: Any,
    ) {
        val condition = Condition(flag = this, key = key, value = value)
        conditions.add(condition)
    }
}
