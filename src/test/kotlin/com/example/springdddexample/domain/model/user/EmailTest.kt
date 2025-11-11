package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Emailの単体テスト
 */
class EmailTest {
    @Test
    fun `正常なメールアドレスでEmailが作成できること`() {
        // Arrange
        val validEmail = "test@example.com"

        // Act
        val email = Email(validEmail)

        // Assert
        assertEquals(validEmail, email.value)
    }

    @Test
    fun `日本企業のドメインのメールアドレスでEmailが作成できること`() {
        // Arrange
        val validEmail = "yamada@example.co.jp"

        // Act
        val email = Email(validEmail)

        // Assert
        assertEquals(validEmail, email.value)
    }

    @Test
    fun `プラス記号を含むメールアドレスでEmailが作成できること`() {
        // Arrange
        val validEmail = "test+label@example.com"

        // Act
        val email = Email(validEmail)

        // Assert
        assertEquals(validEmail, email.value)
    }

    @Test
    fun `ドットを含むローカル部のメールアドレスでEmailが作成できること`() {
        // Arrange
        val validEmail = "first.last@example.com"

        // Act
        val email = Email(validEmail)

        // Assert
        assertEquals(validEmail, email.value)
    }

    @Test
    fun `空文字でEmailを作成するとエラーになること`() {
        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                Email("")
            }
        assertEquals("メールアドレスは空にできません", exception.message)
    }

    @Test
    fun `空白文字でEmailを作成するとエラーになること`() {
        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                Email("   ")
            }
        assertEquals("メールアドレスは空にできません", exception.message)
    }

    @Test
    fun `アットマークがないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "testexample.com"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                Email(invalidEmail)
            }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }

    @Test
    fun `ローカル部がないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "@example.com"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                Email(invalidEmail)
            }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }

    @Test
    fun `ドメイン部がないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "test@"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                Email(invalidEmail)
            }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }

    @Test
    fun `TLDがないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "test@example"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                Email(invalidEmail)
            }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }

    @Test
    fun `254文字のメールアドレスでEmailが作成できること`() {
        // Arrange
        val localPart = "a".repeat(242) // 254文字になるように調整
        val validEmail = "$localPart@example.com"

        // Act
        val email = Email(validEmail)

        // Assert
        assertEquals(validEmail, email.value)
    }

    @Test
    fun `255文字のメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val localPart = "a".repeat(243) // 255文字になるように調整
        val invalidEmail = "$localPart@example.com"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                Email(invalidEmail)
            }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
}
