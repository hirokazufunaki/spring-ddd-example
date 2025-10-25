package com.example.springdddexample.domain.shared

/**
 * ドメイン例外の基底クラス
 * すべてのドメイン固有の例外はこのクラスを継承する
 */
abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * ドメインオブジェクトが見つからない場合の例外
 */
class NotFoundException(message: String) : DomainException(message)

/**
 * ドメインビジネスルール違反の例外
 */
class BusinessRuleViolationException(message: String) : DomainException(message)

/**
 * 無効な値オブジェクトの例外
 */
class InvalidValueException(message: String) : DomainException(message)