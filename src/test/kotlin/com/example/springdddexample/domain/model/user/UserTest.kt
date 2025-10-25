package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

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
        assertTrue(user.createdAt.isEqual(user.updatedAt) || user.createdAt.isBefore(user.updatedAt.plusNanos(1000000)))
    }

    @Test
    fun `ユーザー作成時にULIDが生成されること`() {
        // Arrange
        val name = UserName("山田太郎")
        val email = Email("yamada@example.com")

        // Act
        val user1 = User.create(name, email)
        val user2 = User.create(name, email)

        // Assert
        assertNotEquals(user1.id, user2.id)
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

    @Test
    fun `同じ値のUserは等価であること`() {
        // Arrange
        val userId = UserId("01HKGX123456789ABCDEFGHJKM")
        val name = UserName("山田太郎")
        val email = Email("yamada@example.com")
        val now = LocalDateTime.now()

        val user1 =
            User(
                id = userId,
                name = name,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        val user2 =
            User(
                id = userId,
                name = name,
                email = email,
                createdAt = now,
                updatedAt = now,
            )

        // Assert
        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `異なるIDのUserは等価でないこと`() {
        // Arrange
        val name = UserName("山田太郎")
        val email = Email("yamada@example.com")
        val now = LocalDateTime.now()

        val user1 =
            User(
                id = UserId("01HKGX123456789ABCDEFGHJKM"),
                name = name,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        val user2 =
            User(
                id = UserId("01HKGX123456789ABCDEFGHJKN"),
                name = name,
                email = email,
                createdAt = now,
                updatedAt = now,
            )

        // Assert
        assertNotEquals(user1, user2)
    }

    @Test
    fun `Userの不変性が保たれること`() {
        // Arrange
        val originalUser =
            User.create(
                name = UserName("山田太郎"),
                email = Email("yamada@example.com"),
            )
        val newName = UserName("田中花子")
        val newEmail = Email("tanaka@example.com")

        // Act
        val updatedUser = originalUser.updateProfile(newName, newEmail)

        // Assert
        // 元のオブジェクトは変更されていないこと
        assertEquals(UserName("山田太郎"), originalUser.name)
        assertEquals(Email("yamada@example.com"), originalUser.email)

        // 新しいオブジェクトが作成されていること
        assertNotSame(originalUser, updatedUser)
        assertEquals(newName, updatedUser.name)
        assertEquals(newEmail, updatedUser.email)
    }
}
