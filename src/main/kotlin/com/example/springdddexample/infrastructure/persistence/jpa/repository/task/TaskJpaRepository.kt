package com.example.springdddexample.infrastructure.persistence.jpa.repository.task

import com.example.springdddexample.infrastructure.persistence.jpa.entity.task.TaskJpaEntity
import com.example.springdddexample.infrastructure.persistence.jpa.entity.task.TaskStatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * TaskJPAリポジトリ
 * Spring Data JPAによるデータアクセス
 */
@Repository
interface TaskJpaRepository : JpaRepository<TaskJpaEntity, String> {
    /**
     * ユーザーIDでタスクを検索
     */
    fun findByUserId(userId: String): List<TaskJpaEntity>

    /**
     * ユーザーIDと状態でタスクを検索
     */
    fun findByUserIdAndStatus(
        userId: String,
        status: TaskStatusEntity,
    ): List<TaskJpaEntity>

    /**
     * ユーザーIDのタスクをすべて削除
     */
    fun deleteByUserId(userId: String)
}
