package com.example.springdddexample.presentation.dto.user

import com.example.springdddexample.application.dto.user.UserResult
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * ユーザーレスポンスDTO
 */
@Schema(description = "ユーザー情報")
data class UserResponse(
    @Schema(description = "ユーザーID", example = "01HKGX123456789ABCDEFGHIJ")
    val id: String,
    
    @Schema(description = "ユーザー名", example = "山田太郎")
    val name: String,
    
    @Schema(description = "メールアドレス", example = "yamada@example.com")
    val email: String,
    
    @Schema(description = "作成日時", example = "2024-01-01T00:00:00")
    val createdAt: LocalDateTime,
    
    @Schema(description = "更新日時", example = "2024-01-01T00:00:00")
    val updatedAt: LocalDateTime
) {
    companion object {
        /**
         * UserResultからUserResponseを生成するファクトリ関数
         */
        fun from(userResult: UserResult): UserResponse {
            return UserResponse(
                id = userResult.id,
                name = userResult.name,
                email = userResult.email,
                createdAt = userResult.createdAt,
                updatedAt = userResult.updatedAt
            )
        }
    }
}