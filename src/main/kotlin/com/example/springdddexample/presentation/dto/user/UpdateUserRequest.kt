package com.example.springdddexample.presentation.dto.user

import com.example.springdddexample.application.dto.user.UpdateUserInput
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * ユーザー更新リクエストDTO
 */
data class UpdateUserRequest(
    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 2, max = 50, message = "ユーザー名は2文字以上50文字以内で入力してください")
    val name: String,
    @field:NotBlank(message = "メールアドレスは必須です")
    @field:Email(message = "正しいメールアドレス形式で入力してください")
    @field:Size(max = 254, message = "メールアドレスは254文字以内で入力してください")
    val email: String,
) {
    /**
     * UpdateUserInputへ変換する拡張関数
     */
    fun toInput(id: String): UpdateUserInput =
        UpdateUserInput(
            id = id,
            name = this.name,
            email = this.email,
        )
}
