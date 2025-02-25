package com.lightswitch.infrastructure.database.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
class SdkClient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    val sdkKey: String,
    @Column(nullable = false)
    val sdkType: String,
    var connectedAt: Instant,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) : BaseEntity()
