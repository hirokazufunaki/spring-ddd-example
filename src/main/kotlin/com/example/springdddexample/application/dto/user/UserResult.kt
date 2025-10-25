package com.example.springdddexample.application.dto.user

import com.example.springdddexample.domain.model.user.User
import java.time.LocalDateTime

/**
 * ユーザー結果DTO
 * アプリケーションサービスからプレゼンテーション層への結果データ
 */
data class UserResult(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * ドメインオブジェクトからUserResultを生成
         */
        fun from(user: User): UserResult =
            UserResult(
                id = user.id.value,
                name = user.name.value,
                email = user.email.value,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
    }
}
