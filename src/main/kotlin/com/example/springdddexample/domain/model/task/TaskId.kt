package com.example.springdddexample.domain.model.task

import com.example.springdddexample.domain.shared.InvalidValueException

/**
 * タスクID値オブジェクト（ULID形式）
 */
data class TaskId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "タスクIDは空にできません" }
        if (!isValidUlid(value)) {
            throw InvalidValueException("無効なULID形式です: $value")
        }
    }

    private fun isValidUlid(value: String): Boolean {
        // ULIDの基本的な形式チェック（26文字、Base32文字）
        return value.length == 26 && value.matches(Regex("[0123456789ABCDEFGHJKMNPQRSTVWXYZ]+"))
    }

    companion object {
        private val ULID_CHARS = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray()

        /**
         * 新しいタスクIDを生成
         */
        fun generate(): TaskId {
            // 簡易的なULID生成（実際のプロジェクトではULIDライブラリを使用）
            val timestamp = System.currentTimeMillis()
            val random = java.util.Random()
            val stringBuilder = StringBuilder(26)

            // タイムスタンプ部分（10文字）
            var t = timestamp
            for (i in 0 until 10) {
                stringBuilder.append(ULID_CHARS[(t % 32).toInt()])
                t /= 32
            }

            // ランダム部分（16文字）
            for (i in 0 until 16) {
                stringBuilder.append(ULID_CHARS[random.nextInt(32)])
            }

            return TaskId(stringBuilder.toString())
        }
    }
}
