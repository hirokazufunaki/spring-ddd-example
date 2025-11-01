package com.example.springdddexample.presentation.dto.task

import io.swagger.v3.oas.annotations.media.Schema

/**
 * タスク一覧レスポンスDTO
 */
@Schema(description = "タスク一覧情報")
data class TaskListResponse(
    @Schema(description = "タスク一覧")
    val tasks: List<TaskResponse>,
) {
    companion object {
        /**
         * TaskResponseのリストからTaskListResponseを生成するファクトリ関数
         */
        fun from(tasks: List<TaskResponse>): TaskListResponse =
            TaskListResponse(
                tasks = tasks,
            )
    }
}
