package com.example.springdddexample.application.dto.task

/**
 * タスク作成入力DTO
 */
data class CreateTaskInput(
    val userId: String,
    val name: String,
    val description: String = "",
)
