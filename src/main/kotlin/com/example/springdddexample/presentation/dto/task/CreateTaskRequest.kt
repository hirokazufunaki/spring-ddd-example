package com.example.springdddexample.presentation.dto.task

import com.example.springdddexample.application.dto.task.CreateTaskInput
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * タスク作成リクエストDTO
 */
@Schema(description = "タスク作成リクエスト")
data class CreateTaskRequest(
    @field:NotBlank(message = "ユーザーIDは必須です")
    @Schema(description = "ユーザーID", example = "01HKGX123456789ABCDEFGHIJ")
    val userId: String,
    @field:NotBlank(message = "タスク名は必須です")
    @field:Size(min = 1, max = 255, message = "タスク名は1文字以上255文字以内で入力してください")
    @Schema(description = "タスク名", example = "プロジェクトAの開発", minLength = 1, maxLength = 255)
    val name: String,
    @field:Size(max = 2000, message = "説明は2000文字以内で入力してください")
    @Schema(description = "タスク説明", example = "プロジェクトAの開発タスク", maxLength = 2000)
    val description: String = "",
) {
    /**
     * CreateTaskInputへ変換する拡張関数
     */
    fun toInput(): CreateTaskInput =
        CreateTaskInput(
            userId = this.userId,
            name = this.name,
            description = this.description,
        )
}
