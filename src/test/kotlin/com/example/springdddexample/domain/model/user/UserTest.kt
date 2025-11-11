package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration

/**
 * User集約ルートの単体テスト
 */
class UserTest {
    @Test
    fun `正常な値でUserが作成できること`() {
        // Arrange
        val name = UserName("山田太郎")
        val email = Email("yamada@example.com")

        // Act
        val user = User.create(name, email)

        // Assert
        assertNotNull(user.id)
        assertEquals(name, user.name)
        assertEquals(email, user.email)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
        assertTrue(user.createdAt.isBefore(user.updatedAt.plus(Duration.ofMillis(1))))
    }

    @Test
    fun `プロフィールが更新できること`() {
        // Arrange
        val user =
            User.create(
                name = UserName("山田太郎"),
                email = Email("yamada@example.com"),
            )
        val newName = UserName("田中花子")
        val newEmail = Email("tanaka@example.com")
        val originalUpdatedAt = user.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedUser = user.updateProfile(newName, newEmail)

        // Assert
        assertEquals(user.id, updatedUser.id)
        assertEquals(newName, updatedUser.name)
        assertEquals(newEmail, updatedUser.email)
        assertEquals(user.createdAt, updatedUser.createdAt)
        assertTrue(updatedUser.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `プロフィール更新で名前だけ変更できること`() {
        // Arrange
        val originalEmail = Email("yamada@example.com")
        val user = User.create(UserName("山田太郎"), originalEmail)
        val newName = UserName("田中花子")

        // Act
        val updatedUser = user.updateProfile(newName, originalEmail)

        // Assert
        assertEquals(newName, updatedUser.name)
        assertEquals(originalEmail, updatedUser.email)
    }

    @Test
    fun `プロフィール更新でメールだけ変更できること`() {
        // Arrange
        val originalName = UserName("山田太郎")
        val user = User.create(originalName, Email("yamada@example.com"))
        val newEmail = Email("tanaka@example.com")

        // Act
        val updatedUser = user.updateProfile(originalName, newEmail)

        // Assert
        assertEquals(originalName, updatedUser.name)
        assertEquals(newEmail, updatedUser.email)
    }

    @Test
    fun `名前のみ更新できること`() {
        // Arrange
        val user =
            User.create(
                name = UserName("山田太郎"),
                email = Email("yamada@example.com"),
            )
        val newName = UserName("田中花子")
        val originalUpdatedAt = user.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedUser = user.updateName(newName)

        // Assert
        assertEquals(user.id, updatedUser.id)
        assertEquals(newName, updatedUser.name)
        assertEquals(user.email, updatedUser.email)
        assertEquals(user.createdAt, updatedUser.createdAt)
        assertTrue(updatedUser.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `メールアドレスのみ更新できること`() {
        // Arrange
        val user =
            User.create(
                name = UserName("山田太郎"),
                email = Email("yamada@example.com"),
            )
        val newEmail = Email("yamada.new@example.com")
        val originalUpdatedAt = user.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedUser = user.updateEmail(newEmail)

        // Assert
        assertEquals(user.id, updatedUser.id)
        assertEquals(user.name, updatedUser.name)
        assertEquals(newEmail, updatedUser.email)
        assertEquals(user.createdAt, updatedUser.createdAt)
        assertTrue(updatedUser.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `同じ値でプロフィール更新するとエラーになること`() {
        // Arrange
        val name = UserName("山田太郎")
        val email = Email("yamada@example.com")
        val user = User.create(name, email)

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                user.updateProfile(name, email)
            }
        assertEquals("更新する内容がありません", exception.message)
    }
}
