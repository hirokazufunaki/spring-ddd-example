package com.example.springdddexample.domain.model.task

import com.example.springdddexample.domain.shared.InvalidValueException

/**
 * タスク名値オブジェクト
 */
data class TaskName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "タスク名は空にできません" }
        if (value.length > 255) {
            throw InvalidValueException("タスク名は255文字以下にしてください")
        }
        if (value.length < 1) {
            throw InvalidValueException("タスク名は1文字以上にしてください")
        }
    }
}
