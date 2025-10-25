package com.example.springdddexample.domain.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * ドメイン例外クラスの単体テスト
 */
class DomainExceptionTest {
    @Test
    fun `NotFoundExceptionが正しく作成されること`() {
        // Arrange
        val message = "ユーザーが見つかりません"

        // Act
        val exception = NotFoundException(message)

        // Assert
        assertEquals(message, exception.message)
        assertTrue(exception is DomainException)
        assertTrue(exception is RuntimeException)
        assertNull(exception.cause)
    }

    @Test
    fun `BusinessRuleViolationExceptionが正しく作成されること`() {
        // Arrange
        val message = "ビジネスルールに違反しています"

        // Act
        val exception = BusinessRuleViolationException(message)

        // Assert
        assertEquals(message, exception.message)
        assertTrue(exception is DomainException)
        assertTrue(exception is RuntimeException)
        assertNull(exception.cause)
    }

    @Test
    fun `InvalidValueExceptionが正しく作成されること`() {
        // Arrange
        val message = "無効な値です"

        // Act
        val exception = InvalidValueException(message)

        // Assert
        assertEquals(message, exception.message)
        assertTrue(exception is DomainException)
        assertTrue(exception is RuntimeException)
        assertNull(exception.cause)
    }

    @Test
    fun `DomainExceptionが原因付きで作成されること`() {
        // Arrange
        val message = "ドメインエラーが発生しました"
        val cause = IllegalArgumentException("原因となった例外")

        // Act
        val exception = object : DomainException(message, cause) {}

        // Assert
        assertEquals(message, exception.message)
        assertEquals(cause, exception.cause)
        assertTrue(exception is RuntimeException)
    }

    @Test
    fun `ドメイン例外の継承関係が正しいこと`() {
        // Act & Assert
        assertTrue(NotFoundException::class.java.isAssignableFrom(DomainException::class.java).not())
        assertTrue(DomainException::class.java.isAssignableFrom(NotFoundException::class.java))

        assertTrue(BusinessRuleViolationException::class.java.isAssignableFrom(DomainException::class.java).not())
        assertTrue(DomainException::class.java.isAssignableFrom(BusinessRuleViolationException::class.java))

        assertTrue(InvalidValueException::class.java.isAssignableFrom(DomainException::class.java).not())
        assertTrue(DomainException::class.java.isAssignableFrom(InvalidValueException::class.java))

        assertTrue(RuntimeException::class.java.isAssignableFrom(DomainException::class.java))
    }

    @Test
    fun `異なる例外クラスのメッセージ処理が正しいこと`() {
        // Arrange & Act
        val notFound = NotFoundException("見つかりません")
        val businessRule = BusinessRuleViolationException("ルール違反")
        val invalidValue = InvalidValueException("無効な値")

        // Assert
        assertEquals("見つかりません", notFound.message)
        assertEquals("ルール違反", businessRule.message)
        assertEquals("無効な値", invalidValue.message)

        // すべて異なる例外クラス
        assertNotEquals(notFound::class, businessRule::class)
        assertNotEquals(businessRule::class, invalidValue::class)
        assertNotEquals(invalidValue::class, notFound::class)
    }
}
