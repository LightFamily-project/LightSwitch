package com.lightswitch.infrastructure.database.entity

import com.lightswitch.infrastructure.database.converter.JsonConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Condition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_id", nullable = false)
    var flag: FeatureFlag,
    @Column(nullable = false)
    var key: String,
    @Convert(converter = JsonConverter::class)
    @Column(nullable = false, columnDefinition = "TEXT")
    var value: Any,
) : BaseEntity()
