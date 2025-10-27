package com.example.springdddexample.application.dto.user

import com.example.springdddexample.domain.model.user.User
import java.time.LocalDateTime

/**
 * ユーザー出力DTO
 * アプリケーションサービスからプレゼンテーション層への結果データ
 */
data class UserOutput(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * ドメインオブジェクトからUserOutputを生成
         */
        fun from(user: User): UserOutput =
            UserOutput(
                id = user.id.value,
                name = user.name.value,
                email = user.email.value,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
    }
}
