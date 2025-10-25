package com.example.springdddexample.presentation.exception

import java.time.LocalDateTime

/**
 * エラーレスポンスDTO
 */
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
)
