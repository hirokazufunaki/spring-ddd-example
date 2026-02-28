package com.example.springdddexample.presentation.dto.task

import com.example.springdddexample.application.dto.task.TaskOutput
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * タスクレスポンスDTO
 */
@Schema(description = "タスク情報")
data class TaskResponse(
    @field:Schema(description = "タスクID", example = "01HKGX123456789ABCDEFGHIJ")
    val id: String,
    @field:Schema(description = "ユーザーID", example = "01HKGX123456789ABCDEFGHIJ")
    val userId: String,
    @field:Schema(description = "タスク名", example = "プロジェクトAの開発")
    val name: String,
    @field:Schema(description = "タスク説明", example = "プロジェクトAの開発タスク")
    val description: String,
    @field:Schema(description = "タスク状態", example = "未着手")
    val status: String,
    @field:Schema(description = "作成日時")
    val createdAt: LocalDateTime,
    @field:Schema(description = "更新日時")
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * TaskOutputからTaskResponseを生成するファクトリ関数
         */
        fun from(taskOutput: TaskOutput): TaskResponse =
            TaskResponse(
                id = taskOutput.id,
                userId = taskOutput.userId,
                name = taskOutput.name,
                description = taskOutput.description,
                status = taskOutput.status,
                createdAt = taskOutput.createdAt,
                updatedAt = taskOutput.updatedAt,
            )
    }
}
