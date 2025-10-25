package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException
import org.junit.jupiter.api.Assertions.*
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
    fun `日本のドメインのメールアドレスでEmailが作成できること`() {
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
        val exception = assertThrows<IllegalArgumentException> {
            Email("")
        }
        assertEquals("メールアドレスは空にできません", exception.message)
    }
    
    @Test
    fun `空白文字でEmailを作成するとエラーになること`() {
        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            Email("   ")
        }
        assertEquals("メールアドレスは空にできません", exception.message)
    }
    
    @Test
    fun `アットマークがないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "testexample.com"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            Email(invalidEmail)
        }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
    
    @Test
    fun `ローカル部がないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "@example.com"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            Email(invalidEmail)
        }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
    
    @Test
    fun `ドメイン部がないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "test@"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            Email(invalidEmail)
        }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
    
    @Test
    fun `TLDがないメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val invalidEmail = "test@example"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            Email(invalidEmail)
        }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
    
    @Test
    fun `254文字超過のメールアドレスでEmailを作成するとエラーになること`() {
        // Arrange
        val localPart = "a".repeat(245)  // 254文字を超えるように調整
        val invalidEmail = "${localPart}@example.com"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            Email(invalidEmail)
        }
        assertEquals("無効なメールアドレス形式です: $invalidEmail", exception.message)
    }
    
    @Test
    fun `同じ値のEmailは等価であること`() {
        // Arrange
        val emailValue = "test@example.com"
        val email1 = Email(emailValue)
        val email2 = Email(emailValue)
        
        // Assert
        assertEquals(email1, email2)
        assertEquals(email1.hashCode(), email2.hashCode())
    }
    
    @Test
    fun `異なる値のEmailは等価でないこと`() {
        // Arrange
        val email1 = Email("test1@example.com")
        val email2 = Email("test2@example.com")
        
        // Assert
        assertNotEquals(email1, email2)
    }
}