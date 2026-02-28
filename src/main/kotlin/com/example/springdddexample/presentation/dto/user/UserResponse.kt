package com.example.springdddexample.presentation.dto.user

import com.example.springdddexample.application.dto.user.UserOutput
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * ユーザーレスポンスDTO
 */
@Schema(description = "ユーザー情報")
data class UserResponse(
    @field:Schema(description = "ユーザーID", example = "01HKGX123456789ABCDEFGHIJ")
    val id: String,
    @field:Schema(description = "ユーザー名", example = "山田太郎")
    val name: String,
    @field:Schema(description = "メールアドレス", example = "yamada@example.com")
    val email: String,
    @field:Schema(description = "作成日時")
    val createdAt: LocalDateTime,
    @field:Schema(description = "更新日時")
    val updatedAt: LocalDateTime,
) {
    companion object {
        /**
         * UserOutputからUserResponseを生成するファクトリ関数
         */
        fun from(userOutput: UserOutput): UserResponse =
            UserResponse(
                id = userOutput.id,
                name = userOutput.name,
                email = userOutput.email,
                createdAt = userOutput.createdAt,
                updatedAt = userOutput.updatedAt,
            )
    }
}
