package com.example.springdddexample.domain.model.user

import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import java.time.LocalDateTime

/**
 * User集約ルート
 * ユーザーのライフサイクル全体を管理する
 */
data class User(
    val id: UserId,
    val name: UserName,
    val email: Email,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    
    /**
     * ユーザー情報を更新する
     * @param newName 新しいユーザー名
     * @param newEmail 新しいメールアドレス
     * @return 更新されたユーザー
     */
    fun updateProfile(newName: UserName, newEmail: Email): User {
        validateProfileUpdate(newName, newEmail)
        
        return this.copy(
            name = newName,
            email = newEmail,
            updatedAt = LocalDateTime.now()
        )
    }
    
    /**
     * ユーザー名のみ更新する
     */
    fun updateName(newName: UserName): User {
        return this.copy(
            name = newName,
            updatedAt = LocalDateTime.now()
        )
    }
    
    /**
     * メールアドレスのみ更新する
     */
    fun updateEmail(newEmail: Email): User {
        return this.copy(
            email = newEmail,
            updatedAt = LocalDateTime.now()
        )
    }
    
    private fun validateProfileUpdate(newName: UserName, newEmail: Email) {
        // ビジネスルールのバリデーション
        if (newName.value == this.name.value && newEmail.value == this.email.value) {
            throw BusinessRuleViolationException("更新する内容がありません")
        }
    }
    
    companion object {
        /**
         * 新しいユーザーを作成する
         */
        fun create(name: UserName, email: Email): User {
            return User(
                id = UserId.generate(),
                name = name,
                email = email
            )
        }
    }
}