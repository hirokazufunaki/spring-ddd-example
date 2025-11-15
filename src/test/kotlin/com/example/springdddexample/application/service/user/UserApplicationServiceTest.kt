package com.example.springdddexample.application.service.user

import com.example.springdddexample.application.dto.user.CreateUserInput
import com.example.springdddexample.application.dto.user.UpdateUserInput
import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import com.example.springdddexample.domain.shared.NotFoundException
import com.example.springdddexample.infrastructure.persistence.jpa.repository.user.UserJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

/**
 * UserApplicationServiceの統合テスト
 */
@SpringBootTest
@Transactional
class UserApplicationServiceTest {
    @Autowired
    private lateinit var userApplicationService: UserApplicationService

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `正常な値でユーザーが作成できること`() {
        // Arrange
        val input =
            CreateUserInput(
                name = "山田太郎",
                email = "yamada@example.com",
            )
        val initialCount = userJpaRepository.count()

        // Act
        val output = userApplicationService.createUser(input)

        // Assert
        assertNotNull(output.id)
        assertEquals(input.name, output.name)
        assertEquals(input.email, output.email)
        assertNotNull(output.createdAt)
        assertNotNull(output.updatedAt)
        assertEquals(initialCount + 1, userJpaRepository.count())
    }

    @Test
    fun `既存のメールアドレスでユーザー作成するとエラーになること`() {
        // Arrange
        val existingEmail = "yamada@example.com"
        userApplicationService.createUser(
            CreateUserInput(
                name = "山田太郎",
                email = existingEmail,
            ),
        )
        val input =
            CreateUserInput(
                name = "田中花子",
                email = existingEmail,
            )

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                userApplicationService.createUser(input)
            }
        assertEquals("このメールアドレスは既に使用されています: $existingEmail", exception.message)
    }

    @Test
    fun `既存ユーザーの情報を更新できること`() {
        // Arrange
        val createdUser =
            userApplicationService.createUser(
                CreateUserInput(
                    name = "山田太郎",
                    email = "yamada@example.com",
                ),
            )
        val input =
            UpdateUserInput(
                id = createdUser.id,
                name = "田中花子",
                email = "tanaka@example.com",
            )

        // Act
        val output = userApplicationService.updateUser(input)

        // Assert
        assertEquals(createdUser.id, output.id)
        assertEquals(input.name, output.name)
        assertEquals(input.email, output.email)
    }

    @Test
    fun `別のユーザーのメールアドレスに更新するとエラーになること`() {
        // Arrange
        val user1 =
            userApplicationService.createUser(
                CreateUserInput(
                    name = "山田太郎",
                    email = "yamada@example.com",
                ),
            )
        val user2 =
            userApplicationService.createUser(
                CreateUserInput(
                    name = "田中花子",
                    email = "tanaka@example.com",
                ),
            )
        val input =
            UpdateUserInput(
                id = user1.id,
                name = "山田太郎（変更）",
                email = user2.email, // user2のメールアドレスに更新しようとする
            )

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                userApplicationService.updateUser(input)
            }
        assertEquals("このメールアドレスは既に使用されています: ${user2.email}", exception.message)
    }

    @Test
    fun `存在しないIDのユーザー更新はNotFoundExceptionが発生すること`() {
        // Arrange
        val input =
            UpdateUserInput(
                id = "00000000000000000000000000",
                name = "山田太郎",
                email = "yamada@example.com",
            )

        // Act & Assert
        val exception =
            assertThrows<NotFoundException> {
                userApplicationService.updateUser(input)
            }
        assertEquals("ユーザーが見つかりません: 00000000000000000000000000", exception.message)
    }

    @Test
    fun `ユーザーを削除すると取得できなくなること`() {
        // Arrange
        val createdUser =
            userApplicationService.createUser(
                CreateUserInput(
                    name = "山田太郎",
                    email = "yamada@example.com",
                ),
            )

        // Act
        userApplicationService.deleteUser(createdUser.id)

        // Assert
        val exception =
            assertThrows<NotFoundException> {
                userApplicationService.getUser(createdUser.id)
            }
        assertEquals("ユーザーが見つかりません: ${createdUser.id}", exception.message)
    }
}
