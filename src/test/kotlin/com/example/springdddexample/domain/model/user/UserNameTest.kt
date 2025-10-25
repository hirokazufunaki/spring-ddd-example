package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.InvalidValueException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * UserNameの単体テスト
 */
class UserNameTest {
    
    @Test
    fun `正常な名前でUserNameが作成できること`() {
        // Arrange
        val validName = "山田太郎"
        
        // Act
        val userName = UserName(validName)
        
        // Assert
        assertEquals(validName, userName.value)
    }
    
    @Test
    fun `2文字の名前でUserNameが作成できること`() {
        // Arrange
        val name = "田中"
        
        // Act
        val userName = UserName(name)
        
        // Assert
        assertEquals(name, userName.value)
    }
    
    @Test
    fun `50文字の名前でUserNameが作成できること`() {
        // Arrange
        val name = "あ".repeat(50)
        
        // Act
        val userName = UserName(name)
        
        // Assert
        assertEquals(name, userName.value)
    }
    
    @Test
    fun `空文字でUserNameを作成するとエラーになること`() {
        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            UserName("")
        }
        assertEquals("ユーザー名は空にできません", exception.message)
    }
    
    @Test
    fun `空白文字でUserNameを作成するとエラーになること`() {
        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            UserName("   ")
        }
        assertEquals("ユーザー名は空にできません", exception.message)
    }
    
    @Test
    fun `1文字の名前でUserNameを作成するとエラーになること`() {
        // Arrange
        val shortName = "田"
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            UserName(shortName)
        }
        assertEquals("ユーザー名は2文字以上で入力してください", exception.message)
    }
    
    @Test
    fun `51文字の名前でUserNameを作成するとエラーになること`() {
        // Arrange
        val longName = "あ".repeat(51)
        
        // Act & Assert
        val exception = assertThrows<InvalidValueException> {
            UserName(longName)
        }
        assertEquals("ユーザー名は50文字以内で入力してください", exception.message)
    }
    
    @Test
    fun `同じ値のUserNameは等価であること`() {
        // Arrange
        val name = "山田太郎"
        val userName1 = UserName(name)
        val userName2 = UserName(name)
        
        // Assert
        assertEquals(userName1, userName2)
        assertEquals(userName1.hashCode(), userName2.hashCode())
    }
    
    @Test
    fun `異なる値のUserNameは等価でないこと`() {
        // Arrange
        val userName1 = UserName("山田太郎")
        val userName2 = UserName("田中花子")
        
        // Assert
        assertNotEquals(userName1, userName2)
    }
}