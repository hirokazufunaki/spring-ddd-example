package com.example.springdddexample.presentation.dto.user

import com.example.springdddexample.application.dto.user.CreateUserCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * ユーザー作成リクエストDTO
 */
@Schema(description = "ユーザー作成リクエスト")
data class CreateUserRequest(
    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 2, max = 50, message = "ユーザー名は2文字以上50文字以内で入力してください")
    @Schema(description = "ユーザー名", example = "山田太郎", minLength = 2, maxLength = 50)
    val name: String,
    @field:NotBlank(message = "メールアドレスは必須です")
    @field:Email(message = "正しいメールアドレス形式で入力してください")
    @field:Size(max = 254, message = "メールアドレスは254文字以内で入力してください")
    @Schema(description = "メールアドレス", example = "yamada@example.com", format = "email", maxLength = 254)
    val email: String,
) {
    /**
     * CreateUserCommandへ変換する拡張関数
     */
    fun toCommand(): CreateUserCommand =
        CreateUserCommand(
            name = this.name,
            email = this.email,
        )
}
