package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * UserIdの単体テスト
 */
class UserIdTest {
    @Test
    fun `正常なULIDでUserIdが作成できること`() {
        // Arrange
        val validUlid = "01HGX123456789ABCDEFGHJKM"

        // Act
        val userId = UserId(validUlid)

        // Assert
        assertEquals(validUlid, userId.value)
    }

    @Test
    fun `UserIdが生成できること`() {
        // Act
        val userId = UserId.generate()

        // Assert
        assertNotNull(userId.value)
        assertEquals(26, userId.value.length)
        assertTrue(userId.value.matches(Regex("[0123456789ABCDEFGHJKMNPQRSTVWXYZ]+")))
    }

    @Test
    fun `空文字でUserIdを作成するとエラーになること`() {
        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                UserId("")
            }
        assertEquals("ユーザーIDは空にできません", exception.message)
    }

    @Test
    fun `空白文字でUserIdを作成するとエラーになること`() {
        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                UserId("   ")
            }
        assertEquals("ユーザーIDは空にできません", exception.message)
    }

    @Test
    fun `無効なULID形式でUserIdを作成するとエラーになること`() {
        // Arrange
        val invalidUlid = "invalid-ulid"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                UserId(invalidUlid)
            }
        assertEquals("無効なULID形式です: $invalidUlid", exception.message)
    }

    @Test
    fun `26文字未満のULIDでUserIdを作成するとエラーになること`() {
        // Arrange
        val shortUlid = "01HKGX123456789"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                UserId(shortUlid)
            }
        assertEquals("無効なULID形式です: $shortUlid", exception.message)
    }

    @Test
    fun `26文字超過のULIDでUserIdを作成するとエラーになること`() {
        // Arrange
        val longUlid = "01HKGX123456789ABCDEFGHIJK"

        // Act & Assert
        val exception =
            assertThrows<InvalidValueException> {
                UserId(longUlid)
            }
        assertEquals("無効なULID形式です: $longUlid", exception.message)
    }

    @Test
    fun `同じ値のUserIdは等価であること`() {
        // Arrange
        val value = "01HGX123456789ABCDEFGHJKM"
        val userId1 = UserId(value)
        val userId2 = UserId(value)

        // Assert
        assertEquals(userId1, userId2)
        assertEquals(userId1.hashCode(), userId2.hashCode())
    }

    @Test
    fun `異なる値のUserIdは等価でないこと`() {
        // Arrange
        val userId1 = UserId("01HGX123456789ABCDEFGHJKM")
        val userId2 = UserId("01HGX123456789ABCDEFGHJKN")

        // Assert
        assertNotEquals(userId1, userId2)
    }
}
