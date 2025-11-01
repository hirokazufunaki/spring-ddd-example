package com.example.springdddexample.application.dto.task

/**
 * タスク更新入力DTO
 */
data class UpdateTaskInput(
    val taskId: String,
    val name: String,
    val description: String,
)
