package com.example.springdddexample.application.dto.task

import com.example.springdddexample.domain.model.task.Task
import java.time.LocalDateTime

/**
 * タスク出力DTO
 * アプリケーションサービスからプレゼンテーション層への結果データ
 */
data class TaskOutput(
    val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * ドメインオブジェクトからTaskOutputを生成
         */
        fun from(task: Task): TaskOutput =
            TaskOutput(
                id = task.id.value,
                userId = task.userId.value,
                name = task.name.value,
                description = task.description,
                status = task.status.displayName,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt,
            )
    }
}
