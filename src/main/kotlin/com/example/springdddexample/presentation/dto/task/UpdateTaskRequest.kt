package com.example.springdddexample.presentation.dto.task

import com.example.springdddexample.application.dto.task.UpdateTaskInput
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * タスク更新リクエストDTO
 */
@Schema(description = "タスク更新リクエスト")
data class UpdateTaskRequest(
    @field:NotBlank(message = "タスク名は必須です")
    @field:Size(min = 1, max = 255, message = "タスク名は1文字以上255文字以内で入力してください")
    @Schema(description = "タスク名", example = "プロジェクトAの開発", minLength = 1, maxLength = 255)
    val name: String,
    @field:Size(max = 2000, message = "説明は2000文字以内で入力してください")
    @Schema(description = "タスク説明", example = "プロジェクトAの開発タスク", maxLength = 2000)
    val description: String = "",
) {
    /**
     * UpdateTaskInputへ変換する拡張関数
     */
    fun toInput(taskId: String): UpdateTaskInput =
        UpdateTaskInput(
            taskId = taskId,
            name = this.name,
            description = this.description,
        )
}
