package com.example.springdddexample.application.dto.user

/**
 * ユーザー作成コマンド
 */
data class CreateUserCommand(
    val name: String,
    val email: String
)