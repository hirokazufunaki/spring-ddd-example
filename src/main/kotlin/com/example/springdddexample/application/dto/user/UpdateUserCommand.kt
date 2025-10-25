package com.example.springdddexample.application.dto.user

/**
 * ユーザー更新コマンド
 */
data class UpdateUserCommand(
    val id: String,
    val name: String,
    val email: String
)