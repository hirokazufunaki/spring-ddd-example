package com.example.springdddexample.application.service.user

import com.example.springdddexample.application.dto.user.CreateUserInput
import com.example.springdddexample.infrastructure.persistence.jpa.repository.user.UserJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

/**
 * UserApplicationServiceの統合テスト
 */
@SpringBootTest
@ActiveProfiles("test")
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
}
