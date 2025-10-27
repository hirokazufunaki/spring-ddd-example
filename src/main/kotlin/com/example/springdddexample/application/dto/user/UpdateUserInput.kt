package com.example.springdddexample.application.dto.user

/**
 * ユーザー更新入力DTO
 */
data class UpdateUserInput(
    val id: String,
    val name: String,
    val email: String,
)
