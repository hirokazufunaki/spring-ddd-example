package com.example.springdddexample.infrastructure.persistence.jpa.entity.task

import com.example.springdddexample.infrastructure.persistence.jpa.entity.user.UserJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * TaskJPAエンティティ
 * データベーステーブルとのマッピング
 */
@Entity
@Table(name = "tasks")
data class TaskJpaEntity(
    @Id
    @Column(name = "id", length = 26)
    val id: String,
    @Column(name = "user_id", length = 26, nullable = false)
    val userId: String,
    @Column(name = "name", length = 255, nullable = false)
    val name: String,
    @Column(name = "description", columnDefinition = "TEXT")
    val description: String = "",
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    val status: TaskStatusEntity = TaskStatusEntity.NOT_STARTED,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    val user: UserJpaEntity? = null,
) {
    constructor() : this("", "", "", "", TaskStatusEntity.NOT_STARTED, LocalDateTime.now(), LocalDateTime.now(), null)
}

/**
 * タスク状態のEnum（JPA用）
 */
enum class TaskStatusEntity {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
