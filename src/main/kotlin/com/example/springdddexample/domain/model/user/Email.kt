package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException

/**
 * メールアドレス値オブジェクト
 */
data class Email(val value: String) {
    
    init {
        require(value.isNotBlank()) { "メールアドレスは空にできません" }
        if (!isValidEmail(value)) {
            throw InvalidValueException("無効なメールアドレス形式です: $value")
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return email.matches(emailRegex) && email.length <= 254
    }
}