package com.example.springdddexample.infrastructure.persistence.jpa.entity.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * UserJPAエンティティ
 * データベーステーブルとのマッピング
 */
@Entity
@Table(name = "users")
data class UserJpaEntity(
    @Id
    @Column(name = "id", length = 26)
    val id: String,
    @Column(name = "name", length = 50, nullable = false)
    val name: String,
    @Column(name = "email", length = 254, nullable = false, unique = true)
    val email: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
) {
    constructor() : this("", "", "", LocalDateTime.now(), LocalDateTime.now())
}
