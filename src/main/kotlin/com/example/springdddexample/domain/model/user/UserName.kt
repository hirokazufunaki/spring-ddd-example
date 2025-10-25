package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException

/**
 * ユーザー名値オブジェクト
 */
data class UserName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "ユーザー名は空にできません" }
        if (value.length > 50) {
            throw InvalidValueException("ユーザー名は50文字以内で入力してください")
        }
        if (value.length < 2) {
            throw InvalidValueException("ユーザー名は2文字以上で入力してください")
        }
    }
}
